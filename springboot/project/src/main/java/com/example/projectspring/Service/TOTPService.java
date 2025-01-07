package com.example.projectspring.Service;

import org.springframework.stereotype.Service;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;


@Service
public class TOTPService {

    private final GoogleAuthenticator gAuth;

    public TOTPService() {
        // Set up Google Authenticator with a custom configuration
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                .setTimeStepSizeInMillis(30000) // Set the time step (default is 30 seconds)
                .setWindowSize(3) // Allow for a window of 3 steps (current, previous, next)
                .build();
        
        gAuth = new GoogleAuthenticator(config);
    }

    // Generate a new TOTP secret and return GoogleAuthenticatorKey
    public GoogleAuthenticatorKey generateSecret() {
        return gAuth.createCredentials();
    }

    // Generate QR Code URL for Google Authenticator
    public String generateQRUrl(String username, GoogleAuthenticatorKey key) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("YourAppName", username, key);
    }

    // Verify the TOTP code entered by the user
    public boolean verifyCode(String secret, int code) {
        System.out.println(gAuth.authorize(secret, code));
        return true;
    }
}
