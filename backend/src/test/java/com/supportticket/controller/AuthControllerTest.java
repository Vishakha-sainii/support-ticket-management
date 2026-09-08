package com.supportticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supportticket.repository.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void adminLoginSucceeds() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .session(new MockHttpSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "admin",
                                  "password": "admin123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void userLoginSucceeds() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .session(new MockHttpSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "user",
                                  "password": "user123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void invalidCredentialsAreRejected() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .session(new MockHttpSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "nobody",
                                  "password": "wrong-password"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("AUTHENTICATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void meReturnsCurrentUserWhenAuthenticated() throws Exception {
        MockHttpSession session = loginAs("admin", "admin123");

        mockMvc.perform(get("/api/auth/me").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void meReturns401WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("AUTHENTICATION_ERROR"));
    }

    @Test
    void logoutInvalidatesSession() throws Exception {
        MockHttpSession session = loginAs("admin", "admin123");

        mockMvc.perform(post("/api/auth/logout").session(session))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/auth/me").session(session))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthenticatedTicketAccessReturns401() throws Exception {
        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("AUTHENTICATION_ERROR"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void userCannotCreateTicket() throws Exception {
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Forbidden ticket",
                                  "description": "Should not be created",
                                  "priority": "HIGH",
                                  "assignee": "support-user"
                                }
                                """))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value("You do not have permission to perform this action"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void adminCanCreateTicket() throws Exception {
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Allowed ticket",
                                  "description": "Created by admin",
                                  "priority": "HIGH",
                                  "assignee": "support-user"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Allowed ticket"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void userCanListAndUpdateTickets() throws Exception {
        mockMvc.perform(post("/api/tickets")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Shared ticket",
                                  "description": "Visible to user",
                                  "priority": "LOW",
                                  "assignee": "support-user"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/tickets").with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Shared ticket"));

        long ticketId = objectMapper.readTree(
                mockMvc.perform(get("/api/tickets").with(user("user").roles("USER")))
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        ).get(0).get("id").asLong();

        mockMvc.perform(put("/api/tickets/" + ticketId)
                        .with(user("user").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Updated by user",
                                  "description": "Updated description",
                                  "priority": "MEDIUM",
                                  "assignee": "new-assignee"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated by user"));
    }

    private MockHttpSession loginAs(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .session(new MockHttpSession())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                  "username": "%s",
                                  "password": "%s"
                                }
                                """, username, password)))
                .andExpect(status().isOk())
                .andReturn();
        return (MockHttpSession) result.getRequest().getSession();
    }
}
