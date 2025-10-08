package com.productpulse.productpulse.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Optional;

@Entity
@Table(name="team_invites")
public class TeamInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean accepted = false;

    @ManyToOne
    @JoinColumn(name = "invited_by")
    private Users invitedBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Users getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(Users invitedBy) {
        this.invitedBy = invitedBy;
    }
}
