package com.example.projectspring.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projectspring.Model.docverifyuser;
import com.example.projectspring.Service.TOTPService;
import com.example.projectspring.Service.UserService;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api1")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private TOTPService totpService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> registerRequest) {
        String username = registerRequest.get("username");
        String password = registerRequest.get("password");
    
        docverifyuser user = new docverifyuser();
        user.setUsername(username);
        user.setPassword(password);
    
        // Generate secret on the backend
        GoogleAuthenticatorKey key = totpService.generateSecret();
        String secret = key.getKey();
        user.setSecret(secret);
        user.setEnabled(false); // initially disabled
    
        userService.registerNewUser(user);
    
        // Generate QR URL
        String qrUrl = totpService.generateQRUrl(username, key);
    
        Map<String, String> response = new HashMap<>();
        response.put("qrUrl", qrUrl);
        response.put("secret", secret); // Return the secret for user display
        return ResponseEntity.ok(response);
    }
    

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        int otpCode = Integer.parseInt(loginRequest.get("otp"));

        docverifyuser user = userService.findByUsername(username);
        if (user != null && userService.verifyPassword(user, password)) {
            System.out.println(otpCode);
            if(totpService.verifyCode(user.getSecret(), otpCode))
            {
            // Authenticate user and generate JWT token or session here
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);}
            else{

                return ResponseEntity.status(401).body(Map.of("message", "OTP"));
            }
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }
    }
}
