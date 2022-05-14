package com.example.walkddog_server.User;

import com.example.walkddog_server.Dog.Dog;
import lombok.Data;

import java.util.List;

public @Data class SimpleUserInfo {
    private String username;
    private String first_name;
    private String last_name;
    private List<Dog> dogs;

    public SimpleUserInfo(String username, String first_name, String last_name) {
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dogs = null;
    }

    public SimpleUserInfo(User user) {
        this.username = user.getUsername();
        this.first_name = user.getFirst_name();
        this.last_name = user.getLast_name();
        this.dogs = user.getDogs();
    }

    public SimpleUserInfo whereDogs(List<Dog> dogs) {
        this.dogs = dogs;
        return this;
    }
}
