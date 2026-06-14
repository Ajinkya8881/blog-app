package com.ajinkya.blogapp.service;

import com.ajinkya.blogapp.entity.Role;
import com.ajinkya.blogapp.entity.User;
import com.ajinkya.blogapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public void registerUser(
            String username,
            String name,
            String email,
            String password
    ){

        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.ROLE_AUTHOR);
        userRepository.save(user);


    }
}
