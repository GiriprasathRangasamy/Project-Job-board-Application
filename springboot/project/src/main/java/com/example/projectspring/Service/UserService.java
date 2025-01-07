package com.example.projectspring.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.projectspring.Model.docverifyuser;
import com.example.projectspring.Repository.DocverifyUserRepositor;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    // @Autowired
    // private PasswordEncoder passwordEncoder;
    @Autowired
    private DocverifyUserRepositor userRepository;

    @Autowired
    private TOTPService totpService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void registerNewUser(docverifyuser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        GoogleAuthenticatorKey key = totpService.generateSecret();
        user.setSecret(key.getKey());
        user.setEnabled(true); // initially disabled
        userRepository.save(user);
    }

    public docverifyuser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean verifyPassword(docverifyuser user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
