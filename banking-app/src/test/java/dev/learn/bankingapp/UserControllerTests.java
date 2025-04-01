package dev.learn.bankingapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.learn.bankingapp.constants.ROLE;
import dev.learn.bankingapp.entity.User;
import dev.learn.bankingapp.reposiotry.UserRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    void shouldRegisterUserSuccessfully() throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", "user");
        body.put("password", "password");
        body.put("email", "user@gmail.com");
        body.put("role", "CUSTOMER");

        mvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.email").value("user@gmail.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }

    @Test
    @Order(2)
    void shouldReturnConflictForDuplicateUser() throws Exception {
        User user = new User("user", "password", "user2@gmail.com", ROLE.CUSTOMER);

        mvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(3)
    void shouldReturnBadRequestForEmptyBody() throws Exception {
        mvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    void shouldReturnBadRequestForEmptyFeild() throws Exception {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", "user");
        body.put("password", "password");
        body.put("email", "");
        body.put("role", "CUSTOMER");

        mvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void shouldFindUserByUsername() throws Exception {
        User user = new User("user1", "password", "user1@gmail.com", ROLE.CUSTOMER);
        userRepository.save(user);

        mvc.perform(get("/api/v1/users/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@gmail.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }

    @Test
    @Order(6)
    void shouldReturnNotFoundForNonExistentUser() throws Exception {
        mvc.perform(get("/api/v1/users/unknown_user"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    void shouldDeleteUserSuccessfully() throws Exception {
        mvc.perform(delete("/api/v1/users/user1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(8)
    void shouldReturnNotFoundForNonExistentUserWhenDeleted() throws Exception {
        mvc.perform(delete("/api/v1/users/uknown_user"))
                .andExpect(status().isNotFound());
    }

}
