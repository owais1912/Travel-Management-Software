package com.travel.booking;

import com.travel.booking.model.Booking;
import com.travel.booking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pure unit test for BookingService (no Spring context).
 */
@DisplayName("BookingService Unit Tests")
public class BookingServiceTest {

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService();
    }

    @Test
    @DisplayName("createBooking should assign an ID and save the booking")
    void testCreateBooking() {
        Booking b = new Booking(null, 1L, "Airport", "City", Booking.CabType.SEDAN, 20.0);
        Booking saved = bookingService.createBooking(b);

        assertNotNull(saved.getId());
        assertEquals("Airport", saved.getPickupLocation());
        assertEquals(Booking.BookingStatus.CONFIRMED, saved.getStatus());
    }

    @Test
    @DisplayName("getAllBookings should return all saved bookings")
    void testGetAllBookings() {
        bookingService.createBooking(new Booking(null, 1L, "A", "B", Booking.CabType.MINI, 5.0));
        bookingService.createBooking(new Booking(null, 2L, "C", "D", Booking.CabType.SUV, 15.0));

        List<Booking> all = bookingService.getAllBookings();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("getBookingById should return the correct booking")
    void testGetBookingById_Found() {
        Booking saved = bookingService.createBooking(
                new Booking(null, 1L, "X", "Y", Booking.CabType.LUXURY, 10.0));

        Optional<Booking> result = bookingService.getBookingById(saved.getId());
        assertTrue(result.isPresent());
        assertEquals("X", result.get().getPickupLocation());
    }

    @Test
    @DisplayName("getBookingById should return empty for missing ID")
    void testGetBookingById_NotFound() {
        Optional<Booking> result = bookingService.getBookingById(999L);
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("updateBooking should modify and return the booking")
    void testUpdateBooking() {
        Booking saved = bookingService.createBooking(
                new Booking(null, 1L, "Old Pickup", "Old Drop", Booking.CabType.MINI, 5.0));

        Booking updated = new Booking(null, 1L, "New Pickup", "New Drop", Booking.CabType.SEDAN, 12.0);
        Optional<Booking> result = bookingService.updateBooking(saved.getId(), updated);

        assertTrue(result.isPresent());
        assertEquals("New Pickup", result.get().getPickupLocation());
        assertEquals(Booking.CabType.SEDAN, result.get().getCabType());
    }

    @Test
    @DisplayName("deleteBooking should return true and remove booking")
    void testDeleteBooking_Success() {
        Booking saved = bookingService.createBooking(
                new Booking(null, 1L, "P", "D", Booking.CabType.SUV, 8.0));

        boolean deleted = bookingService.deleteBooking(saved.getId());
        assertTrue(deleted);
        assertFalse(bookingService.getBookingById(saved.getId()).isPresent());
    }

    @Test
    @DisplayName("deleteBooking should return false for non-existent ID")
    void testDeleteBooking_NotFound() {
        boolean deleted = bookingService.deleteBooking(888L);
        assertFalse(deleted);
    }

    @Test
    @DisplayName("getBookingsByPassenger should return all bookings for a passenger")
    void testGetBookingsByPassenger() {
        bookingService.createBooking(new Booking(null, 5L, "A", "B", Booking.CabType.MINI, 3.0));
        bookingService.createBooking(new Booking(null, 5L, "C", "D", Booking.CabType.SEDAN, 7.0));
        bookingService.createBooking(new Booking(null, 9L, "E", "F", Booking.CabType.SUV, 12.0));

        List<Booking> passenger5Bookings = bookingService.getBookingsByPassenger(5L);
        assertEquals(2, passenger5Bookings.size());
    }
}
