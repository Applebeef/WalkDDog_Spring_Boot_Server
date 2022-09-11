package com.example.walkddog_server.ParkVisitor;

import com.example.walkddog_server.Dog.SimpleDogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("park")
public class ParkController {

    ParkService parkService;

    @Autowired
    public ParkController(ParkService parkService) {
        this.parkService = parkService;
    }

    @GetMapping //This is a test to see if the controller is working
    public String isPark() {
        return "Park";
    }

    @GetMapping("/get/{id}")
    public Map<String, List<SimpleDogInfo>> getAllParkVisitors(@PathVariable String id) {
        return parkService.getAllParkVisitors(id);
    }

    @PostMapping("/add")
    public boolean addParkVisitor(@RequestBody String jsonDetails) {
        try {
            Map<String, Object> parkVisitorMap = JsonParserFactory.getJsonParser().parseMap(jsonDetails);
            List<Integer> dog_ids = (ArrayList) parkVisitorMap.get("dog_id");
            long hoursMillis = Long.parseLong(parkVisitorMap.get("hours").toString()) * 60 * 60 * 1000;
            long minutesMillis = Long.parseLong(parkVisitorMap.get("minutes").toString()) * 60 * 1000;
            Timestamp expiration_time = new Timestamp(System.currentTimeMillis() + hoursMillis + minutesMillis);
            int res = 1;
            for (Integer dog_id : dog_ids) {
                ParkVisitor pv = new ParkVisitor(parkVisitorMap.get("park_id").toString(),
                        parkVisitorMap.get("visitor_name").toString(),
                        Integer.toString(dog_id),
                        expiration_time);
                res *= parkService.addParkVisitor(pv);
            }
            return res == 1;
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping("/remove/{name}")
    public int removeParkVisitor(@PathVariable String name) {
        return parkService.removeParkVisitor(name);
    }

    @GetMapping("/check/{name}")
    public boolean checkParkVisitor(@PathVariable String name) {
        return parkService.checkIfCurrentlyInPark(name);
    }

    @Scheduled(fixedDelay = 60000)
    public void removeExpiredParkVisitors() {
        parkService.removeExpiredParkVisitors();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        System.out.println("Removed expired park visitors at " + now);
    }

}
