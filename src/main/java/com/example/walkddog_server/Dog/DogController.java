package com.example.walkddog_server.Dog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.HttpResource;

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
    public int addDog(@RequestBody String dogJson) {
        try {
            Map<String, Object> dogMap = JsonParserFactory.getJsonParser().parseMap(dogJson);
            Dog dog = new Dog(dogMap.get("name").toString(), Integer.parseInt(dogMap.get("age").toString()),
                    dogMap.get("gender").toString(), dogMap.get("owner").toString());
            return dogService.insertDog(dog);
        } catch (Exception e) {
            return 0;
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

    @PostMapping("uploadimage")
    public boolean uploadImage(@RequestParam("image") MultipartFile file, @RequestParam("dog_id") String dogId) {
        try {
            dogService.uploadImage(file, Long.parseLong(dogId));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("deleteimage/{dog_id}")
    public boolean deleteImage(@PathVariable("dog_id") String dogId) {
        try {
            return dogService.deleteImage(Long.parseLong(dogId));
        } catch (Exception e) {
            return false;
        }
    }

}
