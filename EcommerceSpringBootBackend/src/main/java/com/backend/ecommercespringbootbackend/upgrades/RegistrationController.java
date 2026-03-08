package com.backend.ecommercespringbootbackend.upgrades;

import com.backend.ecommercespringbootbackend.dao.CustomerRepository;
import com.backend.ecommercespringbootbackend.dao.DivisionRepository;
import com.backend.ecommercespringbootbackend.entities.Customer;
import com.backend.ecommercespringbootbackend.entities.Division;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    private final CustomerRepository customerRepository;
    private final DivisionRepository divisionRepository;

    public RegistrationController(CustomerRepository customerRepository,
                                  DivisionRepository divisionRepository) {
        this.customerRepository = customerRepository;
        this.divisionRepository = divisionRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CustomerRegistrationDao req) {

        //Implements polymorphism
        Hash hash = new LogPasswordHash();

        Customer customer = new Customer();
        customer.setFirstName(req.firstName);
        customer.setLastName(req.lastName);
        customer.setEmail(req.email);
        customer.setPassword(hash.hashPassword(req.password));
        customer.setAddress(req.address);
        customer.setPostal_code(req.postal_code);
        customer.setPhone(req.phone);
        customer.setCountry(req.country);

        //Extract ID from URL
        Long divisionId = Long.parseLong(req.division.substring(req.division.lastIndexOf("/") + 1));

        //Load entities
        Division division = divisionRepository.findById(divisionId).orElseThrow();

        //Assign to real JPA field
        customer.setDivision(division);

        //Prevent duplicate values error
        try {
            Customer saved = customerRepository.save(customer);
            return ResponseEntity.ok(Map.of("Response","Customer saved"));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Email already exists"));
        }
    }
}
