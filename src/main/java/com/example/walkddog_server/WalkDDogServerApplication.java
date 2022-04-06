package com.example.walkddog_server;

import com.example.walkddog_server.Dog.Dog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class WalkDDogServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalkDDogServerApplication.class, args);
    }

}
