package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.notFound.ItemNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dao.ItemRequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRep;

    @Override
    public List<ItemRequest> getAllItemRequests() {
        log.debug("RequestService: done getAllItemRequests.");
        return itemRequestRep.getAllItemRequests();
    }

    @Override
    public ItemRequest getItemRequestById(Long itemRequestId) {
        ItemRequest itemRequest = itemRequestRep.getItemRequestById(itemRequestId).orElseThrow(
                () -> new ItemNotFoundException(itemRequestId)
        );
        log.debug("RequestService: done getItemRequestById - {}.", itemRequest);
        return itemRequest;
    }

    @Override
    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        if (itemRequest.getId() != null && itemRequestRep.itemRequestExists(itemRequest.getId())) {
            throw new ItemNotFoundException(itemRequest.getId());
        }
        itemRequest = itemRequestRep.createItemRequest(itemRequest);
        log.debug("RequestService: done createItemRequest - {}.", itemRequest);
        return itemRequest;
    }

    @Override
    public ItemRequest updateItemRequest(ItemRequest itemRequest) {
        if (!itemRequestRep.itemRequestExists(itemRequest.getId())) {
            throw new ItemNotFoundException(itemRequest.getId());
        }
        log.debug("RequestService: done updateItemRequest - {}.", itemRequest);
        return itemRequestRep.updateItemRequest(itemRequest);
    }

    @Override
    public void deleteItemRequestById(Long itemRequestId) {
        if (!itemRequestRep.itemRequestExists(itemRequestId)) {
            throw new ItemNotFoundException(itemRequestId);
        }
        itemRequestRep.deleteItemRequestById(itemRequestId);
        log.debug("RequestService: done deleteItemRequestById - ID {}.", itemRequestId);
    }
}