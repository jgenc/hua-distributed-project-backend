package gr.hua.dit.distributed.project_46.controller;

import gr.hua.dit.distributed.project_46.config.JwtUtils;
import gr.hua.dit.distributed.project_46.entity.*;
import gr.hua.dit.distributed.project_46.payload.request.SignupRequest;
import gr.hua.dit.distributed.project_46.payload.request.UpdatePasswordRequest;
import gr.hua.dit.distributed.project_46.payload.request.UpdateRequest;
import gr.hua.dit.distributed.project_46.payload.response.MessageResponse;
import gr.hua.dit.distributed.project_46.payload.response.UserResponse;
import gr.hua.dit.distributed.project_46.repository.DeclarationRepository;
import gr.hua.dit.distributed.project_46.repository.PersonRepository;
import gr.hua.dit.distributed.project_46.repository.RoleRepository;
import gr.hua.dit.distributed.project_46.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private DeclarationRepository declarationRepository;

    @GetMapping ("/user")
    /* Get All Users */
    public ResponseEntity<?> getUsers() {
        List<User> users=userRepository.findAll();
        Set<UserResponse> userResponse = new HashSet<>();
        for (User user:users) {
            userResponse.add(new UserResponse(user.getId(),user.getUsername(),user.getTin(),user.getRoles()));
        }
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping ("/user/{id}")
    /* Get User with id */
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            User user=userRepository.getById(id);
            return ResponseEntity.ok(new UserResponse(user.getId(),user.getUsername(),user.getTin(),user.getRoles()));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User->"+id+" NOT Exists"));
        }
    }

    @PostMapping("/user")
    /* Create New User */
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByTin(signUpRequest.getTin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Taxpayer Identification Number is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getTin(),
                encoder.encode(signUpRequest.getPassword()));

        String strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            strRoles="citizen";
        }

        Role userRole;
        if (strRoles.equals("notary")) {
            userRole = roleRepository.findByName(ERole.ROLE_NOTARY)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        } else {
            userRole = roleRepository.findByName(ERole.ROLE_CITIZEN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        }
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @DeleteMapping("/user/{id}")
    /* Delete User with id */
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            User user=userRepository.getById(id);
            if (user.getUsername().equals("admin")) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: You can't delete admin!"));
            }
            userRepository.deleteById(id);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User->"+id+" NOT Exists"));
        }
        return ResponseEntity.ok(new MessageResponse("User->"+id+" deleted successfully!"));
    }

    @PostMapping("/user/{id}")
    /* Modify user's password with id */
    public ResponseEntity<?> modifyPassword(@PathVariable Long id, @Valid @RequestBody UpdatePasswordRequest updateRequest) {
        if (userRepository.existsById(id)) {
            User user=userRepository.getById(id);
            user.setPassword(encoder.encode(updateRequest.getPassword()));
            userRepository.save(user);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User->"+id+" NOT Exists"));
        }
        return ResponseEntity.ok(new MessageResponse("The password of User->"+id+" modified successfully!"));
    }

    @PutMapping("/user/{id}")
    /* Modify User with id */
    public ResponseEntity<?> modifyUser(@PathVariable Long id, @Valid @RequestBody UpdateRequest updateRequest) {
        if (userRepository.existsById(id)) {
            User user=userRepository.getById(id);
            if (user.getUsername().equals("admin")) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: You can't modify admin!"));
            }

            if (userRepository.existsByUsername(updateRequest.getUsername())) {
                User otherUser = userRepository.findByUsername(updateRequest.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + updateRequest.getUsername()));
                if (user.getId()!=otherUser.getId()) {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Error: Username is already taken!"));
                }
            }

            if (userRepository.existsByTin(updateRequest.getTin())) {
                User otherUser = userRepository.findByTin(updateRequest.getTin())
                        .orElseThrow(() -> new RuntimeException("User Not Found with Taxpayer Identification Number: " + updateRequest.getTin()));
                if (user.getId()!=otherUser.getId()) {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Error: Taxpayer Identification Number is already in use in Users!"));
                }
            }

            if (personRepository.existsByTin(updateRequest.getTin()) && !(user.getTin().equals(updateRequest.getTin()))) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Taxpayer Identification Number is already in use in Persons!"));
            }

            user.setUsername(updateRequest.getUsername());
            String strRoles = updateRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (!(strRoles == null)) {
                Role userRole;
                if (strRoles.equals("notary")) {
                    userRole = roleRepository.findByName(ERole.ROLE_NOTARY)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                } else {
                    userRole = roleRepository.findByName(ERole.ROLE_CITIZEN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                }
                roles.add(userRole);

                user.setRoles(roles);
            }

            userRepository.save(user);
            if (!(user.getTin().equals(updateRequest.getTin()))) {
                /* update tin of user and user's personal data */
                if (personRepository.existsByTin(user.getTin())) {
                    Person person = personRepository.findByTin(user.getTin())
                            .orElseThrow(() -> new RuntimeException("Person Not Found with Taxpayer Identification Number: " + user.getTin()));

                    Person newperson = new Person(updateRequest.getTin(), person.getFirstName(), person.getLastName(), person.getAddress(), person.getDoy());
                    personRepository.save(newperson);

                    declarationRepository.updateNotary(newperson,person);
                    declarationRepository.updatePurchaser(newperson,person);
                    declarationRepository.updateSeller(newperson,person);

                    personRepository.delete(person);

                    user.setTin(updateRequest.getTin());
                    userRepository.save(user);
                }
            }
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User->"+id+" NOT Exists"));
        }
        return ResponseEntity.ok(new MessageResponse("User->"+id+" modified successfully!"));
    }
}