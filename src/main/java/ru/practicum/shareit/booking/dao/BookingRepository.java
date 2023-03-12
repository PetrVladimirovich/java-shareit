package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker_Id(Long bookerId);

    List<Booking> findAllByBooker_IdNotAndItemIn(Long userId, List<Item> ownerItems);

    @Transactional
    @Modifying
    @Query("update Booking b set b.status = :status  where b.id = :bookingId")
    void update(Status status, Long bookingId);

    List<Booking> findAllByItem_Id(Long itemId);

    List<Booking> findAllByItem_IdAndBooker_Id(Long itemId, Long bookerId);
}
