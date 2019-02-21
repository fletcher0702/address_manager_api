package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.UserDAO;
import com.fantech.addressmanager.api.dto.user.UserDto;
import com.fantech.addressmanager.api.entity.User;
import com.fantech.addressmanager.api.helpers.AuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component

public class UserService {
    private UserDAO userDAO;

    @Autowired
    @Lazy
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User createUser(UserDto user) {

        User userToSave = new User(user.getEmail(), user.getPassword());

        boolean res = userDAO.create(userToSave);

        if(res) return userDAO.findByEmail(user.getEmail());
        else return null;
    }

    public User login(UserDto user) {

        if(user.getEmail()!=null && user.getPassword()!=null){
            User userFound = userDAO.findByEmail(user.getEmail());

            if (userFound != null) {
                boolean match = AuthHelper.passwordMatch(user.getPassword(), userFound.getPassword());
                if (match) return userFound;
            }
        }
        return null;
    }

    public List findAll(){
        return userDAO.findAll();
    }
}
