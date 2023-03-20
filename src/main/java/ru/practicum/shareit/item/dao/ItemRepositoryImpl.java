package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.items.ItemRepositoryException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.exception.users.UserRepositoryException;
import ru.practicum.shareit.user.dao.UserRepository;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository("itemDbStorage")
public class ItemRepositoryImpl implements ItemRepository {
    private final ItemRepositoryJpa itemRepositoryJpa;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public Item save(Item item) {
        userRepository.getById(item.getOwner());
        Item newItem = itemRepositoryJpa.save(item);
        log.info("add new item: {}", newItem.toString());
        return newItem;
    }

    @Override
    public Item update(Long userId, Item item) {
        Item itemForUpdate = getById(item.getId());
        if (!itemForUpdate.getOwner().equals(userId) || !itemForUpdate.getId().equals(item.getId())) {
            throw new ItemRepositoryException("this thing is not editable");
        }
        itemMapper.updateItem(item, itemForUpdate);
        itemRepositoryJpa.save(itemForUpdate);
        log.info("These things are updated: {}", itemForUpdate.toString());
        return itemForUpdate;
    }

    @Override
    public Item getById(Long itemId) {
        return itemRepositoryJpa.findById(itemId)
                .orElseGet(() -> {
                    throw new UserRepositoryException(itemId + ": this ID not found");
                });
    }

    @Override
    public Page<Item> getByUserId(Long userId, Pageable page) {
        return itemRepositoryJpa.findByOwner(userId, page);
    }

    @Override
    public Page<Item> getByText(String text, Pageable page) {
        return itemRepositoryJpa.getByText(text.toLowerCase(), page);
    }

    @Override
    public List<Item> getByRequest(Long requestId) {
        return itemRepositoryJpa.findByRequestId(requestId);
    }
}