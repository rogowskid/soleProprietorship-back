package com.example.soleproprietorship.user;

import com.example.soleproprietorship.config.services.MyUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private MyUserDetailsService userDetailsService;
    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUser() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        UserDTO userDTOFromService = userService.getUser();
        assertEquals("firstName", userDTOFromService.getFirstName());
    }

    @Test
    public void testEditUser() {
        User userToTest = new User("username", "password", "email",
                "pesel", "firstName", "surName");
        when(userDetailsService.getUserFromToken()).thenReturn(userToTest);
        UserDTO userDTOToTest = new UserDTO("email", "phoneNumber", "pesel",
                "firstName2", "surName", "address", false);
        userService.editUser(userDTOToTest);
    }
}
