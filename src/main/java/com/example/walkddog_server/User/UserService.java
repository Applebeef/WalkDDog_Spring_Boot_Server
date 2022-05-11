package com.example.walkddog_server.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("first_name"),
                    rs.getString("last_name"));
        }
    }


    @Autowired
    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getAllUsers() {
        final String Sql = "select * from user";
        return jdbcTemplate.query(Sql, new UserRowMapper());
    }

    public User getUser(String username) {
        final String Sql = "select * from user where username = ?";
        return jdbcTemplate.queryForObject(Sql, new UserRowMapper(), username);
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

    public boolean validateUser(String username) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from user where username = ?", Integer.class, username);
        return count != null && count.equals(0);
    }
}
