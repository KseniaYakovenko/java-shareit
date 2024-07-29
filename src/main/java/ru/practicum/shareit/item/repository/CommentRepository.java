package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select count(b) > 0 from Booking b " +
            "join b.item as i " +
            "where i.id = :itemId " +
            "and b.bookerId = :userId " +
            "and b.finish < current_timestamp and b.status = 'APPROVED'")
    boolean existFinishedBooking(long itemId, long userId);

    List<Comment> findAllByItemId(long itemId);
}