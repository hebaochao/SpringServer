package com.it.data.controller;

import com.it.data.bean.User;
import com.it.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestJpaController {

    @Autowired
    private UserService userService;

    @GetMapping("/addUser")
    public User addUser(@RequestParam("name") String name) {
        User user = new User(System.currentTimeMillis(), UUID.randomUUID().toString(), name);
        this.userService.addUser(user);
        return this.userService.getUserById(2L);

    }
}
