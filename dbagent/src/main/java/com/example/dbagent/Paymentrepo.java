package com.example.dbagent;

import org.springframework.data.jpa.repository.JpaRepository;


public interface Paymentrepo extends JpaRepository<PaymentRecord,Integer> {

    
}
