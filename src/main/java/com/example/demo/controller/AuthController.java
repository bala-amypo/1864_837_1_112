package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public User login(@RequestBody User request) {

        User user = userService.findByEmail(request.getEmail());

        // 🔴 IMPORTANT: simple password check
        if (!user.getPassword().equals(request.getPassword() + "_ENC")) {
            throw new BadRequestException("Invalid credentials");
        }

        return user;
    }
}
