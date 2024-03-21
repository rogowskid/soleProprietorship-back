package com.example.soleproprietorship.config;

import com.example.soleproprietorship.config.jwt.AuthEntryPointJwt;
import com.example.soleproprietorship.config.jwt.AuthTokenFilter;
import com.example.soleproprietorship.config.services.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
/**
 * Klasa służąca do konfiguracji bezpieczeństwa aplikacji
 */
public class SecurityConfig implements WebMvcConfigurer {

    private final MyUserDetailsService myUserDetailsService;
    private static final String LOGOUT_URL = "/logout";

    public SecurityConfig(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(myUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    /**
     * Metoda służy do konfiguracji mapowań użytkowników, dodatkowo wybierana jest tutaj pula mapowań, w których
     * użytkownik nie potrzebuje autoryzacji, natomiast każde inne mapowanie wymaga autoryzacji
     */
    public SecurityFilterChain filterChain(HttpSecurity http, AuthEntryPointJwt unauthorizedHandler, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http.csrf()
                .ignoringAntMatchers(LOGOUT_URL)
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http
                .cors().configurationSource(corsConfigurationSource)
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/auth/**", LOGOUT_URL).permitAll()
                .antMatchers("/api/**", LOGOUT_URL).authenticated()
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/swagger**").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/swagger**/**").permitAll()
                .anyRequest().authenticated()
                .and()

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .logout()
                .logoutUrl(LOGOUT_URL)
                .logoutSuccessUrl("http://localhost:3000")
                .permitAll()
                .deleteCookies("SESSION")
                .invalidateHttpSession(true)
                .clearAuthentication(true);


        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}


