package gr.hua.dit.distributed.project_46.controller;

import gr.hua.dit.distributed.project_46.config.JwtUtils;
import gr.hua.dit.distributed.project_46.entity.ERole;
import gr.hua.dit.distributed.project_46.entity.Person;
import gr.hua.dit.distributed.project_46.entity.Role;
import gr.hua.dit.distributed.project_46.entity.User;
import gr.hua.dit.distributed.project_46.payload.request.LoginRequest;
import gr.hua.dit.distributed.project_46.payload.request.SignupRequest;
import gr.hua.dit.distributed.project_46.payload.request.UpdatePasswordRequest;
import gr.hua.dit.distributed.project_46.payload.request.UpdateRequest;
import gr.hua.dit.distributed.project_46.payload.response.JwtResponse;
import gr.hua.dit.distributed.project_46.payload.response.MessageResponse;
import gr.hua.dit.distributed.project_46.repository.PersonRepository;
import gr.hua.dit.distributed.project_46.repository.RoleRepository;
import gr.hua.dit.distributed.project_46.repository.UserRepository;
import gr.hua.dit.distributed.project_46.service.UserDetailsImpl;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @GetMapping ("/user")
    public ResponseEntity<?> getUsers() {
        List<User> users=userRepository.findAll();
        JSONArray jo = new JSONArray();
        for (User user:users) {
            UserDetailsImpl userDetails = UserDetailsImpl.build(user);
            jo.add(userDetails.toJSON());
        }
        return ResponseEntity.ok(jo);
    }
    @GetMapping ("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            User user=userRepository.getById(id);
            UserDetailsImpl userDetails = UserDetailsImpl.build(user);
            return ResponseEntity.ok(userDetails.toJSON());
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User->"+id+" NOT Exists"));
        }
    }


    @PostMapping("/user")
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
    public ResponseEntity<?> delete(@PathVariable Long id) {
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
            String oldTin= user.getTin();
            user.setTin(updateRequest.getTin());

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

            if (!(oldTin.equals(updateRequest.getTin()))) {
                /* update tin of user's person data */
                if (personRepository.existsByTin(oldTin)) {
                    Person person = personRepository.findByTin(oldTin)
                            .orElseThrow(() -> new RuntimeException("User Not Found with Taxpayer Identification Number: " + oldTin));
                    person.setTin(updateRequest.getTin());
                    personRepository.save(person);
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