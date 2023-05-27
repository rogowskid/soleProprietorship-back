package com.example.soleproprietorship.customer.role;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    @PostConstruct
    private void addRole(){
        if(!repository.findAll().isEmpty())
            return;

        Role role = new Role();
        role.setName(ERole.CUSTOMER);
        repository.save(role);
    }

}
