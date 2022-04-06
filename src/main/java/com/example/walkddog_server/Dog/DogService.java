package com.example.walkddog_server.Dog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DogService {

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public DogService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Dog> getAllDogs() {
        return jdbcTemplate.query("select * from dog", (rs, rowNum) -> new Dog(
                rs.getInt("dog_id"),
                rs.getString("dog_name"),
                rs.getInt("dog_age"),
                rs.getString("dog_owner")
        ));
    }
}
