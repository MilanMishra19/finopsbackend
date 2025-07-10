package com.finops.finopsbk.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finops.finopsbk.entity.Analyst;
import com.finops.finopsbk.repository.AnalystRepository;
import com.finops.finopsbk.service.AnalystService;
import com.finops.finopsbk.dto.RegisterRequest;

@RestController
@RequestMapping("/api/analysts")
public class AnalystController {

    private final PasswordEncoder passwordEncoder;
    @Autowired
    private AnalystService analystService;
    @Autowired
    private AnalystRepository analystRepository;

    AnalystController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping
    public ResponseEntity<Analyst> createAnalyst(@RequestBody RegisterRequest registerRequest){ // Use the DTO here
        Analyst createdAnalyst = analystService.createAnalyst(registerRequest);
        return ResponseEntity.ok(createdAnalyst);
    }
    @GetMapping("/me")
    public ResponseEntity<?>getCurrentAnalyst(Principal principal){
        if(principal == null){
            return ResponseEntity.status(401).body("Not logged in");
        }
        String email = principal.getName();
        Optional<Analyst> analystOpt = analystService.getAnalystByEmail(email);
        if(analystOpt.isEmpty()){
            return ResponseEntity.status(404).body("Analyst not found");
        }
        Analyst analyst = analystOpt.get();
        return ResponseEntity.ok(Map.of(
            "id", analyst.getAnalystId(),
            "name", analyst.getName(),
            "email", analyst.getEmail()
        ));
    }
    @GetMapping("/{analystId}")
    public ResponseEntity<Analyst> getAnalystById(@PathVariable UUID analystId){
        return analystService.getAnalystById(analystId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Analyst>> getAllAnalyst(){
        List<Analyst> analysts = analystService.getAllAnalysts();
        return ResponseEntity.ok(analysts);
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateAnalyst(@RequestBody Map<String, String> updates, Principal principal) {
    Optional<Analyst> analystOpt = analystService.getAnalystByEmail(principal.getName());
    if (analystOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");

    Analyst analyst = analystOpt.get();
    analyst.setName(updates.get("name"));
    analyst.setEmail(updates.get("email"));
    analyst.setPhoneNumber(updates.get("phoneNumber"));
    String current = updates.get("currentPassword");
    String newPwd = updates.get("newPassword");

    if (current != null && newPwd != null && !newPwd.isBlank()) {
        if (!passwordEncoder.matches(current, analyst.getPasswordHash())) {
            return ResponseEntity.status(403).body("Incorrect current password");
        }
        analyst.setPasswordHash(passwordEncoder.encode(newPwd));
    }

    analystRepository.save(analyst);
    return ResponseEntity.ok("Updated");
}

}