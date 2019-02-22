package com.fantech.addressmanager.api.controller;

import com.eclipsesource.json.JsonObject;
import com.fantech.addressmanager.api.dto.user.UserDto;
import com.fantech.addressmanager.api.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    @Lazy
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map createUser(@RequestBody UserDto user) {
        if (user.getEmail() != null && user.getPassword() != null) {
            return userService.createUser(user);
        } else return null;

    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map login(@RequestBody UserDto user) {
        return userService.login(user);
    }

    @GetMapping("")
    public List findAll(){
        return userService.findAll();
    }


}
