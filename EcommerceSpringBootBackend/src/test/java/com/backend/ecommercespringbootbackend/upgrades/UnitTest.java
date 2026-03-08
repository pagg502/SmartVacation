package com.backend.ecommercespringbootbackend.upgrades;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UnitTest {
    @Autowired
    private MockMvc mockMvc;
    long timestamp = System.currentTimeMillis();

    //Test Signup, expects server confirmation response: Customer saved and 200 status code
    @Test
    void testSignup() throws Exception {
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "firstName": "John",
                      "lastName": "D",
                      "email": "%s",
                      "password": "123456",
                      "address": "18546 Main rd",
                      "postal_code": "12809",
                      "phone": "8014653297",
                      "country": "U.S",
                      "division": 2
                    }
                    """.formatted(timestamp + "signup@test.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Response").value("Customer saved"));
    }

    @Test
    void testLoginAndLogout() throws Exception {
        //Login
        //Test login with existent user added automatically during StartUpCommandLineRunner
        MvcResult loginResult = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "grace@gmail.com",
                          "password": "123456"
                        }
                    """))
                .andExpect(status().isOk())
                .andReturn();

        //Extract the session created during login
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();

        //Logout using the same session
        mockMvc.perform(delete("/api/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Response").value("Logout Successful"));
    }
}
