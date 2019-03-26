package com.fantech.addressmanager.api.helpers;


import com.eclipsesource.json.JsonObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AuthService {

    @Value("${JWT_KEY}")
    private String jwtKey;
    private Key key;

    @PostConstruct
    public void init(){
        key = Keys.hmacShaKeyFor(jwtKey.getBytes());
    }
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    public boolean passwordMatch(String plainPwd, String hashed) {
        return BCrypt.checkpw(plainPwd, hashed);
    }

    public Map jwtClaim(String userUuid) {

        HashMap<String, Object> credentials = new HashMap<>();
        credentials.put("uuid", userUuid);

        Date now = new Date();

        String jws = Jwts
                .builder()
                .setId(userUuid)
                .setClaims(credentials)
                .signWith(key)
                .setExpiration(new Date(now.getYear()+1,now.getMonth(),now.getDay()))
                .compact();
        JsonObject jwt = new JsonObject();
        jwt.add("uuid", userUuid);
        jwt.add("jwt", jws);
        jwt.add("created",true);
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(jwt.toString());
    }

    private boolean jwtIntegrity(HttpHeaders headers) {
        return headers.get("authorization") != null;
    }

    public String getToken(HttpHeaders headers) {
        return jwtIntegrity(headers) ? Objects.requireNonNull(headers.get("authorization")).get(0).split(" ")[1] : "";
    }

    public HashMap decodeJwt(String token) {
        Jws<Claims> claimsJws = Jwts
                .parser()
                .setSigningKey(key)
                .parseClaimsJws(token);
        HashMap<String, Object> content = new HashMap<>();
        boolean uuidPresence = claimsJws.getBody().containsKey("uuid");
        content.put("valid", uuidPresence);
        if(uuidPresence)content.put("uuid", claimsJws.getBody().get("uuid"));

        return content;
    }
}
