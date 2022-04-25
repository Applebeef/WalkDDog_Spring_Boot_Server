package com.example.walkddog_server.User;

import com.example.walkddog_server.Dog.Dog;
import com.example.walkddog_server.Dog.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    UserService userService;
    DogService dogService;

    @Autowired
    public UserController(UserService userService, DogService dogService) {
        this.userService = userService;
        this.dogService = dogService;
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/byusername/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUser(username).whereDogs(dogService.getDogsByOwner(username));
    }

    @PostMapping("/register")
    public boolean registerUser(@RequestBody String userDetails) {
        //TODO add exception for existing username
        try {
            Map<String, Object> userMap = JsonParserFactory.getJsonParser().parseMap(userDetails);
            //TODO tell Amit to regex email
            final User user = new User(userMap.get("username").toString(), userMap.get("password").toString(),
                    userMap.get("email").toString(), userMap.get("first_name").toString(),
                    userMap.get("last_name").toString());
            int res = userService.registerUser(user);
            List<Map<String, Object>> list = (ArrayList) userMap.get("dogs");
            list.forEach(dogMap -> {
                Dog dog = new Dog(dogMap.get("name").toString(), Integer.parseInt(dogMap.get("age").toString()),
                        dogMap.get("gender").toString(), user.getUsername());
                dogService.insertDog(dog);
            });
            return res == 1;
        } catch (Exception e) {
            for (StackTraceElement element : e.getStackTrace()) {
                System.out.println(element.toString());
            }
            System.out.println(e.getMessage());
            return false;
        }
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody String username) {
        try {
            Map<String, Object> userMap = JsonParserFactory.getJsonParser().parseMap(username);
            String user = userMap.get("username").toString();
            int res = userService.deleteUser(user);
            return res == 1;
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping("/login")
    public boolean loginUser(@RequestBody String userDetails) {
        try {
            Map<String, Object> userMap = JsonParserFactory.getJsonParser().parseMap(userDetails);
            String user = userMap.get("username").toString();
            String pass = userMap.get("password").toString();
            return userService.loginUser(user, pass);
        } catch (Exception e) {
            return false;
        }
    }

}
