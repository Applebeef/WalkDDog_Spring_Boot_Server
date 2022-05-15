package com.example.walkddog_server.Dog;


import lombok.Data;


public @Data
class Dog {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String owner;

    private String filename;

    public Dog(int id, String name, int age, String gender, String owner) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.owner = owner;
        this.filename = null;
    }

    public Dog(String name, int age, String gender, String owner) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.owner = owner;
        this.filename = null;
    }

    public Dog(int id, String name, int age, String gender, String owner, DogService dogService) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.owner = owner;
        this.filename = dogService.findFile(id).getName();
    }

    public Dog(int id, String name, int age, String gender, String owner, String filename) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.owner = owner;
        this.filename = filename;
    }
}
