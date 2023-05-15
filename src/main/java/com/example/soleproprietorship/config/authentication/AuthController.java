package com.example.soleproprietorship.config.authentication;

import com.example.soleproprietorship.config.authentication.request.LoginRequest;
import com.example.soleproprietorship.config.authentication.request.SignUpRequest;
import com.example.soleproprietorship.config.authentication.response.JwtResponse;
import com.example.soleproprietorship.config.authentication.response.MessageResponse;
import com.example.soleproprietorship.config.jwt.JwtUtils;
import com.example.soleproprietorship.config.services.UserDetailsImpl;
import com.example.soleproprietorship.customer.role.ERole;
import com.example.soleproprietorship.customer.role.Role;
import com.example.soleproprietorship.customer.role.RoleRepository;
import com.example.soleproprietorship.user.User;
import com.example.soleproprietorship.user.UserRepository;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController implements HasAuthenticationModel{

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        loginRequest = encodeLoginRequest(loginRequest);

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Niepoprawne dane."));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        signUpRequest = encodeRegisterRequest(signUpRequest);

        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Wybrany login już istnieje!!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Wybrany email już istnieje!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail(),
                signUpRequest.getPesel(), signUpRequest.getUserFirstName(), signUpRequest.getUserSecondName());

        String strRoles = signUpRequest.getRole();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            user.setRole(userRole);
        } else {
            switch (strRoles) {
                case "moderator" -> {
                    Role adminRole = roleRepository.findByName(ERole.MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    user.setRole(adminRole);
                }

                default -> {
                    Role userRole = roleRepository.findByName(ERole.CUSTOMER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    user.setRole(userRole);
                }
            }

        }


        userRepository.save(user);

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Poprawnie dodano użytkownika"));
    }

    private LoginRequest encodeLoginRequest(LoginRequest entity){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName(Encode.forHtml(entity.getUserName()));
        loginRequest.setPassword(Encode.forHtml(entity.getPassword()));

        return loginRequest;
    }

    private SignUpRequest encodeRegisterRequest(SignUpRequest entity){
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setPassword(Encode.forHtml(entity.getPassword()));
        signUpRequest.setPesel(Encode.forHtml(entity.getPesel()));
        signUpRequest.setUsername(Encode.forHtml(entity.getUsername()));
        signUpRequest.setRole(Encode.forHtml(entity.getRole()));
        signUpRequest.setUserFirstName(Encode.forHtml(entity.getUserFirstName()));
        signUpRequest.setUserSecondName(Encode.forHtml(entity.getUserSecondName()));
        signUpRequest.setEmail(parseEmail(entity.getEmail()));

        return signUpRequest;
    }
}
