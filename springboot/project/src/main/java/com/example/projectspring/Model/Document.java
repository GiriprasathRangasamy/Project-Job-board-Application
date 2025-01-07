package com.example.projectspring.Model;


import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "document")
public class Document {
    @Id
    private Long companyId;
    private String companyName;
    private String address;
    private int numEmployees;
    private String gstNumber;
    private String paymentStatus;
    @PrePersist
    protected void onCreate() {
        if (this.paymentStatus == null) {
            this.paymentStatus = "not paid";
        }
    }
    @Lob
    @Column(name = "certificate_of_incorporation", columnDefinition = "LONGTEXT")
    private String certificateOfIncorporation;

    @Lob
    @Column(name = "gst_registration_certificate", columnDefinition = "LONGTEXT")
    private String gstRegistrationCertificate;

    @Lob
    @Column(name = "registered_office_proof", columnDefinition = "LONGTEXT")
    private String registeredOfficeProof;

    @Lob
    @Column(name = "shop_and_establishment_license", columnDefinition = "LONGTEXT")
    private String shopAndEstablishmentLicense;

   

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    // Getters and Setters
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumEmployees() {
        return numEmployees;
    }

    public void setNumEmployees(int numEmployees) {
        this.numEmployees = numEmployees;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getCertificateOfIncorporation() {
        return certificateOfIncorporation;
    }

    public void setCertificateOfIncorporation(String certificateOfIncorporation) {
        this.certificateOfIncorporation = certificateOfIncorporation;
    }

    public String getGstRegistrationCertificate() {
        return gstRegistrationCertificate;
    }

    public void setGstRegistrationCertificate(String gstRegistrationCertificate) {
        this.gstRegistrationCertificate = gstRegistrationCertificate;
    }

    public String getRegisteredOfficeProof() {
        return registeredOfficeProof;
    }

    public void setRegisteredOfficeProof(String registeredOfficeProof) {
        this.registeredOfficeProof = registeredOfficeProof;
    }

    public String getShopAndEstablishmentLicense() {
        return shopAndEstablishmentLicense;
    }

    public void setShopAndEstablishmentLicense(String shopAndEstablishmentLicense) {
        this.shopAndEstablishmentLicense = shopAndEstablishmentLicense;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

   
    
}
