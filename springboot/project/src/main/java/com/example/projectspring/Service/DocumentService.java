package com.example.projectspring.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectspring.Model.Document;
import com.example.projectspring.Model.Dto;
import com.example.projectspring.Repository.DocumentRepository;


@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;


    public void saveDocuments(Document Dto1) {

        Document document = new Document();
        document.setCompanyId(Dto1.getCompanyId());
        document.setCompanyName(Dto1.getCompanyName());
        document.setAddress(Dto1.getAddress());
        document.setNumEmployees(Dto1.getNumEmployees());
        document.setGstNumber(Dto1.getGstNumber());
        document.setCertificateOfIncorporation(Dto1.getCertificateOfIncorporation());
        document.setGstRegistrationCertificate(Dto1.getGstRegistrationCertificate());
        document.setRegisteredOfficeProof(Dto1.getRegisteredOfficeProof());
        document.setShopAndEstablishmentLicense(Dto1.getShopAndEstablishmentLicense());
        

        documentRepository.save(document);
    }

    public Optional<Document> findByCompanyId(Long companyId) {
        return documentRepository.findById(companyId);
    }
    public List<Document> getcompant()
    {
        return documentRepository.findAll();
    }

}