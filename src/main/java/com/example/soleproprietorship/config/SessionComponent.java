package com.example.soleproprietorship.config;

import com.example.soleproprietorship.config.services.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionComponent {
    public String getSessionUserLogin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl)
            return ((UserDetailsImpl) principal).getUsername();
        else
            return principal.toString();
    }
}
