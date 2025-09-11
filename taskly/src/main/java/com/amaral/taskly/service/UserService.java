package com.amaral.taskly.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.amaral.taskly.model.User;
import com.amaral.taskly.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.userRepository = repo;
        this.passwordEncoder = encoder;
    }
    
    public User register(String login, String rawPassword) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new RuntimeException("Login já existe");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setPasswordCreatedAt(new java.util.Date());

        //criar primeiro acesso
        return userRepository.save(user);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
