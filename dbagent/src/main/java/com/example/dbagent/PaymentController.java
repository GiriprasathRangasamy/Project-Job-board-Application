package com.example.dbagent;


import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private RazorpayClient client;

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
    @GetMapping("/verify-payment/{paymentId}")
public Map<String, Object> verifyPayment(@PathVariable String paymentId) {
    Map<String, Object> response = new HashMap<>();
    try {
        Payment payment = client.Payments.fetch(paymentId);
        response.put("status", payment.get("status"));
    } catch (Exception e) {
        e.printStackTrace();
    }
    return response;
}

}

