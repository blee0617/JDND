package com.example.demo.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void unauthenticated_users_test() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/cart")).andExpect(status().isUnauthorized());
    }

    @Test
    public void non_existent_user_test() throws Exception {
        String username = "Brian1";
        String password = "TWN!sH0t";

        String body = "{\"username\":\"" + username + "\", \"password\":\" " + password + "\"}";

        mvc.perform(MockMvcRequestBuilders.post("/api/user/Brian")
                        .content(body))
                .andExpect(status().isUnauthorized()).andReturn();
    }

}
