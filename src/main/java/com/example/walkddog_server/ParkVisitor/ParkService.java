package com.example.walkddog_server.ParkVisitor;

import com.example.walkddog_server.Dog.Dog;
import com.example.walkddog_server.Dog.DogService;
import com.example.walkddog_server.Dog.SimpleDogInfo;
import com.example.walkddog_server.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkService {

    private final JdbcTemplate jdbcTemplate;
    private final DogService dogService;

    @Autowired
    public ParkService(JdbcTemplate jdbcTemplate, DogService dogService) {
        this.jdbcTemplate = jdbcTemplate;
        this.dogService = dogService;
    }

    public Map<String, List<SimpleDogInfo>> getAllParkVisitors(String park_id) {
        List<ParkVisitor> list = jdbcTemplate.query("SELECT PV.*, D.dog_name FROM park_visitor PV, dog D WHERE park_id = '" + park_id + "'" + "AND D.dog_id = PV.dog_id", (rs, rowNum) -> new ParkVisitor(
                rs.getString("park_id"),
                rs.getString("visitor_name"),
                rs.getString("dog_id"),
                rs.getString("dog_name")));
        return createOwnerToDogsMap(list);
    }

    private Map<String, List<SimpleDogInfo>> createOwnerToDogsMap(List<ParkVisitor> list) {
        Map<String, List<SimpleDogInfo>> ownerToDogsMap = new HashMap<>();
        for (ParkVisitor parkVisitor : list) {
            if (!ownerToDogsMap.containsKey(parkVisitor.getVisitor_name())) {
                ownerToDogsMap.put(parkVisitor.getVisitor_name(), new ArrayList<>());
            }
            SimpleDogInfo info = new SimpleDogInfo(parkVisitor.getDog_name(), parkVisitor.getDog_id(), dogService);
            ownerToDogsMap.get(parkVisitor.getVisitor_name()).add(info);
        }
        return ownerToDogsMap;
    }

    public void insertParkVisitor(ParkVisitor parkVisitor) {
        jdbcTemplate.update("INSERT INTO park_visitor (park_id, visitor_name, dog_id) VALUES (?, ?, ?)",
                parkVisitor.getPark_id(), parkVisitor.getVisitor_name(), parkVisitor.getDog_id());
    }

    public void deleteParkVisitor(ParkVisitor parkVisitor) {
        jdbcTemplate.update("DELETE FROM park_visitor WHERE park_id = ? AND dog_id = ?",
                parkVisitor.getPark_id(), parkVisitor.getDog_id());
    }

    public void deleteParkVisitorByUsername(String park_id, String username) {
        jdbcTemplate.update("DELETE FROM park_visitor WHERE park_id = ? AND visitor_name = ?",
                park_id, username);
    }

    public int addParkVisitor(ParkVisitor pv) {
        return jdbcTemplate.update("INSERT INTO park_visitor (park_id, visitor_name, dog_id) VALUES (?, ?, ?)",
                pv.getPark_id(), pv.getVisitor_name(), pv.getDog_id());
    }

    public int removeParkVisitor(String name) {
        return jdbcTemplate.update("DELETE FROM park_visitor WHERE visitor_name = ?", name);
    }
}
