package com.example.walkddog_server.User;

import com.example.walkddog_server.Dog.Dog;
import com.example.walkddog_server.Dog.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<SimpleUserInfo> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream().map(user -> new SimpleUserInfo(user)
                .whereDogs(dogService.getDogsByOwner(user.getUsername()))).collect(Collectors.toList());
    }

    @GetMapping("/byusername/{username}")
    public SimpleUserInfo getUser(@PathVariable String username) {
        return userService.getSimpleUserInfo(username).whereDogs(dogService.getDogsByOwner(username));
    }

    @GetMapping("/validate/{username}")
    public boolean validateUser(@PathVariable String username) {
        return userService.validateUser(username);
    }

    @PostMapping("/register")
    public List<Integer> registerUser(@RequestBody String userDetails) {
        //TODO add exception for existing username
        try {
            Map<String, Object> userMap = JsonParserFactory.getJsonParser().parseMap(userDetails);
            //TODO tell Amit to regex email
            final User user = new User(userMap.get("username").toString(), userMap.get("password").toString(),
                    userMap.get("email").toString(), userMap.get("first_name").toString(),
                    userMap.get("last_name").toString(), userMap.get("push_token").toString());
            userService.registerUser(user);
            List<Map<String, Object>> list = (ArrayList) userMap.get("dogs");
            return list.stream().map(dogMap -> new Dog(dogMap.get("name").toString(),
                            Integer.parseInt(dogMap.get("age").toString()),
                            dogMap.get("gender").toString(),
                            user.getUsername(), dogMap.get("breed").toString()))
                    .map(dogService::insertDog).collect(Collectors.toList());
        } catch (Exception e) {
            for (StackTraceElement element : e.getStackTrace()) {
                System.out.println(element.toString());
            }
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody String username) {
        try {
            Map<String, Object> userMap = JsonParserFactory.getJsonParser().parseMap(username);
            String user = userMap.get("username").toString();
            List<Dog> dogs = dogService.getDogsByOwner(user);
            for (Dog dog : dogs) {
                dogService.deleteDog(dog.getId());
            }
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

    @GetMapping("/friends/{username}")
    public List<String> getFriends(@PathVariable String username) {
        return userService.getFriends(username);
    }

    @PostMapping("/friends/{sender}/{receiver}")
    public boolean addFriendRequest(@PathVariable String sender, @PathVariable String receiver) {
        return userService.addFriendRequest(sender, receiver);
    }

}
