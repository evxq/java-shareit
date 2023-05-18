package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                 @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking responseBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                   @RequestParam Boolean approved) {
        return bookingService.responseBooking(userId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                  @PathVariable Integer bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<Booking> getBookingsForUser(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                            @RequestParam(required = false) String state) {
        return bookingService.getBookingsForUser(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getBookingsForUserItems(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @RequestParam(required = false) String state) {
        return bookingService.getBookingsForUserItems(userId, state);
    }

}
