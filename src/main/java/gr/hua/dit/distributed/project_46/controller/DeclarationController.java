package gr.hua.dit.distributed.project_46.controller;

import gr.hua.dit.distributed.project_46.config.JwtUtils;
import gr.hua.dit.distributed.project_46.entity.Declaration;

import gr.hua.dit.distributed.project_46.entity.EStatus;
import gr.hua.dit.distributed.project_46.entity.Person;
import gr.hua.dit.distributed.project_46.payload.request.AddDeclarationRequest;
import gr.hua.dit.distributed.project_46.payload.request.CompleteDeclarationRequest;
import gr.hua.dit.distributed.project_46.payload.response.MessageResponse;
import gr.hua.dit.distributed.project_46.repository.PersonRepository;
import gr.hua.dit.distributed.project_46.repository.UserRepository;
import gr.hua.dit.distributed.project_46.repository.DeclarationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class DeclarationController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    DeclarationRepository declarationRepository;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/declaration")
    /* Get All user's declarations */
    public ResponseEntity<?> getDeclarations(HttpServletRequest request) {
        String userTin = "";
        try {
            userTin = jwtUtils.getUserTinFromJwtToken(request);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Cannot set user authentication: {}" + e));
        }

        String finalUserTin = userTin;
        Person person = personRepository.findByTin(userTin)
                .orElseThrow(() -> new RuntimeException("Person Not Found with Taxpayer Identification Number: " + finalUserTin));

        Set<Declaration> allDeclarations = Stream.of(person.getNotaryDeclarations(), person.getPurchaserDeclarations(),person.getSellerDeclarations())
                .flatMap(Set::stream)
                .collect(toSet());
        return ResponseEntity.ok(allDeclarations);
    }

    @GetMapping("/declaration/role/{role}")
    /* Get All user's declarations by role */
    public ResponseEntity<?> getDeclarationByRole(@PathVariable String role, HttpServletRequest request) {
        String userTin = "";
        try {
            userTin = jwtUtils.getUserTinFromJwtToken(request);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Cannot set user authentication: {}" + e));
        }

        String finalUserTin = userTin;
        Person person = personRepository.findByTin(userTin)
                .orElseThrow(() -> new RuntimeException("Person Not Found with Taxpayer Identification Number: " + finalUserTin));

        if (role.equals("notary")) {
            return ResponseEntity.ok(person.getNotaryDeclarations());
        } else if (role.equals("purchaser")) {
            return ResponseEntity.ok(person.getPurchaserDeclarations());
        } else if (role.equals("seller")) {
            return ResponseEntity.ok(person.getSellerDeclarations());
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Unknown role->" + role));
        }
   }

    @GetMapping("/declaration/{id}")
    /* Get user's declaration with id */
    public ResponseEntity<?> getDeclaration(@PathVariable Long id, HttpServletRequest request) {
        String userTin = "";
        try {
            userTin = jwtUtils.getUserTinFromJwtToken(request);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Cannot set user authentication: {}" + e));
        }

        String finalUserTin = userTin;
        Person person = personRepository.findByTin(userTin)
                .orElseThrow(() -> new RuntimeException("Person Not Found with Taxpayer Identification Number: " + finalUserTin));

        for (Declaration declaration : person.getNotaryDeclarations()) {
            if (declaration.getId() == id)
                return ResponseEntity.ok(declaration);
        }

        for (Declaration declaration : person.getPurchaserDeclarations()) {
            if (declaration.getId() == id)
                return ResponseEntity.ok(declaration);
        }

        for (Declaration declaration : person.getSellerDeclarations()) {
            if (declaration.getId() == id)
                return ResponseEntity.ok(declaration);
        }

        if (declarationRepository.existsById(id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: The User is not authorized for Declaration->" + id));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Declaration->" + id + " NOT Exists"));
        }
    }

    @PostMapping("/declaration")
    @PreAuthorize("hasRole('NOTARY')")
    /* Add Declaration */
    public ResponseEntity<?> addDeclaration(@Valid @RequestBody AddDeclarationRequest addDeclarationRequest, HttpServletRequest request) {
        String userTin = "";
        try {
            userTin = jwtUtils.getUserTinFromJwtToken(request);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Cannot set user authentication: {}" + e));
        }

       if (addDeclarationRequest.getPurchaserTin().equals(addDeclarationRequest.getSellerTin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Purchaser and Seller can not be the same!"));
        }

        if (!(personRepository.existsByTin(addDeclarationRequest.getPurchaserTin()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Purchaser's Data Not Found with Taxpayer Identification Number: " + addDeclarationRequest.getPurchaserTin()));
        }

        if (!(personRepository.existsByTin(addDeclarationRequest.getSellerTin()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Seller's Data Not Found with Taxpayer Identification Number: " + addDeclarationRequest.getSellerTin()));
        }

        String finalUserTin = userTin;
        Person notary = personRepository.findByTin(userTin)
                .orElseThrow(() -> new RuntimeException("Person Not Found with Taxpayer Identification Number: " + finalUserTin));

        Person purchaser = personRepository.findByTin(addDeclarationRequest.getPurchaserTin())
                .orElseThrow(() -> new RuntimeException("Person Not Found with Taxpayer Identification Number: " + addDeclarationRequest.getPurchaserTin()));

        Person seller = personRepository.findByTin(addDeclarationRequest.getSellerTin())
                .orElseThrow(() -> new RuntimeException("Person Not Found with Taxpayer Identification Number: " + addDeclarationRequest.getSellerTin()));

        if (declarationRepository.existsBySellerAndPurchaserAndPropertyNumber(seller,purchaser,addDeclarationRequest.getPropertyNumber())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Declaration exists!"));
        }

        // Create new Declaration
        Declaration declaration = new Declaration(addDeclarationRequest.getPropertyNumber(), addDeclarationRequest.getPropertyCategory(),
                addDeclarationRequest.getPropertyDescription(), addDeclarationRequest.getTax());
        declaration.setId((long) 0);
        declaration.setNotary(notary);
        declaration.setSeller(seller);
        declaration.setPurchaser(purchaser);
        declarationRepository.save(declaration);
        return ResponseEntity.ok(new MessageResponse("Declaration added successfully!"));
    }

    @PostMapping("/declaration/complete/{id}")
    @PreAuthorize("hasRole('NOTARY')")
    /* Complete Declaration */
    public ResponseEntity<?> completeDeclaration(@PathVariable Long id, @Valid @RequestBody CompleteDeclarationRequest completeDeclarationRequest, HttpServletRequest request) {
        String userTin = "";
        try {
            userTin = jwtUtils.getUserTinFromJwtToken(request);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Cannot set user authentication: {}" + e));
        }

        if (declarationRepository.existsById(id)) {
            Declaration declaration = declarationRepository.getById(id);

            if (declaration.getStatus()==EStatus.completed) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: The Declaration->" + id+" is already completed."));
            }

            if (declaration.getNotary().getTin().equals(userTin)) {
                String error = "";
                if (!(declaration.getSellerAcceptance())) {
                    error += "No Seller Acceptance. ";
                }
                if (!(declaration.getPurchaserAcceptance())) {
                    error += "No Purchaser Acceptance";
                }
                if (error.isBlank()) {
                    declaration.setContractDetails(completeDeclarationRequest.getContractDetails());
                    declaration.setPaymentMethod(completeDeclarationRequest.getPaymentMethod());
                    declaration.setStatus(EStatus.completed);
                    declarationRepository.save(declaration);
                    return ResponseEntity.ok(new MessageResponse("Declaration completed successfully!"));
                } else {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: " + error));
                }
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: The User is not authorized for Declaration->" + id));
            }
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Declaration->" + id + " NOT Exists"));
        }
    }

    @PostMapping("/declaration/accept/{id}")
    /* Declaration Acceptance */
    public ResponseEntity<?> acceptDeclaration(@PathVariable Long id, HttpServletRequest request) {
        String userTin = "";
        try {
            userTin = jwtUtils.getUserTinFromJwtToken(request);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Cannot set user authentication: {}" + e));
        }

        if (declarationRepository.existsById(id)) {
            Declaration declaration = declarationRepository.getById(id);

            if (declaration.getStatus()==EStatus.completed) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: The Declaration->" +id+" is already completed."));
            }

            if (declaration.getSeller().getTin().equals(userTin)) {
                declaration.setSellerAcceptance(true);
                declarationRepository.save(declaration);
                return ResponseEntity.ok(new MessageResponse("Declaration accepted by the Seller successfully!"));
            } else if (declaration.getPurchaser().getTin().equals(userTin)) {
                declaration.setPurchaserAcceptance(true);
                declarationRepository.save(declaration);
                return ResponseEntity.ok(new MessageResponse("Declaration accepted by the Purchaser successfully!"));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: The User is not authorized to accept Declaration->" + id));
            }
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Declaration->" + id + " NOT Exists"));
        }

    }
}