package com.amaral.taskly.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amaral.taskly.service.UserService;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{userPublicId}/access/{accessPublicId}")
    public ResponseEntity<Void> assignAccess(@PathVariable UUID userPublicId, @PathVariable UUID accessPublicId) {
        userService.assignAccess(userPublicId, accessPublicId);
        return ResponseEntity.ok().build();
    }
}
