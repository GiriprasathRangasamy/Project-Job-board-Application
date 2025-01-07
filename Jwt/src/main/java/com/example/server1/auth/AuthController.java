package com.example.server1.auth;

import org.springframework.web.bind.annotation.RestController;

import com.example.server1.user.User;
import com.example.server1.user.VerificationToken;
import com.example.server1.user.repository.UserRepo;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/security")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @Autowired
    UserRepo userRepository;
    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private EmailService emailService;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        Optional<User> existingUsers = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUsers.isEmpty()) {
            // Register the user but set `isVerified` to false
            String authResponse=authService.register(registerRequest);
           return ResponseEntity.ok(authResponse);
        }
        return ResponseEntity.status(409).body("Email already exists.");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        Optional<VerificationToken> verificationToken = verificationTokenService.getToken(token);
        if (verificationToken.isPresent() && verificationToken.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            // Mark the user as verified
            User user = userRepository.findByEmail(verificationToken.get().getEmail()).get();
            user.setVerified(true);
            userRepository.save(user);
            verificationTokenService.deleteToken(verificationToken.get());
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.status(400).body("Invalid or expired verification token.");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationEmail(@RequestParam String email) {
        if (!isValidEmail(email)) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }
    
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.isVerified()) {
                // Generate and save the verification token
                String verificationToken = verificationTokenService.generateToken();
                verificationTokenService.saveToken(verificationToken, email);
    
                // Send verification email
                emailService.sendVerificationEmail(email, verificationToken);
                return ResponseEntity.ok("Verification email resent successfully.");
            } else {
                return ResponseEntity.badRequest().body("User already verified.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }
    
    // Simple email validation
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
       return ResponseEntity.ok(authService.authenticate(request));
    }
}