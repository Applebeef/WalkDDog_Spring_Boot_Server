package com.example.walkddog_server.Dog;

import lombok.Data;

public @Data class SimpleDogInfo {
    private String name;
    private long id;
    private String filename;

    private void Constructor(String name, long id, String filename) {
        this.name = name;
        this.id = id;
        this.filename = filename;
    }

    public SimpleDogInfo(String name, long id, String filename) {
        Constructor(name, id, filename);
    }

    public SimpleDogInfo(String name, String id, String filename) {
        Constructor(name, Long.parseLong(id), filename);
    }

    public SimpleDogInfo(String name, long id, DogService dogService) {
        Constructor(name, id, dogService.findFile(id).getName());
    }

    public SimpleDogInfo(String name, String id, DogService dogService) {
        Constructor(name, Long.parseLong(id), dogService.findFile(id).getName());
    }

    public SimpleDogInfo(Dog dog, DogService dogService) {
        Constructor(dog.getName(), dog.getId(), dogService.findFile(dog.getId()).getName());
    }
}
