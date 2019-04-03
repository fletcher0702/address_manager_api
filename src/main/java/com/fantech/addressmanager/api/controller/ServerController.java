package com.fantech.addressmanager.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class ServerController {

    @GetMapping("/server/bot")
    public Object canSendResponse(){
        HashMap<String,Boolean> res = new HashMap<>();
        res.put("working",true);
        return res;
    }
}
