package com.udea.grupo1.service;

import com.udea.grupo1.entity.Weather;
import com.udea.grupo1.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WeatherService")
class WeatherServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private WeatherService weatherService;

    @Nested
    @DisplayName("saveWeather")
    class SaveWeather {

        @Test
        @DisplayName("sets registration date and returns saved entity (simple)")
        void setsRegistrationDateAndReturnsSavedEntity() {
            Weather input = new Weather();
            input.setCountry("Colombia");
            input.setCity("Medellín");
            input.setTemperature(22.5);
            input.setRegistrationDate(null);

            Weather saved = new Weather();
            saved.setId("id-123");
            saved.setCountry(input.getCountry());
            saved.setCity(input.getCity());
            saved.setTemperature(input.getTemperature());
            when(weatherRepository.save(any(Weather.class))).thenReturn(saved);

            Weather result = weatherService.saveWeather(input);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo("id-123");
            assertThat(input.getRegistrationDate()).isNotNull();
            assertThat(input.getRegistrationDate()).isBefore(LocalDateTime.now().plusSeconds(1));
        }

        @Test
        @DisplayName("invokes repository save exactly once with same instance (complex)")
        void invokesRepositorySaveExactlyOnceWithSameInstance() {
            Weather weather = new Weather();
            weather.setCountry("Mexico");
            weather.setCity("CDMX");
            weather.setTemperature(18.0);
            when(weatherRepository.save(same(weather))).thenReturn(weather);

            weatherService.saveWeather(weather);

            ArgumentCaptor<Weather> captor = ArgumentCaptor.forClass(Weather.class);
            verify(weatherRepository, times(1)).save(captor.capture());
            assertThat(captor.getValue()).isSameAs(weather);
            assertThat(captor.getValue().getRegistrationDate()).isNotNull();
        }
    }

    @Nested
    @DisplayName("findByCountryAndCity")
    class FindByCountryAndCity {

        @Test
        @DisplayName("returns empty list when no results (simple)")
        void returnsEmptyListWhenNoResults() {
            String country = "Argentina";
            String city = "Buenos Aires";
            when(weatherRepository.findByCountryAndCity(eq(country), eq(city)))
                    .thenReturn(Collections.emptyList());

            List<Weather> result = weatherService.findByCountryAndCity(country, city);

            assertThat(result).isNotNull().isEmpty();
            verify(weatherRepository, times(1)).findByCountryAndCity(country, city);
        }

        @Test
        @DisplayName("returns all matching records for country and city (complex)")
        void returnsAllMatchingRecordsForCountryAndCity() {
            String country = "Colombia";
            String city = "Bogotá";
            Weather w1 = new Weather();
            w1.setId("1");
            w1.setCountry(country);
            w1.setCity(city);
            w1.setTemperature(15.0);
            Weather w2 = new Weather();
            w2.setId("2");
            w2.setCountry(country);
            w2.setCity(city);
            w2.setTemperature(16.0);
            List<Weather> stored = List.of(w1, w2);
            when(weatherRepository.findByCountryAndCity(eq(country), eq(city))).thenReturn(stored);

            List<Weather> result = weatherService.findByCountryAndCity(country, city);

            assertThat(result).hasSize(2);
            assertThat(result).extracting(Weather::getId).containsExactly("1", "2");
            assertThat(result).extracting(Weather::getTemperature).containsExactly(15.0, 16.0);
        }
    }
}
