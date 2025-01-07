package com.example.projectspring.Controller;

import com.example.projectspring.Model.PaymentRecord;
import com.example.projectspring.Repository.DocumentRepository;
import com.example.projectspring.Repository.Paymentrepo;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private RazorpayClient client;

    @Autowired
    private Paymentrepo paymentRepository;

    public PaymentController() throws Exception {
        client = new RazorpayClient("rzp_test_w52Rce8bQSsnwr", "DxOE9UMV3xdnYgjCalnFd7qk");
    }

    @PostMapping("/create-order")
    public Map<String, String> createOrder() {
        Map<String, String> response = new HashMap<>();
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", 300 * 100); // Amount in paise (300 INR)
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "receipt#1");
            orderRequest.put("payment_capture", true);

            Order order = client.Orders.create(orderRequest);

            response.put("orderId", order.get("id"));
            response.put("status", "created");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    @Autowired
    DocumentRepository repo1;
    @GetMapping("/verify-payment/{paymentId}")
    public Map<String, Object> verifyPayment(
            @PathVariable String paymentId,
            @RequestParam String companyId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Payment payment = client.Payments.fetch(paymentId);
            String status = payment.get("status");
            String orderId = payment.get("order_id");

            // Save payment with company ID in the database
            PaymentRecord paymentRecord = new PaymentRecord(paymentId, orderId, status);
            paymentRecord.setCompanyId(companyId);  // Assuming your Payment entity has this field
            paymentRepository.save(paymentRecord);
            long cid=Long.parseLong(companyId);
            if(status=="captured")
            repo1.updatePaymentStatusToPaid(cid);
            response.put("status", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
