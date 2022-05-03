package com.example.walkddog_server.Key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

@Service
public class KeyService {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public KeyService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getKey() {
        return jdbcTemplate.queryForObject("SELECT api_key FROM google_key", String.class);
    }
}
