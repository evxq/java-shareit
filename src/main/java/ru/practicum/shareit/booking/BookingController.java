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

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                    @Valid @RequestBody BookingItemDto bookingItemDto) {
        return bookingService.createBooking(userId, bookingItemDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto responseToBooking(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                        @PathVariable Integer bookingId,
                                        @RequestParam Boolean approved) {
        return bookingService.responseToBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                     @PathVariable Integer bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingsForUser(@RequestHeader("X-Sharer-User-Id") Integer bookerId,
                                               @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingsForUser(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                                @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingsForOwner(ownerId, state);
    }

}
