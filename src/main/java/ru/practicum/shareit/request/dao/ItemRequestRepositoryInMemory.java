package ru.practicum.shareit.request.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.*;

@Repository
public class ItemRequestRepositoryInMemory implements ItemRequestRepository {
    private final Map<Long, ItemRequest> itemRequests = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public List<ItemRequest> getAllItemRequests() {
        return new ArrayList<>(itemRequests.values());
    }

    @Override
    public Optional<ItemRequest> getItemRequestById(Long itemRequestId) {
        return Optional.of(itemRequests.get(itemRequestId));
    }

    @Override
    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        if (itemRequest.getId() == null) {
            itemRequest.setId(nextId++);
        }
        itemRequests.put(itemRequest.getId(), itemRequest);
        return itemRequest;
    }

    @Override
    public ItemRequest updateItemRequest(ItemRequest itemRequest) {
        itemRequests.replace(itemRequest.getId(), itemRequest);
        return itemRequest;
    }

    @Override
    public void deleteItemRequestById(Long itemRequestId) {
        itemRequests.remove(itemRequestId);
    }

    @Override
    public boolean itemRequestExists(Long itemRequestId) {
        return itemRequests.containsKey(itemRequestId);
    }
}