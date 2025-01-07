package com.example.projectspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectspring.Model.PaymentRecord;

public interface Paymentrepo extends JpaRepository<PaymentRecord,Integer> {

    
}
