package com.example.walkddog_server;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WalkDDogServerApplicationTests {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    String url = "http://localhost:";


    @Test
    void contextLoads() {
        assertThat(testRestTemplate).isNotNull();
    }

    @Test
    void dogTest() {
        String path = "/dog";
        assertThat(testRestTemplate.getForObject(url + port + path, String.class)).contains("Dog");
    }

    @Test
    void allDogsTest() {
        System.out.println("allDogsTest");
        String path = "/dog/all";
        String res = testRestTemplate.getForObject(url + port + path, String.class);
        System.out.println(res);
//        assertThat(testRestTemplate.getForObject(url + port + path, String.class)).contains("Dogs");
    }

    @Test
    void addDogTest() throws JSONException {
        System.out.println("addDogTest");
        String path = "/dog/add";
        JSONObject json = new JSONObject();
        json.put("dog_name", "Genie");
        json.put("dog_age", "14");
        json.put("dog_owner", "Applebeef");

        HttpEntity<String> request = new HttpEntity<>(json.toString());
        testRestTemplate.postForObject(url + port + path, request, String.class);

        String allDogsPath = "/dog/all";
        String res = testRestTemplate.getForObject(url + port + allDogsPath, String.class);
        System.out.println(res);
//        assertThat(res).contains("Genie");
    }

    @Test
    void deleteDogTest() throws JSONException {
        System.out.println("deleteDogTest");
        String path = "/dog/delete";
        JSONObject json = new JSONObject();
        json.put("dog_name", "Genie");
        json.put("dog_age", "14");
        json.put("dog_owner", "Applebeef");

        HttpEntity<String> request = new HttpEntity<>(json.toString());
        testRestTemplate.postForObject(url + port + path, request, String.class);

        String allDogsPath = "/dog/all";
        String res = testRestTemplate.getForObject(url + port + allDogsPath, String.class);
        System.out.println(res);
//        assertThat(res).doesNotContain("Genie");
    }

    @Test
    void getUserByUsernameTest() {
        System.out.println("getUserByUsernameTest");
        String path = "/user/byusername/";
        String username = "Applebeef";
        String res = testRestTemplate.getForObject(url + port + path + username, String.class);
        System.out.println(res);
        assertThat(res).contains("Othnay");
    }

}
