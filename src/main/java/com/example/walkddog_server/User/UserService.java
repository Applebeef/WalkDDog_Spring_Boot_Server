package com.example.walkddog_server.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("select * from user", (rs, rowNum) -> new User(
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("first_name"),
                rs.getString("last_name")));
    }

    public User getUser(String username) {
        return jdbcTemplate.query("select * from user where username = ?", (rs, rowNum) -> new User(
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("first_name"),
                rs.getString("last_name")), username).get(0);
    }

    public int registerUser(User user) {
        return jdbcTemplate.update("insert into user (username, password, email, first_name, last_name) values (?, ?, ?, ?, ?)",
                user.getUsername(), user.getPassword(), user.getEmail(), user.getFirst_name(), user.getLast_name());
    }

    public int deleteUser(String username) {
        return jdbcTemplate.update("delete from user where username = ?", username);
    }


    public boolean loginUser(String username, String pass) {
        return getUser(username).getPassword().equals(pass);
    }
}
