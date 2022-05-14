package com.example.walkddog_server.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    public SimpleUserInfo getSimpleUserInfo(String username){
        return new SimpleUserInfo(getUser(username));
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

    public List<String> getFriends(String username) {
        final String sql = "SELECT * FROM friend WHERE username1 = ? OR username2 = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        rs.getString("username1").equals(username) ?
                                rs.getString("username2") : rs.getString("username1"),
                username, username);
    }

    /**
     * @param sender   username of the user who is sending the request
     * @param receiver username of the user who is receiving the request
     * @return false if there is no request already send from the receiver to the sender
     * true if there is a request already sent, from the receiver to the sender, and the friend request is accepted
     */
    public boolean addFriendRequest(String sender, String receiver) {
        final String addRequestSql = "INSERT INTO friend_request (sender, receiver) VALUES (?, ?)";
        final String checkRequestSql = "SELECT count(*) FROM friend_request WHERE sender = ? AND receiver = ?";

        Integer count = jdbcTemplate.queryForObject(checkRequestSql, Integer.class, receiver, sender);
        if (count == null || count.equals(0)) {
            jdbcTemplate.update(addRequestSql, sender, receiver);
            return false;
        } else {
            addFriend(sender, receiver);
            removeFriendRequest(sender, receiver);
            return true;
        }
    }

    private void addFriend(String username1, String username2) {
        final String sql = "INSERT INTO friend (username1, username2) VALUES (?, ?)";
        jdbcTemplate.update(sql, username1, username2);
    }

    public void removeFriendRequest(String sender, String receiver) {
        final String sql = "DELETE FROM friend_request WHERE sender = ? AND receiver = ?";
        jdbcTemplate.update(sql, sender, receiver);
    }
}
