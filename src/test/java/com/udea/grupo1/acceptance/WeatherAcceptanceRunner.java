package com.udea.grupo1.acceptance;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class WeatherAcceptanceRunner {

    @LocalServerPort
    private int serverPort;

    @Test
    void runWeatherAcceptanceTests() {
        System.setProperty("server.port", String.valueOf(serverPort));

        Results results = Runner.path("classpath:karate/weather.feature").parallel(1);

        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
