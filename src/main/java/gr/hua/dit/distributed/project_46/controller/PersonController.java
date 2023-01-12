package gr.hua.dit.distributed.project_46.controller;

import gr.hua.dit.distributed.project_46.config.JwtUtils;
import gr.hua.dit.distributed.project_46.entity.*;
import gr.hua.dit.distributed.project_46.payload.request.PersonRequest;
import gr.hua.dit.distributed.project_46.payload.response.MessageResponse;
import gr.hua.dit.distributed.project_46.repository.PersonRepository;
import gr.hua.dit.distributed.project_46.repository.UserRepository;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/person")
    /* Get All Persons */
    public ResponseEntity<?> getPersons() {
        List<Person> persons=personRepository.findAll();
        JSONArray jo = new JSONArray();
        for (Person person:persons) {
            jo.add(person);
        }
        return ResponseEntity.ok(jo);
    }

    @GetMapping("/person/{tin}")
    /* Get Person with TIN */
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
    /* Add or Modify Person's Data */
    public ResponseEntity<?> addPerson(@Valid @RequestBody PersonRequest personRequest, HttpServletRequest request) {
        String userTin="";
        try {
            userTin=jwtUtils.getUserTinFromJwtToken(request);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Cannot set user authentication: {}"+ e));
        }

        String message="Person added successfully!";
        if (personRepository.existsByTin(userTin)) {
            message="Person modified successfully!";
        }
            // Create new Person
        Person person = new Person(userTin, personRequest.getFirstName(), personRequest.getLastName(), personRequest.getAddress(), personRequest.getDoy());
        personRepository.save(person);

        return ResponseEntity.ok(new MessageResponse(message));
    }
}
