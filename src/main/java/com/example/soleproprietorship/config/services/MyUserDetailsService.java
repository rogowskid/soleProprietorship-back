package com.example.soleproprietorship.config.services;

import com.example.soleproprietorship.user.User;
import com.example.soleproprietorship.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono takiego użytkownika!"));

        return UserDetailsImpl.build(user);
    }
}
