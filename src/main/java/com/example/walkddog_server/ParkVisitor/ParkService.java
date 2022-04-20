package com.example.walkddog_server.ParkVisitor;

import com.example.walkddog_server.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ParkService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ParkVisitor> getAllParkVisitors(String park_id) {
        return jdbcTemplate.query("SELECT * FROM park_visitor WHERE park_id = '" + park_id + "'", (rs, rowNum) -> new ParkVisitor(
                rs.getString("park_id"),
                rs.getString("visitor_name"),
                rs.getString("dog_id")));
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
}
