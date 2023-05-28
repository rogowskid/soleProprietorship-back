package com.example.soleproprietorship.config.services;

import com.example.soleproprietorship.user.User;
import com.example.soleproprietorship.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    public User getUserFromToken() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono takiego użytkownika!"));
    }
}
