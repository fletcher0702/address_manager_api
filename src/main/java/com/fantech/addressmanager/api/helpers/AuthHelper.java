package com.fantech.addressmanager.api.helpers;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class AuthHelper {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    public static boolean passwordMatch(String plainPwd, String hashed) {
        return BCrypt.checkpw(plainPwd, hashed);
    }
}
