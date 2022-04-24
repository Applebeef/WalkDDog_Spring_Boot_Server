package com.example.walkddog_server.User;

import com.example.walkddog_server.Dog.Dog;
import lombok.Data;

import java.util.List;

public @Data
class User {
    private String username;
    //TODO add hashing/salting
    private String password;
    private String email;
    private String first_name;
    private String last_name;

    private List<Dog> dogs;


    public User(String username, String password, String email, String first_name, String last_name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dogs = null;
    }

    public User whereDogs(List<Dog> dogs) {
        this.dogs = dogs;
        return this;
    }

}
