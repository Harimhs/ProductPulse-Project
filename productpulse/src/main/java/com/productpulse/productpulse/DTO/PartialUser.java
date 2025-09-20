package com.productpulse.productpulse.DTO;

public class PartialUser {

    private String email;
    private Long companyId;
    private String role;
    private boolean invited;

    public PartialUser(String email, Long companyId, String role, boolean invited) {
        this.email = email;
        this.companyId = companyId;
        this.role = role;
        this.invited = invited;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isInvited() {
        return invited;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }
}
