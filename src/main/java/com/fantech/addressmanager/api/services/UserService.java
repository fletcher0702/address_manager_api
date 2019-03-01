package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.UserDAO;
import com.fantech.addressmanager.api.dto.user.UserDto;
import com.fantech.addressmanager.api.entity.User;
import com.fantech.addressmanager.api.helpers.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component

public class UserService {
    private UserDAO userDAO;
    private AuthService authService;

    @Autowired
    @Lazy
    public UserService(UserDAO userDAO, AuthService authService) {
        this.userDAO = userDAO;
        this.authService = authService;
    }

    public Map createUser(UserDto user) {

        User userToSave = new User(user.getEmail(), user.getPassword());

        boolean res = userDAO.create(userToSave);

        if(res) return authService.jwtClaim(userDAO.findByEmail(user.getEmail()).getUuid().toString());
        else return null;
    }

    public Map login(UserDto user) {

        if(user.getEmail()!=null && user.getPassword()!=null){
            User userFound = userDAO.findByEmail(user.getEmail());

            if (userFound != null) {
                boolean match = authService.passwordMatch(user.getPassword(), userFound.getPassword());
                if (match) return authService.jwtClaim(userFound.getUuid().toString());
            }
        }
        return null;
    }

    public Object checkJwtIntegrity(HttpHeaders headers){

        String token = authService.getToken(headers);
        HashMap<String, Object> res = new HashMap<>();
        if(!token.isEmpty()){

            try{
                return authService.decodeJwt(token);
            }catch(Exception e){
                res.put("valid", false);
                e.printStackTrace();
                return res;
            }
        }
        res.put("message", "401 Unauthorized");
        return res;
    }

    public List findAll(){
        return userDAO.findAll();
    }
}
