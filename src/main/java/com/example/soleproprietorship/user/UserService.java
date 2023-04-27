package com.example.soleproprietorship.user;

import com.example.soleproprietorship.config.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private MyUserDetailsService userDetailsService;

    public UserDTO getUser() {
        User user = userDetailsService.getUserFromToken();
        return mapEntityToDTO(user);
    }

    public void editUser(UserDTO dto) {
        User user = userDetailsService.getUserFromToken();
        user.setEmail(dto.getEmail() != null ? dto.getEmail() : user.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : user.getPhoneNumber());
        user.setPesel(dto.getPesel() != null ? dto.getPesel() : user.getPesel());
        user.setFirstName(dto.getFirstName() != null ? dto.getFirstName() : user.getFirstName());
        user.setSurName(dto.getSurName() != null ? dto.getSurName() : user.getSurName());
        user.setAddress(dto.getAddress() != null ? dto.getAddress() : user.getAddress());
        repository.save(user);
    }

    private UserDTO mapEntityToDTO(User user) {
        return new UserDTO(user.getEmail(), user.getPhoneNumber(), user.getPesel(),
                user.getFirstName(), user.getSurName(), user.getAddress());
    }
}
