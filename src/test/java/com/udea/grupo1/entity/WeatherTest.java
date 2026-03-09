package com.udea.grupo1.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Weather entity")
class WeatherTest {

    @Nested
    @DisplayName("getters and setters")
    class GettersAndSetters {

        @Test
        @DisplayName("all fields are read and written correctly (simple)")
        void allFieldsAreReadAndWrittenCorrectly() {

            Weather weather = new Weather();
            String id = "id-123";
            String country = "Colombia";
            String city = "Medellín";
            double temperature = 22.5;
            double feelsLike = 23.0;
            int humidity = 70;
            String description = "Partly cloudy";
            double windSpeed = 5.2;
            LocalDateTime registrationDate = LocalDateTime.now();

            weather.setId(id);
            weather.setCountry(country);
            weather.setCity(city);
            weather.setTemperature(temperature);
            weather.setFeelsLike(feelsLike);
            weather.setHumidity(humidity);
            weather.setDescription(description);
            weather.setWindSpeed(windSpeed);
            weather.setRegistrationDate(registrationDate);

            assertThat(weather.getId()).isEqualTo(id);
            assertThat(weather.getCountry()).isEqualTo(country);
            assertThat(weather.getCity()).isEqualTo(city);
            assertThat(weather.getTemperature()).isEqualTo(temperature);
            assertThat(weather.getFeelsLike()).isEqualTo(feelsLike);
            assertThat(weather.getHumidity()).isEqualTo(humidity);
            assertThat(weather.getDescription()).isEqualTo(description);
            assertThat(weather.getWindSpeed()).isEqualTo(windSpeed);
            assertThat(weather.getRegistrationDate()).isEqualTo(registrationDate);
        }

        @Test
        @DisplayName("default constructor produces object with null id and optional fields (simple)")
        void defaultConstructorProducesObjectWithNullId() {
            Weather weather = new Weather();

            assertThat(weather.getId()).isNull();
            assertThat(weather.getCountry()).isNull();
            assertThat(weather.getCity()).isNull();
            assertThat(weather.getTemperature()).isEqualTo(0.0);
            assertThat(weather.getHumidity()).isEqualTo(0);
            assertThat(weather.getRegistrationDate()).isNull();
        }
    }
}
