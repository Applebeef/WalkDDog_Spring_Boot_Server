package com.example.walkddog_server.Dog;


import lombok.Data;


public @Data
class Dog {
    private int id;
    private String name;
    private int age;
    private String owner;

    public Dog(int id, String name, int age, String owner) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.owner = owner;
    }

    public Dog(String name, int age, String owner) {
        this.name = name;
        this.age = age;
        this.owner = owner;
    }
}
