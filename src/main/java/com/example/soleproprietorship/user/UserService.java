package com.example.soleproprietorship.user;

import com.example.soleproprietorship.common.EntityModelValid;
import com.example.soleproprietorship.config.services.MyUserDetailsService;
import com.example.soleproprietorship.customer.role.RoleRepository;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements EntityModelValid<User, Long> {
    private UserRepository repository;
    private MyUserDetailsService userDetailsService;
    private PasswordEncoder encoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository repository, MyUserDetailsService userDetailsService,
                       PasswordEncoder encoder, RoleRepository roleRepository) {
        this.repository = repository;
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    /**
     * Pobranie użytkownika z sesji
     * @return
     */
    public UserDTO getUser() {
        User user = userDetailsService.getUserFromToken();
        return mapEntityToDTO(user);
    }

    /**
     * Modyfikacja użytkownika
     * @param dto
     */
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
                user.getFirstName(), user.getSurName(), user.getAddress(), user.isUsing2FA());
    }

    @Override
    public User executeEncode(User model) {
        User user = new User();
        user.setIdUser(model.getIdUser());
        user.setProducts(model.getProducts());
        user.setJobs(model.getJobs());
        user.setEmail(parseEmail(model.getEmail()));
        user.setPassword(Encode.forHtml(model.getPassword()));
        user.setAddress(Encode.forHtml(model.getAddress()));
        user.setFirstName(Encode.forHtml(model.getFirstName()));
        user.setSurName(Encode.forHtml(model.getSurName()));
        user.setPhoneNumber(parsePhoneNumber(model.getPhoneNumber()));
        user.setRole(model.getRole());
        user.setCustomers(model.getCustomers());
        user.setPesel(model.getPesel());
        user.setUserName(model.getUserName());
        user.setTransactions(model.getTransactions());
        user.setUsing2FA(model.isUsing2FA());
        return user;
    }

    @Override
    public List<User> executeEncodeList(List<User> models) {

        ArrayList<User> users = new ArrayList<>();

        for (User model : models) {
            users.add(executeEncode(model));
        }

        return users;

    }

    @Override
    public User getEntity(Long id) {
        return userDetailsService.getUserFromToken();
    }

    @Override
    public List<User> getEntities() {
        return repository.findAll();
    }
}
