package ru.practicum.shareit.booking;

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
        return new Booking(
//                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd()
//                BookingStatus.valueOf(bookingDto.getStatus())
        );
    }

}
