package gr.hua.dit.distributed.project_46.controller;

import gr.hua.dit.distributed.project_46.config.JwtUtils;
import gr.hua.dit.distributed.project_46.dao.DeclarationDAO;
import gr.hua.dit.distributed.project_46.dao.TeacherDAO;
import gr.hua.dit.distributed.project_46.entity.Course;
import gr.hua.dit.distributed.project_46.entity.Declaration;

import gr.hua.dit.distributed.project_46.payload.request.AddDeclarationRequest;
import gr.hua.dit.distributed.project_46.payload.response.MessageResponse;
import gr.hua.dit.distributed.project_46.repository.PersonRepository;
import gr.hua.dit.distributed.project_46.repository.UserRepository;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
    JwtUtils jwtUtils;

    @Autowired
    private DeclarationDAO declarationDAO;

    @PostMapping("/declaration")
    @PreAuthorize("hasRole('NOTARY')")
    public ResponseEntity<?> addDeclaration(@Valid @RequestBody AddDeclarationRequest addDeclarationRequest, HttpServletRequest request) {
        String userTin = "";
        try {
            userTin = jwtUtils.getUserTinFromJwtToken(request);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Cannot set user authentication: {}" + e));
        }

        if (!(userTin.equals(addDeclarationRequest.getNotaryTin()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Taxpayer Identification Number of Notary must be the user's!"));
        }

        if (addDeclarationRequest.getPurchaserTin().equals(addDeclarationRequest.getSellerTin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Purchaser and Seller can not be the same!"));
        }

        if (!(personRepository.existsByTin(addDeclarationRequest.getNotaryTin()))){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Notary's Data Not Found with Taxpayer Identification Number: "+addDeclarationRequest.getNotaryTin()));
        }

        if (!(personRepository.existsByTin(addDeclarationRequest.getPurchaserTin()))){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Purchaser's Data Not Found with Taxpayer Identification Number: "+addDeclarationRequest.getPurchaserTin()));
        }

        if (!(personRepository.existsByTin(addDeclarationRequest.getSellerTin()))){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Seller's Data Not Found with Taxpayer Identification Number: "+addDeclarationRequest.getSellerTin()));
        }

        // Create new Declaration
        Declaration declaration = new Declaration(addDeclarationRequest.getNotaryTin(), addDeclarationRequest.getPropertyNumber(), addDeclarationRequest.getPropertyCategory(),
                addDeclarationRequest.getPropertyDescription(), addDeclarationRequest.getSellerTin(), addDeclarationRequest.getPurchaserTin(), addDeclarationRequest.getTax());
        declaration.setId((long)0);
        declarationDAO.save(declaration);

        return ResponseEntity.ok(new MessageResponse("Declaration added successfully!"));
    }

}
