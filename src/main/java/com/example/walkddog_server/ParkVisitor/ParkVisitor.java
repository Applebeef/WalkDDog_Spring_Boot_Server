package com.example.walkddog_server.ParkVisitor;

import lombok.Data;

import java.sql.Timestamp;

public @Data
class ParkVisitor {
    private String park_id;
    private String visitor_name;
    private String dog_id;
    private String dog_name;
    private Timestamp expiration_time;

    public ParkVisitor(String park_id, String visitor_name, String dog_id) {
        this.park_id = park_id;
        this.visitor_name = visitor_name;
        this.dog_id = dog_id;
    }

    public ParkVisitor(String park_id, String visitor_name, String dog_id, String dog_name) {
        this.park_id = park_id;
        this.visitor_name = visitor_name;
        this.dog_id = dog_id;
        this.dog_name = dog_name;
    }

    public ParkVisitor(String park_id, String visitor_name, String dog_id, Timestamp expiration_time) {
        this.park_id = park_id;
        this.visitor_name = visitor_name;
        this.dog_id = dog_id;
        this.expiration_time = expiration_time;
    }

    public ParkVisitor(String park_id, String visitor_name, String dog_id, String dog_name, Timestamp expiration_time) {
        this.park_id = park_id;
        this.visitor_name = visitor_name;
        this.dog_id = dog_id;
        this.dog_name = dog_name;
        this.expiration_time = expiration_time;
    }
}


