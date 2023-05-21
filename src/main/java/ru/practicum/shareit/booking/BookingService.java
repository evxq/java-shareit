package ru.practicum.shareit.booking;


import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface BookingService {

    @Transactional
    Booking createBooking(Integer userId, BookingDto bookingDto);

    Booking responseToBooking(Integer userId, Integer bookingId, Boolean approved);

    Booking getBookingById(Integer userId, Integer bookingId);

    List<Booking> getBookingsForUser(Integer userId, String state);

    List<Booking> getBookingsForOwner(Integer userId, String state);

}
