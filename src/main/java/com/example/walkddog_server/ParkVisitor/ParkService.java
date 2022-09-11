package com.example.walkddog_server.ParkVisitor;

import com.example.walkddog_server.Dog.DogService;
import com.example.walkddog_server.Dog.SimpleDogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkService {

    private final JdbcTemplate jdbcTemplate;
    private final DogService dogService;

    private static class ParkVisitorRowMapper implements RowMapper<ParkVisitor> {
        @Override
        public ParkVisitor mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ParkVisitor(rs.getString("park_id"),
                    rs.getString("visitor_name"),
                    rs.getString("dog_id"),
                    rs.getString("expiration_time"));
        }
    }

    @Autowired
    public ParkService(JdbcTemplate jdbcTemplate, DogService dogService) {
        this.jdbcTemplate = jdbcTemplate;
        this.dogService = dogService;
    }

    public Map<String, List<SimpleDogInfo>> getAllParkVisitors(String park_id) {
        final String sql = "SELECT PV.*, D.dog_name FROM park_visitor PV, dog D WHERE park_id = ? AND D.dog_id = PV.dog_id";
        List<ParkVisitor> list = jdbcTemplate.query(sql, new ParkVisitorRowMapper(), park_id);
        return createOwnerToDogsMap(list);
    }

    //TODO move this function to the frontend
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
        return jdbcTemplate.update("INSERT INTO park_visitor (park_id, visitor_name, dog_id, expiration_time) VALUES (?, ?, ?, ?)",
                pv.getPark_id(), pv.getVisitor_name(), pv.getDog_id(), pv.getExpiration_time());
    }

    public int removeParkVisitor(String name) {
        return jdbcTemplate.update("DELETE FROM park_visitor WHERE visitor_name = ?", name);
    }

    public boolean checkIfCurrentlyInPark(String username){
        final String sql = "SELECT * FROM park_visitor WHERE visitor_name = ?";
        List<ParkVisitor> list = jdbcTemplate.query(sql, new ParkVisitorRowMapper(), username);
        return list.size() > 0;
    }
}
