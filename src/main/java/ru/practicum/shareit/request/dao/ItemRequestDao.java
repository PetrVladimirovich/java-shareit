package ru.practicum.shareit.request.dao;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestDao {
    List<ItemRequest> findAllItemRequests();
    Optional<ItemRequest> findItemRequestById(Long itemRequestId);
    ItemRequest createItemRequest(ItemRequest itemRequest);
    ItemRequest updateItemRequest(ItemRequest itemRequest);
    void deleteItemRequestById(Long itemRequestId);
    boolean itemRequestExists(Long itemRequestId);
}