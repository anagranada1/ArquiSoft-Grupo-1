package com.udea.grupo1.acceptance;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @BeforeEach
    void setServerPort() {
        System.setProperty("server.port", String.valueOf(serverPort));
    }

    private Results runWithTag(String tag) {
        return Runner.path("classpath:karate/weather.feature")
                .tags(tag)
                .parallel(1);
    }

    @Nested
    @DisplayName("All acceptance scenarios")
    class AllScenarios {

        @Test
        @DisplayName("Run all weather acceptance tests")
        void runAllWeatherAcceptanceTests() {
            Results results = Runner.path("classpath:karate/weather.feature").parallel(1);
            assertEquals(0, results.getFailCount(), results.getErrorMessages());
        }
    }

    @Nested
    @DisplayName("Individual acceptance scenarios")
    class IndividualScenarios {

        @Test
        @DisplayName("Create weather (POST 201)")
        void runCreateWeatherOk() {
            Results results = runWithTag("@create-weather-ok");
            assertEquals(0, results.getFailCount(), results.getErrorMessages());
        }

        @Test
        @DisplayName("Create weather 400 when country missing")
        void runCreate400Country() {
            Results results = runWithTag("@create-400-country");
            assertEquals(0, results.getFailCount(), results.getErrorMessages());
        }

        @Test
        @DisplayName("Create weather 400 when city missing")
        void runCreate400City() {
            Results results = runWithTag("@create-400-city");
            assertEquals(0, results.getFailCount(), results.getErrorMessages());
        }

        @Test
        @DisplayName("Search weather by country and city (GET 200)")
        void runSearchWeather() {
            Results results = runWithTag("@search-weather");
            assertEquals(0, results.getFailCount(), results.getErrorMessages());
        }

        @Test
        @DisplayName("Search with encoded city name")
        void runSearchEncoded() {
            Results results = runWithTag("@search-encoded");
            assertEquals(0, results.getFailCount(), results.getErrorMessages());
        }
    }
}
