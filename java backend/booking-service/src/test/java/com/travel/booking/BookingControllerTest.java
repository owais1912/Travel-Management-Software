package com.travel.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.booking.controller.BookingController;
import com.travel.booking.model.Booking;
import com.travel.booking.service.BookingService;
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
 * Unit tests for BookingController using MockMvc.
 *
 * Tests all CRUD operations:
 *  - Create booking (POST)
 *  - Get all bookings (GET)
 *  - Get booking by ID (GET)
 *  - Update booking (PUT)
 *  - Delete booking (DELETE)
 */
@WebMvcTest(BookingController.class)
@DisplayName("BookingController Unit Tests")
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private Booking sampleBooking;

    @BeforeEach
    void setUp() {
        sampleBooking = new Booking(
                1L, 1L,
                "Bangalore Airport",
                "MG Road",
                Booking.CabType.SEDAN,
                28.5
        );
        sampleBooking.setStatus(Booking.BookingStatus.CONFIRMED);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /api/bookings
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/bookings - should create booking and return 201")
    void testCreateBooking_Success() throws Exception {
        when(bookingService.createBooking(any(Booking.class))).thenReturn(sampleBooking);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBooking)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.pickupLocation").value("Bangalore Airport"))
                .andExpect(jsonPath("$.dropLocation").value("MG Road"))
                .andExpect(jsonPath("$.cabType").value("SEDAN"))
                .andExpect(jsonPath("$.distanceKm").value(28.5))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(bookingService, times(1)).createBooking(any(Booking.class));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/bookings
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/bookings - should return all bookings with 200")
    void testGetAllBookings_Success() throws Exception {
        Booking booking2 = new Booking(2L, 2L, "Whitefield", "Koramangala",
                Booking.CabType.MINI, 12.0);

        List<Booking> bookings = Arrays.asList(sampleBooking, booking2);
        when(bookingService.getAllBookings()).thenReturn(bookings);

        mockMvc.perform(get("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));

        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    @DisplayName("GET /api/bookings - should return empty list when no bookings exist")
    void testGetAllBookings_EmptyList() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(List.of());

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/bookings/{id}
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/bookings/{id} - should return booking when found")
    void testGetBookingById_Found() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(sampleBooking));

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cabType").value("SEDAN"));

        verify(bookingService, times(1)).getBookingById(1L);
    }

    @Test
    @DisplayName("GET /api/bookings/{id} - should return 404 when not found")
    void testGetBookingById_NotFound() throws Exception {
        when(bookingService.getBookingById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/99"))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).getBookingById(99L);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PUT /api/bookings/{id}
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/bookings/{id} - should update and return 200")
    void testUpdateBooking_Success() throws Exception {
        Booking updatedBooking = new Booking(1L, 1L, "Kempegowda Bus Stand",
                "Hebbal", Booking.CabType.SUV, 15.0);
        when(bookingService.updateBooking(eq(1L), any(Booking.class)))
                .thenReturn(Optional.of(updatedBooking));

        mockMvc.perform(put("/api/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBooking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pickupLocation").value("Kempegowda Bus Stand"))
                .andExpect(jsonPath("$.cabType").value("SUV"));
    }

    @Test
    @DisplayName("PUT /api/bookings/{id} - should return 404 when booking not found")
    void testUpdateBooking_NotFound() throws Exception {
        when(bookingService.updateBooking(eq(99L), any(Booking.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/bookings/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBooking)))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE /api/bookings/{id}
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/bookings/{id} - should return 204 when deleted")
    void testDeleteBooking_Success() throws Exception {
        when(bookingService.deleteBooking(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNoContent());

        verify(bookingService, times(1)).deleteBooking(1L);
    }

    @Test
    @DisplayName("DELETE /api/bookings/{id} - should return 404 when not found")
    void testDeleteBooking_NotFound() throws Exception {
        when(bookingService.deleteBooking(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/bookings/99"))
                .andExpect(status().isNotFound());
    }
}
