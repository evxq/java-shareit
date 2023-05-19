package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Integer userId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Integer userId, LocalDateTime start, LocalDateTime end);       // CURRENT

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Integer userId, LocalDateTime end);                                         // PAST

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Integer userId, LocalDateTime start);                                      // FUTURE

    List<Booking> findAllByBookerIdAndStatusIsWaitingOrderByStartDesc(Integer userId);                                                      // WAITING

    List<Booking> findAllByBookerIdAndStatusIsRejectedOrderByStartDesc(Integer userId);                                                     // REJECTED

    /*@Query("SELECT bk " +
            "FROM Booking bk " +
            "WHERE bk.booker = 1? AND bk.")
    List<Booking> getBookingsForUser(Integer userId, String state);*/

    String OWNER_QUERY = "SELECT bk " +
            "FROM Booking bk " +
            "JOIN bk.item it" +
            "JOIN it.owner ow " +
            "WHERE ow.id = 1? ";
    String ORDER_QUERY = " ORDER BY bk.start DESC";

    @Query(OWNER_QUERY + ORDER_QUERY)
    List<Booking> getBookingsForOwner(Integer userId);

    @Query(OWNER_QUERY + "AND UPPER(bk.status) = UPPER(2?)" + ORDER_QUERY)
    List<Booking> getBookingsForOwnerByStatus(Integer userId, String state);

    @Query(OWNER_QUERY + "AND bk.start < 2? AND bk.end > 3?" + ORDER_QUERY)
    List<Booking> getBookingsForOwnerCurrent(Integer userId, LocalDateTime start, LocalDateTime end);

    @Query(OWNER_QUERY + "AND bk.end < 2?" + ORDER_QUERY)
    List<Booking> getBookingsForOwnerPast(Integer userId, LocalDateTime end);

    @Query(OWNER_QUERY + "AND bk.start > 2?" + ORDER_QUERY)
    List<Booking> getBookingsForOwnerFuture(Integer userId, LocalDateTime start);

}
