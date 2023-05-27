package com.example.soleproprietorship.config;

import com.example.soleproprietorship.user.User;
import com.example.soleproprietorship.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TotpController {

    @Autowired
    private TotpService totpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationProvider authenticationManager;

    @PatchMapping("/login/2fa/enable")
    public ResponseEntity<?> enable2FA(Principal principal) {
        User user = userRepository.findByUserName("daniel").orElse(null);
        if (user == null)
            return ResponseEntity.ok("Nie ma takiego użytkownika");

        String secret = totpService.generateSecret();
        user.setSecret2FA(secret);
        user.setUsing2FA(true);
        userRepository.save(user);

        String qrUrl = totpService.generateQRUrl(user);
        return ResponseEntity.ok("{\"qrUrl\": \"" + qrUrl + "\"}");
    }

    @GetMapping("/login/2fa/verify")
    public ResponseEntity<?> verify2FA(@RequestParam int totp, Principal principal) {
        User user = userRepository.findByUserName(principal.getName()).orElse(null);
        if (user == null)
            return ResponseEntity.ok("Nie ma takiego użytkownika");

        if (totpService.verifyCode(user.getSecret2FA(), totp)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}


