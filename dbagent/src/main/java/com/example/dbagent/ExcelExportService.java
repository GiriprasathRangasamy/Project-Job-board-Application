package com.example.dbagent;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ExcelExportService {

    public ByteArrayOutputStream exportToExcel(List<PaymentRecord> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Payment Records");

            // Add a header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("companyID");
            header.createCell(1).setCellValue("payment id");
            header.createCell(2).setCellValue("payment date");
            // Add more headers as needed

            // Create a cell style for date formatting
            CellStyle dateCellStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));

            // Add data rows
            for (int i = 0; i < data.size(); i++) {
                PaymentRecord record = data.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(record.getCompanyId()); // Adjust according to your PaymentRecord fields
                row.createCell(1).setCellValue(record.getPaymentId());
                
                // Create a cell for the payment date and apply the date format
                Cell dateCell = row.createCell(2);
                dateCell.setCellValue(record.getCreatedAt()); // Assuming this returns a Date object
                dateCell.setCellStyle(dateCellStyle);
            }

            // Write the data to a ByteArrayOutputStream
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out;
        }
    }
}
