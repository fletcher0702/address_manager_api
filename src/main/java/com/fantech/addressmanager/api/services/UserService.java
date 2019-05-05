package com.fantech.addressmanager.api.services;

import com.fantech.addressmanager.api.dao.UserDAO;
import com.fantech.addressmanager.api.dto.UserPasswordUpdateDto;
import com.fantech.addressmanager.api.dto.user.UserDto;
import com.fantech.addressmanager.api.entity.User;
import com.fantech.addressmanager.api.helpers.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.transaction.Transactional;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Component

public class UserService {
    private UserDAO userDAO;
    private AuthService authService;
    private HashMap<String,Object> response = new HashMap<>();

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

        try{

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

        }catch (Exception e){
            e.printStackTrace();
            response.put("created", false);
            return response;
        }

    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Invalid token")
    public Object checkJwtIntegrity(HttpHeaders headers){

        String token = authService.getToken(headers);
        HashMap<String, Object> res = new HashMap<>();
        try{

            if(!token.isEmpty()){
                    res = authService.decodeJwt(token);
                    if((boolean)res.get("valid")){
                        User u = userDAO.findByUuid(UUID.fromString((String) res.get("uuid")));
                        if(u!=null){

                            res.putIfAbsent("valid",true);
                        }else{
                            res.putIfAbsent("valid",false);
                        }
                    }else res.putIfAbsent("valid",false);

            }
            return res;

        }catch (Exception e){
            e.printStackTrace();
            res.put("valid", false);
            res.put("message", "401 Unauthorized");
            return res;
        }

    }

    public List findAll(){
        return userDAO.findAll();
    }

    public Object findOne(String userUuid){

        User u = userDAO.findByUuid(UUID.fromString(userUuid));

        HashMap<String,Object> res = new HashMap<>();

        if(u!=null){
            res.putIfAbsent("exist",true);
            res.put("email",u.getEmail());
            return  res;
        }

        res.put("exist",false);

        return res;
    }

    public Object updatePassword(UserPasswordUpdateDto userPasswordUpdateDto){

        try {

            response.clear();
            assertNotNull(userPasswordUpdateDto);
            assertNotNull(userPasswordUpdateDto.getUserUuid());
            assertNotNull(userPasswordUpdateDto.getOldPassword());
            assertNotNull(userPasswordUpdateDto.getNewPassword());

            User u = userDAO.findByUuid(UUID.fromString(userPasswordUpdateDto.getUserUuid()));

            assertNotNull(u);
            System.out.println("User found...continue...");
            if(authService.passwordMatch(userPasswordUpdateDto.getOldPassword(),u.getPassword()) && !userPasswordUpdateDto.getNewPassword().isEmpty()){

                System.out.println("Password match...");
                response.put("updated", userDAO.updateUserPassword(u.getUuid(),AuthService.hashPassword(userPasswordUpdateDto.getNewPassword())));

            }else response.put("updated", false);

            return response;

        }catch (Exception e){
            e.printStackTrace();
            response.put("updated", false);
            return response;
        }
    }

    @Transactional
    public Object deleteUserAccount(String userUuid){

        response.clear();

        try{
            System.out.println("UserUuid : "+ userUuid);
            assertNotNull(userUuid);
            if(userUuid.isEmpty()) {
                response.put("message","Enter user UUID");
                return response;
            }

            User u = userDAO.findByUuid(UUID.fromString(userUuid));

            System.out.println("UUID : " + u.getUuid());
            assertNotNull(u);
            response.put("deleted", userDAO.delete(u));

            return response;

        }catch (Exception e){

            response.put("message","Error something went wring when deleting the account");
            e.printStackTrace();

            return response;

        }
    }
}
