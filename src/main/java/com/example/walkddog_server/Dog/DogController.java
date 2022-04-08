package com.example.walkddog_server.Dog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dog")
public class DogController {

    private final DogService dogService;

    @Autowired
    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @GetMapping //This is a test to see if the controller is working
    public String isDog() {
        return "Dog";
    }

    @GetMapping("/all")
    public List<Dog> getAllDogs() {
        return dogService.getAllDogs();
    }

    @GetMapping("/byowner/{ownerName}")
    public List<Dog> getDogsByOwner(@PathVariable String ownerName) {
        return dogService.getDogsByOwner(ownerName);
    }

    @PostMapping("/add")
    public void addDog(@RequestBody String dogJson) {
        Map<String, Object> dogMap = JsonParserFactory.getJsonParser().parseMap(dogJson);
//        dogService.insertDog(dog);
    }

    @PostMapping("delete")
    public void deleteDog(@RequestBody String dogJson) {
        Map<String, Object> dogMap = JsonParserFactory.getJsonParser().parseMap(dogJson);
//        dogService.deleteDog(dogMap);
    }

}
