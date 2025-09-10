package com.amaral.taskly.controller;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amaral.taskly.model.User;
import com.amaral.taskly.security.JwtUtils;
import com.amaral.taskly.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String login = body.get("login");
        String password = body.get("password");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        String token = jwtUtils.generateJwt(login);
        return Map.of("token", token);
    }

    @PostMapping("/register")
    public User register(@RequestBody Map<String, String> body) {
        return userService.register(body.get("login"), body.get("password"));
    }
}
