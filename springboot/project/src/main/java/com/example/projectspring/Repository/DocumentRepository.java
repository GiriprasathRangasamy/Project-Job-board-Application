package com.example.projectspring.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.projectspring.Model.Document;
import com.example.projectspring.Model.Dto;

import jakarta.transaction.Transactional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
   
@Modifying
    @Transactional
    @Query("UPDATE Document d SET d.paymentStatus = 'paid' WHERE d.companyId = :companyId")
    void updatePaymentStatusToPaid(Long companyId);
}

