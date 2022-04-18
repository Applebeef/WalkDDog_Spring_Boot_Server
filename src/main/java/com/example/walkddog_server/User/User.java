package com.example.walkddog_server.User;

import lombok.Data;

public @Data
class User {
    private String username;

    //TODO add hashing/salting
    private String password;


    private String email;
    private String first_name;
    private String last_name;


    public User(String username, String password, String email, String first_name, String last_name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;

    }

}
