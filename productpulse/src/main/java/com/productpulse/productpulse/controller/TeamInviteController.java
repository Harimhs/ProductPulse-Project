package com.productpulse.productpulse.controller;

import com.productpulse.productpulse.DTO.InviteRequestWrapper;
import com.productpulse.productpulse.DTO.PartialUser;
import com.productpulse.productpulse.model.Company;
import com.productpulse.productpulse.DTO.InviteRequest;
import com.productpulse.productpulse.repo.CompanyRepo;
import com.productpulse.productpulse.service.JWTService;
import com.productpulse.productpulse.service.TeamInviteService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.hibernate.boot.model.internal.CreateKeySecondPass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class TeamInviteController {

    @Autowired
    private TeamInviteService teamInviteService;

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/api/company/{companyId}/invites")
    public ResponseEntity<?> SendTeamInvite(@PathVariable Long companyId, @Valid @RequestBody InviteRequestWrapper wrapper){
        System.out.println("Hit TeamInviteController! companyId=" + companyId + ", inviteList=" + wrapper.getInviteList());
        Authentication adminAuth= SecurityContextHolder.getContext().getAuthentication();
        String adminEmail = adminAuth.getName();

        List<InviteRequest> invites= wrapper.getInviteList();
        List<Map<String, String>> success = new ArrayList<>();
        List<Map<String, String>> failed = new ArrayList<>();

        for(InviteRequest invite: invites){
            try{
                if(processInvite(invite, adminEmail, companyId)){
                    success.add(Map.of("email", invite.getEmail(), "role", invite.getRole()));
                }
            } catch(Exception e){
                failed.add(Map.of("email", invite.getEmail(), "reason", e.getMessage()));
            }
        }
        System.out.println("Invite results - Success: " + success + ", Failed: " + failed);

        if(success.isEmpty() && !failed.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "All invites failed",
                    "success", success,
                    "failed", failed
            ));

        return ResponseEntity.ok(Map.of(
                "message", "Team invites processed",
                "success", success,
                "failed", failed
        ));
    }

    public boolean processInvite(InviteRequest inviteRequest, String adminEmail, Long companyId) throws ExecutionException, InterruptedException {
        System.out.println("Hit process invite method! User: "+ inviteRequest.getEmail()+", Role: "+inviteRequest.getRole()+ " Admin: "+adminEmail+ ", companyId: "+companyId);
        CompletableFuture<String> futureLink = teamInviteService.sendInvitation(
                inviteRequest.getEmail(),
                inviteRequest.getRole(),
                adminEmail,
                companyId
        );
        String inviteLink = futureLink.get();

        Company company= companyRepo.findById(companyId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        if(teamInviteService.sendInviteEmail(adminEmail,
                inviteRequest.getEmail(),
                inviteLink,
                company).get()){
            return true;
        }
        return false;
    }

    @PostMapping("/api/company/{companyId}/invites/accept")
    public ResponseEntity<?> acceptInvite(@RequestParam String token) {

        Optional<Claims> claimsOpt = jwtService.processingInviteAccept(token);

        if (claimsOpt.isPresent()) {
            Claims claims = claimsOpt.get();
            Long companyId = claims.get("companyId", Long.class);
            String role = claims.get("role", String.class);
            String email = claims.getSubject();

            return ResponseEntity.ok(new PartialUser(email, companyId, role, true));
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }

    }

}
