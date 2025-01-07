package com.example.dbagent;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class PaymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Constructors, getters, and setters
    public PaymentRecord() {}

    public PaymentRecord(String paymentId, String orderId, String status) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}

