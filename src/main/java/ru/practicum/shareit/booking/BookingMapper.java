package ru.practicum.shareit.booking;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@NoArgsConstructor
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus().toString());
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
//                .item(item)
//                .booker(user)
                .build();
    }

}
