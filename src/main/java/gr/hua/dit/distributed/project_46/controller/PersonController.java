package gr.hua.dit.distributed.project_46.controller;

import gr.hua.dit.distributed.project_46.config.JwtUtils;
import gr.hua.dit.distributed.project_46.entity.*;
import gr.hua.dit.distributed.project_46.payload.request.SignupRequest;
import gr.hua.dit.distributed.project_46.payload.response.MessageResponse;
import gr.hua.dit.distributed.project_46.repository.DeclarationRepository;
import gr.hua.dit.distributed.project_46.repository.PersonRepository;
import gr.hua.dit.distributed.project_46.repository.UserRepository;
import gr.hua.dit.distributed.project_46.service.UserDetailsImpl;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class PersonController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    UserRepository userRepository;
     @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private DeclarationRepository declarationRepository;

    @GetMapping("/person")
    public ResponseEntity<?> getPersons() {
        List<Person> persons=personRepository.findAll();
        JSONArray jo = new JSONArray();
        for (Person person:persons) {
            jo.add(person);
        }
        return ResponseEntity.ok(jo);
    }

    @GetMapping("/person/{tin}")
    public ResponseEntity<?> getPerson(@PathVariable String tin) {
        if (personRepository.existsByTin(tin)) {
            Person person = personRepository.findByTin(tin)
                    .orElseThrow(() -> new RuntimeException("Person Not Found with Taxpayer Identification Number: " + tin));
            return ResponseEntity.ok(person);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Person Not Found with Taxpayer Identification Number: " + tin));
        }
    }

    @PostMapping("/person")
    public ResponseEntity<?> addPerson(@Valid @RequestBody Person newPerson, HttpServletRequest request) {
        String userTin="";
        try {
            userTin=jwtUtils.getUserTinFromJwtToken(request);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Cannot set user authentication: {}"+ e));
        }

        if (!(userTin.equals(newPerson.getTin()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Taxpayer Identification Number must be the user's!"));
        }

        if (personRepository.existsByTin(newPerson.getTin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Taxpayer Identification Number is already in use!"));
        }

        // Create new Person
        Person person = new Person(newPerson.getTin(), newPerson.getFirstName(), newPerson.getLastName(), newPerson.getAddress(), newPerson.getDoy());
        personRepository.save(person);

        return ResponseEntity.ok(new MessageResponse("Person added successfully!"));
    }

    @PutMapping("/person")
    public ResponseEntity<?> modifyPerson(@Valid @RequestBody Person newPerson, HttpServletRequest request) {
        String userTin="";
        try {
            userTin=jwtUtils.getUserTinFromJwtToken(request);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Cannot set user authentication: {}"+ e));
        }

        if (!(personRepository.existsByTin(userTin))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User has no Person Data!"));
        }

        if (personRepository.existsByTin(newPerson.getTin()) && !(userTin.equals(newPerson.getTin()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Taxpayer Identification Number is already in use in Persons!"));
        }

        if (userRepository.existsByTin(newPerson.getTin()) && !(userTin.equals(newPerson.getTin()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Taxpayer Identification Number is already in use in Users!"));
        }

        String finalUserTin = userTin;
        Person person=personRepository.findByTin(userTin)
                .orElseThrow(() -> new RuntimeException("Person Not Found with Taxpayer Identification Number: " + finalUserTin));

        // Modify Person Data
        person.setTin(newPerson.getTin());
        person.setFirstName(newPerson.getFirstName());
        person.setLastName(newPerson.getLastName());
        person.setAddress(newPerson.getAddress());
        person.setDoy(newPerson.getDoy());
        personRepository.save(person);

        if (!(userTin.equals(newPerson.getTin()))) {
            /* update tin of user */
            if (userRepository.existsByTin(userTin)) {
                User user = userRepository.findByTin(userTin)
                        .orElseThrow(() -> new RuntimeException("User Not Found with Taxpayer Identification Number: " + finalUserTin));
                user.setTin(newPerson.getTin());
                userRepository.save(user);
                }
            }

        return ResponseEntity.ok(new MessageResponse("Person modified successfully!"));
    }

}
