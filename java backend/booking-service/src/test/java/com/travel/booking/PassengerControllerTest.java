package com.travel.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.booking.controller.PassengerController;
import com.travel.booking.model.Passenger;
import com.travel.booking.service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for PassengerController using MockMvc.
 *
 * Tests all CRUD operations:
 *  - Create passenger (POST)
 *  - Get all passengers (GET)
 *  - Get passenger by ID (GET)
 *  - Update passenger (PUT)
 *  - Delete passenger (DELETE)
 */
@WebMvcTest(PassengerController.class)
@DisplayName("PassengerController Unit Tests")
public class PassengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassengerService passengerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Passenger samplePassenger;

    @BeforeEach
    void setUp() {
        samplePassenger = new Passenger(1L, "John Doe",
                "john.doe@email.com", "9876543210", "123 MG Road, Bangalore");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /api/passengers
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/passengers - should create passenger and return 201")
    void testCreatePassenger_Success() throws Exception {
        when(passengerService.createPassenger(any(Passenger.class))).thenReturn(samplePassenger);

        mockMvc.perform(post("/api/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(samplePassenger)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@email.com"))
                .andExpect(jsonPath("$.phone").value("9876543210"));

        verify(passengerService, times(1)).createPassenger(any(Passenger.class));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/passengers
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/passengers - should return all passengers with 200")
    void testGetAllPassengers_Success() throws Exception {
        Passenger p2 = new Passenger(2L, "Jane Smith",
                "jane.smith@email.com", "9123456789", "456 Koramangala");
        List<Passenger> passengers = Arrays.asList(samplePassenger, p2);
        when(passengerService.getAllPassengers()).thenReturn(passengers);

        mockMvc.perform(get("/api/passengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));

        verify(passengerService, times(1)).getAllPassengers();
    }

    @Test
    @DisplayName("GET /api/passengers - should return empty list when no passengers")
    void testGetAllPassengers_EmptyList() throws Exception {
        when(passengerService.getAllPassengers()).thenReturn(List.of());

        mockMvc.perform(get("/api/passengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/passengers/{id}
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/passengers/{id} - should return passenger when found")
    void testGetPassengerById_Found() throws Exception {
        when(passengerService.getPassengerById(1L)).thenReturn(Optional.of(samplePassenger));

        mockMvc.perform(get("/api/passengers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(passengerService, times(1)).getPassengerById(1L);
    }

    @Test
    @DisplayName("GET /api/passengers/{id} - should return 404 when not found")
    void testGetPassengerById_NotFound() throws Exception {
        when(passengerService.getPassengerById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/passengers/99"))
                .andExpect(status().isNotFound());

        verify(passengerService, times(1)).getPassengerById(99L);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PUT /api/passengers/{id}
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/passengers/{id} - should update and return 200")
    void testUpdatePassenger_Success() throws Exception {
        Passenger updated = new Passenger(1L, "John Updated",
                "john.updated@email.com", "9000000001", "789 New Address");
        when(passengerService.updatePassenger(eq(1L), any(Passenger.class)))
                .thenReturn(Optional.of(updated));

        mockMvc.perform(put("/api/passengers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@email.com"));
    }

    @Test
    @DisplayName("PUT /api/passengers/{id} - should return 404 when not found")
    void testUpdatePassenger_NotFound() throws Exception {
        when(passengerService.updatePassenger(eq(99L), any(Passenger.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/passengers/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(samplePassenger)))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE /api/passengers/{id}
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/passengers/{id} - should return 204 when deleted")
    void testDeletePassenger_Success() throws Exception {
        when(passengerService.deletePassenger(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/passengers/1"))
                .andExpect(status().isNoContent());

        verify(passengerService, times(1)).deletePassenger(1L);
    }

    @Test
    @DisplayName("DELETE /api/passengers/{id} - should return 404 when not found")
    void testDeletePassenger_NotFound() throws Exception {
        when(passengerService.deletePassenger(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/passengers/99"))
                .andExpect(status().isNotFound());
    }
}
