package com.example.walkddog_server.ParkVisitor;

import com.example.walkddog_server.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.bind.annotation.*;

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
    public List<ParkVisitor> getAllParkVisitors(@PathVariable String id) {
        return parkService.getAllParkVisitors(id);
    }

    @PostMapping("/add")
    public boolean addParkVisitor(@RequestBody String jsonDetails) {
        try {
            Map<String, Object> parkVisitorMap = JsonParserFactory.getJsonParser().parseMap(jsonDetails);
            ParkVisitor pv = new ParkVisitor(parkVisitorMap.get("park_id").toString(), parkVisitorMap.get("visitor_name").toString(),
                    parkVisitorMap.get("dog_id").toString());
            int res = parkService.addParkVisitor(pv);
            return res == 1;
        } catch (Exception e) {
            return false;
        }
    }
}
