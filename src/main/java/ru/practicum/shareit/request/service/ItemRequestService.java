package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Service
public interface ItemRequestService {

    List<ItemRequest> getAllItemRequests();

    ItemRequest getItemRequestById(Long itemRequestId);

    ItemRequest createItemRequest(ItemRequest itemRequest);

    ItemRequest updateItemRequest(ItemRequest itemRequest);

    void deleteItemRequestById(Long itemRequestId);

}