package com.example.projectspring.Controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projectspring.Model.Document;
import com.example.projectspring.Model.Dto;
import com.example.projectspring.Repository.DocumentRepository;
import com.example.projectspring.Service.DocumentService;

@RestController
@CrossOrigin
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentRepository repo2;

    // Endpoint to upload documents with AES encryption key
    @PostMapping("/upload-documents")
    public ResponseEntity<String> uploadDocuments(@RequestBody Document documentDTO) {
        try {
            
            // Save the encrypted document in the database
            documentService.saveDocuments(
                    documentDTO
            );
            return ResponseEntity.ok("Documents uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload documents.");
        }
    }
    @GetMapping("/documents")
    public List<Document> getcompanyddt()
    {
        return documentService.getcompant();
    }


    // Fetch document details by company ID
    @GetMapping("/{companyId}")
    public ResponseEntity<Document> getDocumentByCompanyId(@PathVariable Long companyId) {
        Optional<Document> document = documentService.findByCompanyId(companyId);
        if (document.isPresent()) {
            Document dto = document.get();
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/payment-status/{companyId}")
public Map<String, String> getPaymentStatus(@PathVariable Long companyId) {
    Map<String, String> response = new HashMap<>();
    try {
        Document document = repo2.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        response.put("paymentStatus", document.getPaymentStatus());
    } catch (Exception e) {
        e.printStackTrace();
        response.put("paymentStatus", "Error retrieving payment status");
    }
    return response;
}

}
