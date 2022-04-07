package com.example.walkddog_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

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
        String path = "/dog/all";
        String res = testRestTemplate.getForObject(url + port + path, String.class);
        System.out.println(res);
//        assertThat(testRestTemplate.getForObject(url + port + path, String.class)).contains("Dogs");
    }

}
