package com.example.walkddog_server.ParkVisitor;

import com.example.walkddog_server.Dog.DogService;
import com.example.walkddog_server.Dog.SimpleDogInfo;
import com.example.walkddog_server.User.UserService;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushMessageTicketPair;
import io.github.jav.exposerversdk.ExpoPushReceipt;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;

@Service
public class ParkService {

    private final JdbcTemplate jdbcTemplate;
    private final DogService dogService;
    private final UserService userService;

    public void removeExpiredParkVisitors() {
        jdbcTemplate.update("delete from park_visitor where expiration_time < now()");
    }

    private static class ParkVisitorRowMapper implements RowMapper<ParkVisitor> {
        @Override
        public ParkVisitor mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ParkVisitor(rs.getString("park_id"),
                    rs.getString("visitor_name"),
                    rs.getString("dog_id"),
                    rs.getString("dog_name"),
                    rs.getTimestamp("expiration_time"));
        }
    }

    @Autowired
    public ParkService(JdbcTemplate jdbcTemplate, DogService dogService, UserService userService) {
        this.jdbcTemplate = jdbcTemplate;
        this.dogService = dogService;
        this.userService = userService;
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

    public boolean checkIfCurrentlyInPark(String username) {
        final String sql = "SELECT * FROM park_visitor WHERE visitor_name = ?";
        List<ParkVisitor> list = jdbcTemplate.query(sql, (rs, rowNum) -> new ParkVisitor(rs.getString("park_id"),
                rs.getString("visitor_name"),
                rs.getString("dog_id"),
                rs.getTimestamp("expiration_time")), username);
        return !list.isEmpty();
    }

    public void sendNotificationToParkVisitorFriends(String park_id, String username) {
        List<String> friends = userService.getFriends(username);
        List<String> tokens = new ArrayList<>();
        for (String friend : friends) {
            tokens.addAll(jdbcTemplate.query("SELECT push_token FROM user WHERE username = ?",
                    (rs, rowNum) -> rs.getString("push_token"), friend));
        }
        tokens.forEach(token -> {
            sendExpoNotification(token, "Friend visiting park.",
                    "Your friend " + username + " is currently in the park " + park_id);
        });
    }

    public void sendExpoNotification(String expoToken, String title, String message) {

        if (!PushClient.isExponentPushToken(expoToken))
            throw new Error("Token:" + expoToken + " is not a valid token.");

        ExpoPushMessage expoPushMessage = new ExpoPushMessage();
        expoPushMessage.getTo().add(expoToken);
        expoPushMessage.setTitle(title);
        expoPushMessage.setBody(message);

        List<ExpoPushMessage> expoPushMessages = new ArrayList<>();
        expoPushMessages.add(expoPushMessage);

        PushClient client;
        try {
            client = new PushClient();
        } catch (PushClientException e) {
            throw new RuntimeException(e);
        }
        List<List<ExpoPushMessage>> chunks = client.chunkPushNotifications(expoPushMessages);

        List<CompletableFuture<List<ExpoPushTicket>>> messageRepliesFutures = new ArrayList<>();

        for (List<ExpoPushMessage> chunk : chunks) {
            messageRepliesFutures.add(client.sendPushNotificationsAsync(chunk));
        }

        // Wait for each completable future to finish
        List<ExpoPushTicket> allTickets = new ArrayList<>();
        for (CompletableFuture<List<ExpoPushTicket>> messageReplyFuture : messageRepliesFutures) {
            try {
                for (ExpoPushTicket ticket : messageReplyFuture.get()) {
                    allTickets.add(ticket);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
