package com.example.projectspring.Model;

public class Dto {
    public Long companyId;
    public String companyName;
    public Dto(Long companyId, String companyName) {
        this.companyId = companyId;
        this.companyName = companyName;
    }
    public Long getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    
}
