package com.test.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.demo.model.User;
import com.test.demo.service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/method")
    public String getMethodName() {
        System.out.println("dmwodmwodmwodwmdo");
        return "hellow";
    }

    @GetMapping("/getAll")
    public List<User> getAll() {
        System.out.println("Getting all users");
        return userService.getAllUsers();
    }
}
