package ru.practicum.shareit.request.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRequestRepository {

    List<ItemRequest> getAllItemRequests();

    Optional<ItemRequest> getItemRequestById(Long itemRequestId);

    ItemRequest createItemRequest(ItemRequest itemRequest);

    ItemRequest updateItemRequest(ItemRequest itemRequest);

    void deleteItemRequestById(Long itemRequestId);

    boolean itemRequestExists(Long itemRequestId);

}