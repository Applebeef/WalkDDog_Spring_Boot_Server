package com.example.walkddog_server.Key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("key")
public class KeyController {

    KeyService keyService;

    @Autowired
    public KeyController(KeyService keyService) {
        this.keyService = keyService;
    }

    @RequestMapping("/get")
    public String getKey() {
        return keyService.getKey();
    }
}
