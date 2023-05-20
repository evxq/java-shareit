package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping                                                                          // ПО ТЕСТАМ возвращается объект Booking
    public Booking createBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")                                                         // ПО ТЕСТАМ возвращается объект Booking
    public Booking responseToBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,       // здесь вроде передается id владелеца
                                     @PathVariable Integer bookingId,
                                     @RequestParam Boolean approved) {
        return bookingService.responseToBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")                                                           // ПО ТЕСТАМ возвращается объект Booking
    public Booking getBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId,          // передается id пользователя или владельца вещи
                                  @PathVariable Integer bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping                                                                           // ПО ТЕСТАМ возвращается объект Booking
    public List<Booking> getBookingsForUser(@RequestHeader("X-Sharer-User-Id") Integer userId,          // передается id пользователя, для которого нужно получить список бронирований
                                            @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingsForUser(userId, state);
    }

    @GetMapping("/owner")                                                                 // ПО ТЕСТАМ возвращается объект Booking
    public List<Booking> getBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,     // передается id владелеца вещей, для которых нужно получить список бронирований
                                             @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingsForOwner(userId, state);
    }

}
