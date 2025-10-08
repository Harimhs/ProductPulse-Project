package com.productpulse.productpulse.service;

import com.productpulse.productpulse.model.Company;
import com.productpulse.productpulse.DTO.CompanyRegistrationResponse;
import com.productpulse.productpulse.model.UserPrincipal;
import com.productpulse.productpulse.model.Users;
import com.productpulse.productpulse.repo.CompanyRepo;
import com.productpulse.productpulse.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    public CompanyRegistrationResponse registerCompany(Company company, String email) throws CompanyExistsException {
        Optional<Company> optionalCompany = companyRepo.findByName(company.getName());
        if (optionalCompany.isPresent()) {
            throw new CompanyExistsException("The company name is already registered.");
        }

        // 1. Save the new company
        Company savedCompany = companyRepo.save(company);

        // 2. Find and update the user
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not Found!"));

        user.setRole("ADMIN");
        user.setCompanyId(savedCompany.getId());
        userRepo.save(user);

        // 3. Refresh authentication with updated user details
        UserPrincipal updatedPrincipal = new UserPrincipal(user);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedPrincipal,
                null,
                updatedPrincipal.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        // 4. Generate a new JWT using email (not toString)
        String newToken = jwtService.generateToken(user.getEmail());

        // 5. Return response with fresh token
        return new CompanyRegistrationResponse(
                "Company registered successfully!",
                savedCompany,
                newToken
        );
    }

}

