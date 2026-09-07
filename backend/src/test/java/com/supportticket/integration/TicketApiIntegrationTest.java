package com.supportticket.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supportticket.entity.Status;
import com.supportticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TicketApiIntegrationTest {

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

    @Test
    void fullTicketLifecycle() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Lifecycle ticket",
                                  "description": "Integration flow",
                                  "priority": "HIGH",
                                  "assignee": "support-user"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andReturn();

        long ticketId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asLong();

        mockMvc.perform(get("/api/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Lifecycle ticket"));

        mockMvc.perform(put("/api/tickets/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Updated lifecycle ticket",
                                  "description": "Updated integration flow",
                                  "priority": "MEDIUM",
                                  "assignee": "new-assignee"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated lifecycle ticket"));

        mockMvc.perform(post("/api/tickets/{id}/comments", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\": \"Working on it.\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(patch("/api/tickets/{id}/status", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        mockMvc.perform(get("/api/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.assignee").value("new-assignee"))
                .andExpect(jsonPath("$.comments[0].text").value("Working on it."));
    }

    @Test
    void persistedTicketRemainsAvailableAfterSubsequentRead() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Persistent ticket",
                                  "description": "Should survive reload",
                                  "priority": "LOW",
                                  "assignee": "support-user"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = objectMapper.readTree(createResult.getResponse().getContentAsString());
        long ticketId = body.get("id").asLong();

        assertThat(ticketRepository.findById(ticketId)).isPresent();
        assertThat(ticketRepository.findById(ticketId).orElseThrow().getTitle())
                .isEqualTo("Persistent ticket");

        mockMvc.perform(get("/api/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Persistent ticket"));
    }

    @Test
    void createTicketRejectsTitleExceedingMaxLength() throws Exception {
        String longTitle = "a".repeat(201);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                  "title": "%s",
                                  "description": "Description",
                                  "priority": "HIGH",
                                  "assignee": "support-user"
                                }
                                """, longTitle)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void addCommentRejectsBlankText() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Title",
                                  "description": "Description",
                                  "priority": "HIGH",
                                  "assignee": "support-user"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        long ticketId = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asLong();

        mockMvc.perform(post("/api/tickets/{id}/comments", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void listTicketsReturnsEmptyArrayWhenNoMatches() throws Exception {
        mockMvc.perform(get("/api/tickets").param("status", Status.OPEN.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
