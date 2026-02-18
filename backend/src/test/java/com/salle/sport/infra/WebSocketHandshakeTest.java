package com.salle.sport.infra;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketHandshakeTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void infoEndpointShouldReturn200() {
        String url = "http://localhost:" + port + "/ws/info";
        ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
        System.out.println("/ws/info response status=" + resp.getStatusCode() + " body=" + resp.getBody());
        assertEquals(HttpStatus.OK, resp.getStatusCode());
    }
}
