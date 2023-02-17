package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository {

    List<Item> getAllItems(Long userId);

    Optional<Item> getItemById(Long itemId);

    List<Item> getItemsByText(String text);

    Item createItem(Long userId, Item item);

    Item updateItem(Long userId, Long itemId, Item item);

    void deleteItemById(Long itemId);

    boolean itemExists(Long itemId);
}