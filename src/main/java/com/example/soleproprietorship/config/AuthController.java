package com.example.soleproprietorship.config;

import com.example.soleproprietorship.config.jwt.JwtResponse;
import com.example.soleproprietorship.config.jwt.JwtUtils;
import com.example.soleproprietorship.config.request.LoginRequest;
import com.example.soleproprietorship.config.request.RegisterRequest;
import com.example.soleproprietorship.config.services.TotpDto;
import com.example.soleproprietorship.config.services.UserDetailsImpl;
import com.example.soleproprietorship.customer.role.ERole;
import com.example.soleproprietorship.customer.role.Role;
import com.example.soleproprietorship.customer.role.RoleRepository;
import com.example.soleproprietorship.user.User;
import com.example.soleproprietorship.user.UserRepository;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

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

    @Autowired
    private TotpService totpService;

    /**
     * Metoda służąca do logowania użytkownika, jako parametr przyjmuje obiekt LoginRequest
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        User user = userRepository.findByUserName(loginRequest.getUserName()).orElse(null);

        if (user == null)
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Nie znaleziono takiego loginu!"));


        Authentication authentication;
        try {
            if (user.isUsing2FA()) {
                try {
                    Integer.parseInt(loginRequest.getCode());
                } catch (NumberFormatException ex) {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Wprowadzony kod nie jest poprawnie sformatowany"));
                }
                if (totpService.verifyCode(user.getSecret2FA(), Integer.parseInt(loginRequest.getCode()))) {
                    authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
                } else {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Wprowadzony kod jest niepoprawny!"));
                }
            } else {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
            }

        } catch (AuthenticationException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Niepoprawne dane."));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
                .stream()
                .filter(el -> el.equals(user.getRole().getName().name()))
                .findFirst().orElse("");

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                role));
    }

    /**
     * Metoda służy do rejestracji użytkownika w systemie, jako parametr przyjmuje obiekt RegisterRequest
     *
     * @param register
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest register) {

        if (userRepository.existsByUserName(register.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Wybrany login już istnieje!!"));
        }

        if (userRepository.existsByEmail(register.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Wybrany email już istnieje!"));
        }

        // Create new user's account
        Role role = roleRepository.findByName(ERole.CUSTOMER).orElseThrow(() -> new RuntimeException("Nie znaleziono roli"));

        User user = new User(register.getUsername(), encoder.encode(register.getPassword()), register.getEmail(), register.getPhoneNumber(),
                register.getPesel(), register.getUserFirstName(), register.getUserSecondName(), register.getAddress(), role);

        if (register.isUse2FA()) {
            String secret = totpService.generateSecret();
            user.setSecret2FA(secret);
            user.setUsing2FA(true);
            String qrUrl = totpService.generateQRUrl(user);
            userRepository.save(user);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse("Poprawnie dodano użytkownika \n Kod do 2etapowej autoryzacji: \n" +
                            qrUrl));
        }
        userRepository.save(user);

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Poprawnie dodano użytkownika"));
    }

    /**
     * Metoda sprawdza czy logowaniu, czy dany użytkownik posiada 2-stopniową autoryzację
     * @param loginRequest
     * @return
     */
    @PostMapping("/signin/verify2FA")
    public ResponseEntity<?> verify2FA(@RequestBody LoginRequest loginRequest) {

        User user = userRepository.findByUserName(loginRequest.getUserName()).orElse(null);

        if (user == null)
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Nie znaleziono takiego użytkownika"));


        if (!user.isUsing2FA())
            return ResponseEntity
                    .ok()
                    .body(new TotpDto(false));


        return ResponseEntity
                .ok()
                .body(new TotpDto(true, totpService.generateQRUrl(user)));

    }


}
