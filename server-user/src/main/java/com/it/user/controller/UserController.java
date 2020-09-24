package com.it.user.controller;

import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/login")
    public Map<String, Object> login(@RequestParam("name") String name, @RequestParam("password") String password) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("name", name);
        result.put("psw", password);
        result.put("loginTime",System.currentTimeMillis());
        return result;
    }


}
