package com.productpulse.productpulse.DTO;

public class InvitePayload {
    private String email;
    private String role;
    private Long companyId;
    
    public InvitePayload(String email, String role, Long companyId) {
        this.email = email;
        this.role = role;
        this.companyId = companyId;
    }
     
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
}
