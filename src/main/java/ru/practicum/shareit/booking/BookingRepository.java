package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    String OWNER_QUERY = "SELECT bk " +
            "FROM Booking bk " +
            "JOIN bk.item it " +
            "JOIN it.owner ow " +
            "WHERE ow.id = ?1 ";

    String ORDER_QUERY = " ORDER BY bk.start DESC";

    @Query(OWNER_QUERY + ORDER_QUERY)
    List<Booking> getBookingsForOwner(Integer userId);                                                                                      // ALL for owner

    @Query(OWNER_QUERY + "AND UPPER(bk.status) = UPPER(?2)" + ORDER_QUERY)
    List<Booking> getBookingsForOwnerByStatus(Integer userId, String state);                                                                // BY STATUS for owner

    @Query(OWNER_QUERY + "AND bk.start < ?2 AND bk.end > ?3" + ORDER_QUERY)
    List<Booking> getBookingsForOwnerCurrent(Integer userId, LocalDateTime start, LocalDateTime end);                                       // CURRENT for owner

    @Query(OWNER_QUERY + "AND bk.end < ?2" + ORDER_QUERY)
    List<Booking> getBookingsForOwnerPast(Integer userId, LocalDateTime end);                                                               // PAST

    @Query(OWNER_QUERY + "AND bk.start > ?2" + ORDER_QUERY)
    List<Booking> getBookingsForOwnerFuture(Integer userId, LocalDateTime start);                                                           // FUTURE

    List<Booking> findAllByBookerIdOrderByStartDesc(Integer userId);                                                                        // ALL for user

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Integer userId, LocalDateTime start, LocalDateTime end);       // CURRENT for user

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Integer userId, LocalDateTime end);                                         // PAST

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Integer userId, LocalDateTime start);                                      // FUTURE

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Integer userId, BookingStatus status);                                         // WAITING / REJECTED

    String BOOKING_FOR_ITEM = "SELECT bk " +
            "FROM Booking bk " +
            "JOIN bk.item it " +
            "WHERE it.id = ?1 ";

    @Query(BOOKING_FOR_ITEM + "AND bk.start < ?2 ORDER BY bk.start DESC")
    List<Booking> findLastBookingForItem(Integer itemId, LocalDateTime start);

    @Query(BOOKING_FOR_ITEM + "AND bk.start > ?2 ORDER BY bk.start ASC")
    List<Booking> findNextBookingForItem(Integer itemId, LocalDateTime start);

    @Query("SELECT bk " +
            "FROM Booking bk " +
            "JOIN bk.item it " +
            "JOIN bk.booker us " +
            "WHERE it.id = ?1 AND us.id = ?2")
    List<Booking> findBookingByUserAndItem(Integer itemId, Integer userId);

}
