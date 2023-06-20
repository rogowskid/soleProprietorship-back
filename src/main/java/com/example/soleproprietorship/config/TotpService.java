package com.example.soleproprietorship.config;

import com.example.soleproprietorship.user.User;
import org.apache.commons.codec.binary.Base32;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Service
public class TotpService {


    private static final String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    /**
     * Metoda służy do generowania klucza do 2FA
     * @return
     */
    public String generateSecret() {
        Base32 base32 = new Base32();
        byte[] bytes = new byte[10];
        new SecureRandom().nextBytes(bytes);
        return base32.encodeAsString(bytes);
    }

    /**
     * Metoda służy do generowania kodu QR dla użytkownika, który skorzysta z podwójnego zabezpieczenia
     * @param user
     * @return
     */
    public String generateQRUrl(User user) {
        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", "SpringSecurity",
                user.getUserName(), user.getSecret2FA(), "SpringSecurity"), StandardCharsets.UTF_8);
    }

    /**
     * Metoda służy do weryfikacji kodu od użytkownika
     * @param secret
     * @param code
     * @return
     */
    public boolean verifyCode(String secret, int code) {
        Totp totp = new Totp(secret);
        return totp.verify(String.valueOf(code));
    }

}
