package ru.yandex.practicum.Filmorate.contoller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.Filmorate.FilmorateApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureMockMvc
 class MpaControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("получение всех рейтингов.")
    void getAllMpa_ShouldReturnList() throws Exception {
        mvc.perform(get("/mpa"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("G"));
    }

    @Test
    @DisplayName("получение рейтингов по id.")
     void getMpa_ByValidId_ShouldReturnMpa() throws Exception {
        mvc.perform(get("/mpa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("G"));

        mvc.perform(get("/mpa/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("PG"));

        mvc.perform(get("/mpa/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("PG-13"));
    }

    @Test
    @DisplayName("получение рейтингов по не существующему id.")
     void getMpa_ByInvalidId_ShouldReturn404() throws Exception {
        mvc.perform(get("/mpa/9999"))
                .andExpect(status().isNotFound());
    }
}
