package com.productpulse.productpulse.controller;

import com.productpulse.productpulse.model.Company;
import com.productpulse.productpulse.DTO.CompanyRegistrationResponse;
import com.productpulse.productpulse.model.Industry;
import com.productpulse.productpulse.payload.ApiResponse;
import com.productpulse.productpulse.repo.CompanyRepo;
import com.productpulse.productpulse.service.CompanyAlreadyExistsException;
import com.productpulse.productpulse.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class CompanyController {
    @Autowired
    private CompanyService service;

    @Autowired
    private CompanyRepo repo;

    @PostMapping("register/company")
    public ResponseEntity<?> registerCompany(@Valid @RequestBody Company company) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        try {
            CompanyRegistrationResponse response = service.registerCompany(company, email);
            return ResponseEntity.ok(Map.of(
                    "data", Map.of("companyId", company.getId())
            ));

        } catch (CompanyAlreadyExistsException e) {
            return ResponseEntity.status(409)
                    .body(new ApiResponse<>("Failed to save company: " + e.getMessage(), null));
        }
    }

    @RestController
    public class EnumController {
        @GetMapping("/api/enums/industries")
        public List<Map<String, String>> getIndustries() {
            return Arrays.stream(Industry.values())
                    .map(ind -> Map.of("value", ind.name(), "label", ind.getLabel()))
                    .toList();
        }
    }
}
