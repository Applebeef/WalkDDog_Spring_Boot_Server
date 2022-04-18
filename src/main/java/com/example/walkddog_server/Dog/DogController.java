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
    public boolean addDog(@RequestBody String dogJson) {
        try {
            Map<String, Object> dogMap = JsonParserFactory.getJsonParser().parseMap(dogJson);
            Dog dog = new Dog(dogMap.get("name").toString(), Integer.parseInt(dogMap.get("age").toString()), dogMap.get("owner").toString());
            int res = dogService.insertDog(dog);
            return res == 1;
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping("delete")
    public boolean deleteDog(@RequestBody String dogJson) {
        //TODO need to send dog_id in the body to delete
        try {
            Map<String, Object> dogMap = JsonParserFactory.getJsonParser().parseMap(dogJson);
            int res = dogService.deleteDog(Long.parseLong(dogMap.get("id").toString()));
            return res == 1;
        } catch (Exception e) {
            return false;
        }
    }

}
