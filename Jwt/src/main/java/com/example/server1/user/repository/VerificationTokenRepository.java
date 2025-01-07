package com.example.server1.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server1.user.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}

