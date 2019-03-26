package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.UserDAO;
import com.fantech.addressmanager.api.dto.user.UserDto;
import com.fantech.addressmanager.api.entity.User;
import com.fantech.addressmanager.api.helpers.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    public Map register(UserDto user) {

        User userToSave = new User(user.getEmail(), user.getPassword());
        HashMap<String, Boolean> response = new HashMap<>();

        boolean res = userDAO.create(userToSave);

        if(res) return authService.jwtClaim(userDAO.findByEmail(user.getEmail()).getUuid().toString());
        else {
            response.put("created", false);
            return response;
        }
    }

    public Map login(UserDto user) {

        HashMap<String, Boolean> response = new HashMap<>();
        if(user.getEmail()!=null && user.getPassword()!=null){
            User userFound = userDAO.findByEmail(user.getEmail());

            if (userFound != null) {
                boolean match = authService.passwordMatch(user.getPassword(), userFound.getPassword());
                if (match) return authService.jwtClaim(userFound.getUuid().toString());
            }
        }else {

            response.put("created", false);

        }
        return response;
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Invalid token")
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
