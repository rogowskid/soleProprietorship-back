package com.example.soleproprietorship.customer.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    @PostConstruct
    private void addRole(){
        if(!repository.findAll().isEmpty())
            return;

        repository.save(new Role(ERole.CUSTOMER));
        repository.save(new Role(ERole.MODERATOR));
    }

}
