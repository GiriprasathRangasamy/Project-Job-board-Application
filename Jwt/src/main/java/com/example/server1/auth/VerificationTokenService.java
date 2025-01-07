package com.example.server1.auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server1.user.VerificationToken;
import com.example.server1.user.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public void saveToken(String token, String email) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setEmail(email);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // Token valid for 24 hours
        verificationTokenRepository.save(verificationToken);
    }

    public Optional<VerificationToken> getToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public void deleteToken(VerificationToken token) {
        verificationTokenRepository.delete(token);
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}

