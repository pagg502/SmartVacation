package com.backend.ecommercespringbootbackend.upgrades;


import com.backend.ecommercespringbootbackend.dao.ExcursionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SearchAndReportsController {
    private final LoginCheckSessionDTORepository loginCheckSessionDTORepository;
    private final ExcursionRepository excursionRepository;

    public SearchAndReportsController (LoginCheckSessionDTORepository loginCheckSessionDTORepository, ExcursionRepository excursionRepository) {
        this.loginCheckSessionDTORepository = loginCheckSessionDTORepository;
        this.excursionRepository = excursionRepository;
    }

    @PostMapping("/searchByFirstName")
    public ResponseEntity<?> searchFirstName(@RequestBody LoginCheckSessionDTO req) {
        String firstName = req.getFirstName();

        //Find customers with the same firstName. I use the LoginCheckSessionDTO so its already created and does not contain password in it.
        List<LoginCheckSessionDTO> customers = loginCheckSessionDTORepository.findByFirstName(firstName);

        //Count them to include in the report
        int sameNameCount = loginCheckSessionDTORepository.countByFirstName(firstName);

        //Build response
        Map<String, Object> response = new HashMap<>();
        response.put("reportName", "Find users matching name: " + firstName);
        response.put("date", LocalDateTime.now());
        response.put("firstName", firstName);
        response.put("customers", customers);
        response.put("sameNameCount", sameNameCount);
        response.put("summary", "The report shows we have " + sameNameCount + " users named " + firstName + " in our database.");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/searchByPhoneAreaCode")
    public ResponseEntity<?> searchPhoneAreaCode(@RequestBody LoginCheckSessionDTO req) {
        String phoneAreaCode = req.getPhone();

        //Find customers that lives in the same Phone Area Code
        List<LoginCheckSessionDTO> customers = loginCheckSessionDTORepository.findByPhoneIsStartingWith(phoneAreaCode);

        //Count them to include in the report
        int samePhoneAreCodeCount = loginCheckSessionDTORepository.countByPhoneIsStartingWith(phoneAreaCode);

        //Build response
        Map<String, Object> response = new HashMap<>();
        response.put("reportName", "Find users living in the same Phone Area Code: " + phoneAreaCode);
        response.put("date", LocalDateTime.now());
        response.put("phoneAreaCode", phoneAreaCode);
        response.put("customers", customers);
        response.put("samePhoneAreaCodeCount", samePhoneAreCodeCount);
        response.put("summary", "The report shows we have " + samePhoneAreCodeCount + " users in our database living in the " + phoneAreaCode + " area code region.");

        return ResponseEntity.ok(response);
    }
}
