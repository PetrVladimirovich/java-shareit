package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import static ru.practicum.shareit.Constants.USER_ID_IN_HEADER;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(USER_ID_IN_HEADER) Long userId) {
        log.debug("ItemController: done getAllItems - {}.", userId);
        return ItemMapper.toItemDtoList(itemService.getAllItems(userId));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(USER_ID_IN_HEADER) Long userId, @PathVariable Long itemId) {
        log.debug("ItemController: done getItemById - {}.", itemId);
        return ItemMapper.toItemDto(itemService.getItemById(userId, itemId));
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByParams(@RequestParam String text) {
        if (text.isBlank()) {
            log.debug("ItemController: done findItemByParams - text not found.");
            return new ArrayList<>();
        } else {
            log.debug("ItemController: done findItemByParams - {}.", text);
            return ItemMapper.toItemDtoList(itemService.getItemsByText(text));
        }
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(USER_ID_IN_HEADER) Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.debug("ItemController: done createItem - {}.", itemDto);
        Item item = ItemMapper.toItem(itemDto);
        Long requestId = itemDto.getRequest();
        return ItemMapper.toItemDto(itemService.createItem(userId, item, requestId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader(USER_ID_IN_HEADER) Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto
    ) {
        log.debug("ItemController: done updateItem - {}.", itemDto);
        Item item = ItemMapper.toItem(itemDto);
        Long requestId = itemDto.getRequest();
        return ItemMapper.toItemDto(itemService.updateItem(userId, itemId, item, requestId));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(USER_ID_IN_HEADER) Long userId, @PathVariable Long itemId) {
        log.debug("ItemController: done deleteItem - {}.", itemId);
        itemService.deleteItemById(userId, itemId);
    }

}
