package com.example.server1.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.server1.config.JwtService;
import com.example.server1.user.User;
import com.example.server1.user.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

         @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private EmailService emailService;
    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest registerRequest) {
        User user = User.builder()
        .email(registerRequest.getEmail())
        .name(registerRequest.getName())
        .mobile(registerRequest.getMobile())
        .password(passwordEncoder.encode(registerRequest.getPassword()))
        .company(registerRequest.getCompany())
        .isVerified(false) // Setting the default value
        .role(registerRequest.getRole())
        .build();
            userRepo.save(user);

            // Generate a verification token and save it in the database
            String verificationToken = verificationTokenService.generateToken();
            verificationTokenService.saveToken(verificationToken, registerRequest.getEmail());

            // Send verification email
            emailService.sendVerificationEmail(registerRequest.getEmail(), verificationToken);
        return "Registration successful. Please check your email to verify your account.";
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            // Check if the user exists
            var user = userRepo.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                    if (!user.isVerified()) {
                        return AuthenticationResponse.builder()
                                .alert("Email not verified. Please verify your email before logging in.")
                                .build();
                    }
            
            // If the user exists, validate the credentials and authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // If authentication is successful, generate the JWT token
            String jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .alert("Login successful")
                    .build();

        } catch (UsernameNotFoundException e) {
            return AuthenticationResponse.builder()
                    .alert("User not found")
                    .build();
        } catch (BadCredentialsException e) {
            return AuthenticationResponse.builder()
                    .alert("Incorrect password")
                    .build();
        } catch (Exception e) {
                System.out.println(e);
            return AuthenticationResponse.builder()
            .alert("Authentication failed")
            .build();
        }
    }
}
