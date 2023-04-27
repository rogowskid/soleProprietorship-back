package com.example.soleproprietorship.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserDTO> getUser() {
        return new ResponseEntity<>(userService.getUser(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> editUser(@Valid @RequestBody UserDTO dto) {
        userService.editUser(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
