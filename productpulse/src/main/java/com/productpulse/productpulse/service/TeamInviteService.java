package com.productpulse.productpulse.service;

import com.productpulse.productpulse.model.Company;
import com.productpulse.productpulse.model.TeamInvite;
import com.productpulse.productpulse.model.Users;
import com.productpulse.productpulse.repo.CompanyRepo;
import com.productpulse.productpulse.repo.TeamInviteRepo;
import com.productpulse.productpulse.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Optional;

@Service
public class TeamInviteService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private TeamInviteRepo teamInviteRepo;

    public String sendInvitation(String email, String role, String admin, Long companyId){
        TeamInvite teamInvite= new TeamInvite();
        Users adminUser = userRepo.findByEmail(admin)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not Found!"));
        teamInvite.setInvitedBy(adminUser);

        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));
        teamInvite.setCompany(company);

        teamInvite.setEmail(email);
        teamInvite.setRole(role);
        teamInvite.setAccepted(false);
        teamInvite.setExpiresAt(Instant.now().plus(24, ChronoUnit.HOURS));

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", teamInvite.getRole());
        claims.put("companyId", teamInvite.getCompany().getId());
        claims.put("invitedBy", adminUser.getId());

        String inviteToken = jwtService.generateInviteToken(email, claims);
        teamInvite.setToken(inviteToken);

        TeamInvite savedInvite = teamInviteRepo.save(teamInvite);

        return "http://localhost:5173/company/"+companyId+"/invites/accept?token=" + inviteToken;

    }

    public boolean sendInviteEmail(String adminEmail, String toEmail, String inviteLink, Company company){
        System.out.println("Sending Invite to: " + toEmail);
        System.out.println("Invite Link is: " + inviteLink);
        System.out.println("Admin of Company: " + adminEmail);
        System.out.println("Company is: " + company.getName());
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setCc(adminEmail);
            simpleMailMessage.setSubject(company.getName()+" Invited You to Join their Team!");
            simpleMailMessage.setText("Hey! You’ve been invited to join "+company.getName() +" as a team member.\n\n" +
                    "Click the link below to accept the invitation and register:\n" +
                    inviteLink + "\n\n" +
                    "This link expires in 24 hours.");
            javaMailSender.send(simpleMailMessage);
            System.out.println("Invitation sent Successfully!");
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
