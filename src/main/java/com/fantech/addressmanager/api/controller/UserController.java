package com.fantech.addressmanager.api.controller;

import com.fantech.addressmanager.api.dto.UserPasswordUpdateDto;
import com.fantech.addressmanager.api.dto.user.UserDto;
import com.fantech.addressmanager.api.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{userUuid}")
    public Object findOne(@PathVariable("userUuid") String userUuid){
        return userService.findOne(userUuid);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map createUser(@RequestBody UserDto user) {
        if (user.getEmail() != null && user.getPassword() != null) {
            return userService.register(user);
        } else return null;

    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map login(@RequestBody UserDto user) {
        return userService.login(user);
    }

    @PatchMapping(value = "/password/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object passwordUpdate(@RequestBody UserPasswordUpdateDto userPasswordUpdateDto) {
        return userService.updatePassword(userPasswordUpdateDto);
    }

    @GetMapping("/checktoken")
    public Object checkJwtIntegrity(@RequestHeader HttpHeaders headers){
        return userService.checkJwtIntegrity(headers);
    }

    @GetMapping("")
    public List findAll(){
        return userService.findAll();
    }


}
