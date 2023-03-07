package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Transactional
    @Modifying
    @Query("update Item i set i.owner = :owner, i.name = :name, " +
            "i.description = :description, i.available = :available where i.id = :itemId")
    void update(User owner, String name, String description, Boolean available, Long itemId);

    List<Item> findByOwnerId(Long userId);

    List<Item> findByDescriptionContainsIgnoreCaseOrNameContainsIgnoreCase(String textD, String textN);

}
