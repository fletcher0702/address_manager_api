package com.fantech.addressmanager.api.helpers;


import com.eclipsesource.json.JsonObject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class AuthHelper {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    public static boolean passwordMatch(String plainPwd, String hashed) {
        return BCrypt.checkpw(plainPwd, hashed);
    }

    public static Map jwtClaim(String userUuid){
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        HashMap<String, Object> credentials = new HashMap<>();
        credentials.put("uuid", userUuid);

        String jws = Jwts
                .builder()
                .setClaims(credentials)
                .signWith(key)
                .compact();
        JsonObject jwt = new JsonObject();
        jwt.add("jwt",jws);
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return  jsonParser.parseMap(jwt.toString());
    }
}
