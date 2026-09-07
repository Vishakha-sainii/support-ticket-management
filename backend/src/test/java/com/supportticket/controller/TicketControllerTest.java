package com.supportticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supportticket.dto.CreateCommentRequest;
import com.supportticket.dto.CreateTicketRequest;
import com.supportticket.dto.UpdateStatusRequest;
import com.supportticket.entity.Priority;
import com.supportticket.entity.Status;
import com.supportticket.entity.Ticket;
import com.supportticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TicketControllerTest {

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
    void createTicketReturnsOpenStatus() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest(
                "Unable to login",
                "User cannot login",
                Priority.HIGH,
                "support-user"
        );

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Unable to login"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.assignee").value("support-user"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists())
                .andExpect(jsonPath("$.comments").isArray());
    }

    @Test
    void createTicketValidationErrorWhenTitleBlank() throws Exception {
        String body = """
                {
                  "title": "",
                  "description": "Description",
                  "priority": "HIGH",
                  "assignee": "support-user"
                }
                """;

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void listTicketsReturnsRequiredFields() throws Exception {
        createSampleTicket("Login issue", "Cannot login", Priority.HIGH, Status.OPEN);

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].title").value("Login issue"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"))
                .andExpect(jsonPath("$[0].status").value("OPEN"))
                .andExpect(jsonPath("$[0].assignee").value("support-user"))
                .andExpect(jsonPath("$[0].createdAt").exists());
    }

    @Test
    void getTicketReturnsDetailsAndComments() throws Exception {
        long ticketId = createSampleTicket("Login issue", "Cannot login", Priority.HIGH, Status.OPEN);

        CreateCommentRequest commentRequest = new CreateCommentRequest("Investigating the issue.");
        mockMvc.perform(post("/api/tickets/{id}/comments", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Cannot login"))
                .andExpect(jsonPath("$.comments", hasSize(1)))
                .andExpect(jsonPath("$.comments[0].text").value("Investigating the issue."))
                .andExpect(jsonPath("$.comments[0].createdAt").exists());
    }

    @Test
    void getTicketNotFoundReturns404() throws Exception {
        mockMvc.perform(get("/api/tickets/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Ticket not found"));
    }

    @Test
    void updateTicketPersistsChanges() throws Exception {
        long ticketId = createSampleTicket("Old title", "Old description", Priority.LOW, Status.OPEN);

        String body = """
                {
                  "title": "Updated title",
                  "description": "Updated description",
                  "priority": "MEDIUM",
                  "assignee": "new-assignee"
                }
                """;

        mockMvc.perform(put("/api/tickets/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.assignee").value("new-assignee"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void updateTicketNotFoundReturns404() throws Exception {
        String body = """
                {
                  "title": "Updated title",
                  "description": "Updated description",
                  "priority": "MEDIUM",
                  "assignee": "new-assignee"
                }
                """;

        mockMvc.perform(put("/api/tickets/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void updateTicketRejectsStatusField() throws Exception {
        long ticketId = createSampleTicket("Title", "Description", Priority.HIGH, Status.OPEN);

        String body = """
                {
                  "title": "Updated title",
                  "description": "Updated description",
                  "priority": "MEDIUM",
                  "assignee": "new-assignee",
                  "status": "CLOSED"
                }
                """;

        mockMvc.perform(put("/api/tickets/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value(
                        "Status cannot be changed through ticket update; use PATCH /api/tickets/{id}/status"));

        mockMvc.perform(get("/api/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void addCommentPersistsComment() throws Exception {
        long ticketId = createSampleTicket("Title", "Description", Priority.HIGH, Status.OPEN);

        CreateCommentRequest request = new CreateCommentRequest("Investigating the issue.");

        mockMvc.perform(post("/api/tickets/{id}/comments", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.ticketId").value(ticketId))
                .andExpect(jsonPath("$.text").value("Investigating the issue."))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void addCommentNotFoundReturns404() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest("Investigating the issue.");

        mockMvc.perform(post("/api/tickets/{id}/comments", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void searchTicketsByKeyword() throws Exception {
        createSampleTicket("Login issue", "User cannot login", Priority.HIGH, Status.OPEN);
        createSampleTicket("Billing issue", "Payment failed", Priority.LOW, Status.OPEN);

        mockMvc.perform(get("/api/tickets").param("search", "login"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Login issue"));
    }

    @Test
    void filterTicketsByStatus() throws Exception {
        createSampleTicket("Open ticket", "Description", Priority.HIGH, Status.OPEN);
        createSampleTicket("Closed ticket", "Description", Priority.LOW, Status.CLOSED);

        mockMvc.perform(get("/api/tickets").param("status", "OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("OPEN"));
    }

    @Test
    void filterTicketsBySearchAndStatus() throws Exception {
        createSampleTicket("Login issue", "Cannot login", Priority.HIGH, Status.OPEN);
        createSampleTicket("Login closed", "Cannot login", Priority.HIGH, Status.CLOSED);

        mockMvc.perform(get("/api/tickets")
                        .param("search", "login")
                        .param("status", "OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Login issue"));
    }

    @Test
    void invalidStatusFilterReturns400() throws Exception {
        mockMvc.perform(get("/api/tickets").param("status", "INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void transitionOpenToInProgressSucceeds() throws Exception {
        long ticketId = createSampleTicket("Title", "Description", Priority.HIGH, Status.OPEN);

        performStatusTransition(ticketId, Status.IN_PROGRESS)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void transitionInProgressToResolvedSucceeds() throws Exception {
        long ticketId = createSampleTicket("Title", "Description", Priority.HIGH, Status.IN_PROGRESS);

        performStatusTransition(ticketId, Status.RESOLVED)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESOLVED"));
    }

    @Test
    void transitionResolvedToClosedSucceeds() throws Exception {
        long ticketId = createSampleTicket("Title", "Description", Priority.HIGH, Status.RESOLVED);

        performStatusTransition(ticketId, Status.CLOSED)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    void transitionOpenToCancelledSucceeds() throws Exception {
        long ticketId = createSampleTicket("Title", "Description", Priority.HIGH, Status.OPEN);

        performStatusTransition(ticketId, Status.CANCELLED)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void transitionInProgressToCancelledSucceeds() throws Exception {
        long ticketId = createSampleTicket("Title", "Description", Priority.HIGH, Status.IN_PROGRESS);

        performStatusTransition(ticketId, Status.CANCELLED)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void transitionClosedToOpenIsRejectedAndStatusUnchanged() throws Exception {
        long ticketId = createSampleTicket("Title", "Description", Priority.HIGH, Status.CLOSED);

        performStatusTransition(ticketId, Status.OPEN)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("INVALID_STATE_TRANSITION"))
                .andExpect(jsonPath("$.message").value("Cannot transition from CLOSED to OPEN"));

        mockMvc.perform(get("/api/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    void transitionResolvedToOpenIsRejected() throws Exception {
        long ticketId = createSampleTicket("Title", "Description", Priority.HIGH, Status.RESOLVED);

        performStatusTransition(ticketId, Status.OPEN)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("INVALID_STATE_TRANSITION"))
                .andExpect(jsonPath("$.message").value("Cannot transition from RESOLVED to OPEN"));
    }

    @Test
    void transitionCancelledToOpenIsRejected() throws Exception {
        long ticketId = createSampleTicket("Title", "Description", Priority.HIGH, Status.CANCELLED);

        performStatusTransition(ticketId, Status.OPEN)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("INVALID_STATE_TRANSITION"))
                .andExpect(jsonPath("$.message").value("Cannot transition from CANCELLED to OPEN"));
    }

    @Test
    void transitionStatusNotFoundReturns404() throws Exception {
        performStatusTransition(999, Status.IN_PROGRESS)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    private ResultActions performStatusTransition(
            long ticketId,
            Status status
    ) throws Exception {
        UpdateStatusRequest request = new UpdateStatusRequest(status);
        return mockMvc.perform(patch("/api/tickets/{id}/status", ticketId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    private long createSampleTicket(String title, String description, Priority priority, Status status) {
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setPriority(priority);
        ticket.setStatus(status);
        ticket.setAssignee("support-user");
        return ticketRepository.save(ticket).getId();
    }
}
