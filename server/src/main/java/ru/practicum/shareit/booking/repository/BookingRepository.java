package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerId(Long bookerId);

    List<Booking> findAllByItemId(Long itemId);

    @Query("select b from Booking b " +
            "join fetch b.item as i " +
            "where i.owner.id = :ownerId")
    List<Booking> findByOwnerId(Long ownerId);

    @Query("select b from Booking b " +
            "where b.item.id in :ids")
    List<Booking> findAllByItemIds(List<Long> ids);

    @Query("select b from Booking b " +
            "join fetch b.item as i " +
            "where i.owner.id = :ownerId and b.id = :bookingId")
    Booking findByOwnerIdAndBookingId(Long ownerId, Long bookingId);

    @Query("select b from Booking b " +
            "join fetch b.item as i " +
            "where b.id = :bookingId and (i.owner.id = :userId or b.bookerId = :userId)")
    Booking findByIdAndUserId(Long bookingId, Long userId);
}