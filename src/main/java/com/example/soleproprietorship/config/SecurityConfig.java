package com.example.soleproprietorship.config;

import com.example.soleproprietorship.config.jwt.AuthTokenFilter;
import com.example.soleproprietorship.config.services.MyUserDetailsService;
import com.example.soleproprietorship.user.User;
import com.example.soleproprietorship.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private TotpService totpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("CUSTOMER");
        return roleHierarchy;
    }

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(customAuthenticationProvider(bCryptPasswordEncoder()))
                .cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers("/login", "/login/**").permitAll()
                        .anyRequest().hasRole("CUSTOMER"))
                .formLogin(login -> login.loginPage("/login").permitAll()
                        .authenticationDetailsSource((AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails>) CustomWebAuthenticationDetails::new))
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .clearAuthentication(true)
                        .deleteCookies("SESSIONID"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .maximumSessions(1).maxSessionsPreventsLogin(true));


        return http.build();
    }


    @Bean
    public AuthenticationProvider customAuthenticationProvider(PasswordEncoder passwordEncoder) {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = (String) authentication.getCredentials();
                String totp = ((CustomWebAuthenticationDetails) authentication.getDetails()).getVerificationCode();
                System.out.println(totp);
                User user = userRepository.findByUserName(username).orElse(null);
                if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole().getName().name());
                    List<GrantedAuthority> authorities = List.of(simpleGrantedAuthority);
                    if (user.isUsing2FA()) {
                        if (totpService.verifyCode(user.getSecret2FA(), Integer.parseInt(totp))) {
                            return new UsernamePasswordAuthenticationToken(username, password, authorities);
                        } else {
                            throw new BadCredentialsException("Invalid 2FA code");
                        }
                    } else {
                        return new UsernamePasswordAuthenticationToken(username, password, authorities);
                    }
                } else {
                    throw new BadCredentialsException("Invalid username or password");
                }
            }

            @Override
            public boolean supports(Class<?> aClass) {
                return aClass.equals(UsernamePasswordAuthenticationToken.class);
            }
        };
    }
}


