package com.udea.grupo1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udea.grupo1.entity.Weather;
import com.udea.grupo1.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WeatherController")
class WeatherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    @Nested
    @DisplayName("POST /api/v1/clima")
    class CreateWeather {

        @Test
        @DisplayName("returns 201 and body with created weather when country and city are provided (simple)")
        void returns201AndBodyWhenValidRequest() throws Exception {
            Weather request = new Weather();
            request.setCountry("Colombia");
            request.setCity("Medellín");
            request.setTemperature(23.5);
            request.setHumidity(60);
            request.setDescription("Sunny");

            Weather saved = new Weather();
            saved.setId("generated-id");
            saved.setCountry(request.getCountry());
            saved.setCity(request.getCity());
            saved.setTemperature(request.getTemperature());
            saved.setHumidity(request.getHumidity());
            saved.setDescription(request.getDescription());
            when(weatherService.saveWeather(any(Weather.class))).thenReturn(saved);

            mockMvc.perform(post("/api/v1/clima")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value("generated-id"))
                    .andExpect(jsonPath("$.pais").value("Colombia"))
                    .andExpect(jsonPath("$.ciudad").value("Medellín"));
        }

        @Test
        @DisplayName("returns 400 when country is null (complex)")
        void returns400WhenCountryIsNull() throws Exception {
            Weather request = new Weather();
            request.setCountry(null);
            request.setCity("Bogotá");

            mockMvc.perform(post("/api/v1/clima")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("País y ciudad son obligatorios")));
        }

        @Test
        @DisplayName("returns 400 when city is null (complex)")
        void returns400WhenCityIsNull() throws Exception {
            Weather request = new Weather();
            request.setCountry("Colombia");
            request.setCity(null);

            mockMvc.perform(post("/api/v1/clima")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("País y ciudad son obligatorios")));
        }

        @Test
        @DisplayName("response body echoes created country and city (complex)")
        void responseIncludesHateoasLinks() throws Exception {
            Weather request = new Weather();
            request.setCountry("Peru");
            request.setCity("Lima");
            Weather saved = new Weather();
            saved.setId("id-1");
            saved.setCountry("Peru");
            saved.setCity("Lima");
            when(weatherService.saveWeather(any(Weather.class))).thenReturn(saved);

            mockMvc.perform(post("/api/v1/clima")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.pais").value("Peru"))
                    .andExpect(jsonPath("$.ciudad").value("Lima"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/clima/buscar")
    class SearchWeather {

        @Test
        @DisplayName("returns 200 and list of weather records (simple)")
        void returns200AndListWhenResultsExist() throws Exception {
            Weather w = new Weather();
            w.setId("id-1");
            w.setCountry("Colombia");
            w.setCity("Cali");
            w.setTemperature(28.0);
            when(weatherService.findByCountryAndCity("Colombia", "Cali")).thenReturn(List.of(w));

            mockMvc.perform(get("/api/v1/clima/buscar")
                            .param("pais", "Colombia")
                            .param("ciudad", "Cali"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].id").value("id-1"))
                    .andExpect(jsonPath("$[0].pais").value("Colombia"))
                    .andExpect(jsonPath("$[0].ciudad").value("Cali"))
                    .andExpect(jsonPath("$[0].temperatura").value(28.0));
        }

        @Test
        @DisplayName("returns 200 and empty array when no matches (complex)")
        void returns200AndEmptyArrayWhenNoMatches() throws Exception {
            when(weatherService.findByCountryAndCity("Chile", "Santiago")).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/clima/buscar")
                            .param("pais", "Chile")
                            .param("ciudad", "Santiago"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }
}
