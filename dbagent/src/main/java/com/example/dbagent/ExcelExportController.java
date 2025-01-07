package com.example.dbagent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/export")
public class ExcelExportController {

    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private Paymentrepo repository;  // Use your actual repository class

    @GetMapping("/excel")
    public ResponseEntity<byte[]> downloadExcel() throws IOException {
        List<PaymentRecord> data = repository.findAll();  // Now returning List<PaymentRecord>
        ByteArrayOutputStream excelFile = excelExportService.exportToExcel(data);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelFile.toByteArray());
    }
}
