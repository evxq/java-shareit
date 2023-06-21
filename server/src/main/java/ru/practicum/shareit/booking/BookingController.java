package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                    @RequestBody BookingItemDto bookingItemDto) {
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
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {

        /*List<BookingDto> list = bookingService.getBookingsForUser(bookerId, state, from, size).stream()
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());*/
        return bookingService.getBookingsForUser(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return bookingService.getBookingsForOwner(ownerId, state, from, size);
    }

}
