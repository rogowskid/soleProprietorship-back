package com.example.soleproprietorship.config.services;

import com.example.soleproprietorship.customer.role.ERole;
import com.example.soleproprietorship.customer.role.Role;
import com.example.soleproprietorship.customer.role.RoleRepository;
import com.example.soleproprietorship.user.User;
import com.example.soleproprietorship.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Test
    public void testLoadUserByUsername() {
        when(roleRepository.findByName(ERole.CUSTOMER)).thenReturn(Optional.of(new Role(ERole.CUSTOMER)));
        User userToTest = new User("login", "password", "email",
                "pesel", "firstName", "surName");
        userToTest.setRole(roleRepository.findByName(ERole.CUSTOMER).get());
        when(userRepository.findByUserName("login")).thenReturn(Optional.of(userToTest));
        UserDetails userFromService = myUserDetailsService.loadUserByUsername("login");
        assertEquals(userToTest.getUserName(), userFromService.getUsername());
    }

    @Test
    public void testLoadUserByUserNameExceptionThrown() {
        when(userRepository.findByUserName("login")).thenThrow(new UsernameNotFoundException("Nie znaleziono takiego użytkownika!"));
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> myUserDetailsService.loadUserByUsername("login"));
        assertEquals(exception.getMessage(), "Nie znaleziono takiego użytkownika!");
    }

    @Test
    @WithMockUser
    public void testGetUserFromToken() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        when(roleRepository.findByName(ERole.CUSTOMER)).thenReturn(Optional.of(new Role(ERole.CUSTOMER)));
        User userToTest = new User(username, "password", "email",
                "pesel", "firstName", "surName");
        userToTest.setRole(roleRepository.findByName(ERole.CUSTOMER).get());
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(userToTest));
        User userFromService = myUserDetailsService.getUserFromToken();
        assertEquals(userToTest, userFromService);
    }

    @Test
    @WithMockUser
    public void testGetUserFromTokenExceptionThrown() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        when(userRepository.findByUserName(username)).thenThrow(new UsernameNotFoundException("Nie znaleziono takiego użytkownika!"));
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> myUserDetailsService.loadUserByUsername(username));
        assertEquals(exception.getMessage(), "Nie znaleziono takiego użytkownika!");
    }
}
