package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.alreadyExists.ItemAlreadyExistsException;
import ru.practicum.shareit.exception.notFound.ItemNotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRep;
    private final UserService userService;
    private final ItemRequestService requestService;

    @Override
    public List<Item> getAllItems(Long userId) {
        log.debug("ItemService: done getAllItems.");
        return itemRep.getAllItems(userId);
    }

    @Override
    public Item getItemById(Long userId, Long itemId) {
        Item item = itemRep.getItemById(itemId).orElseThrow(
                () -> new ItemNotFoundException(itemId)
        );
        log.debug("ItemService: done getItemById - {}.", item);
        return item;
    }

    @Override
    public List<Item> getItemsByText(String text) {
        List<Item> searchedItems = itemRep.getItemsByText(text.toLowerCase());
        log.debug("ItemService: done getItemsByText - {}.", searchedItems);
        return searchedItems;
    }

    @Override
    public Item createItem(Long userId, Item item, Long requestId) {
        if (item.getId() != null && itemRep.itemExists(item.getId())) {
            throw new ItemAlreadyExistsException(item.getId());
        }
        item.setOwner(userService.getUserById(userId));
        item.setRequest(requestId != null ? requestService.getItemRequestById(requestId) : null);
        item = itemRep.createItem(userId, item);
        log.debug("ItemService: done createItem - {}.", item);
        return item;
    }

    @Override
    public Item updateItem(Long userId, Long itemId, Item item, Long requestId) {
        if (!itemRep.itemExists(itemId)) {
            throw new ItemNotFoundException(itemId);
        }
        item.setOwner(userService.getUserById(userId));
        item.setRequest(requestId != null ? requestService.getItemRequestById(requestId) : null);
        item = itemRep.updateItem(userId, itemId, item);
        log.debug("ItemService: done updateItem - {}.", item);
        return item;
    }

    @Override
    public void deleteItemById(Long userId, Long itemId) {
        if (!itemRep.itemExists(itemId)) {
            throw new ItemNotFoundException(itemId);
        }
        itemRep.deleteItemById(itemId);
        log.debug("ItemService: done deleteItemById - ID {}.", itemId);
    }
}