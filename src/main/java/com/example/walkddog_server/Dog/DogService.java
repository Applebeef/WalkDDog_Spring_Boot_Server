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

    public List<Dog> getDogsByOwner(String owner) {
        return jdbcTemplate.query("select * from dog where dog_owner = ?", (rs, rowNum) -> new Dog(
                rs.getInt("dog_id"),
                rs.getString("dog_name"),
                rs.getInt("dog_age"),
                rs.getString("dog_owner")
        ), owner);
    }

    public int insertDog(Dog dog) {
        return jdbcTemplate.update("insert into dog (dog_name, dog_age, dog_gender, dog_owner) values (?, ?, ?, ?)",
                dog.getName(), dog.getAge(), dog.getGender(), dog.getOwner());
    }

    public int deleteDog(long dog_id) {
        return jdbcTemplate.update("delete from dog where dog_id = ? ", dog_id);
    }
}
