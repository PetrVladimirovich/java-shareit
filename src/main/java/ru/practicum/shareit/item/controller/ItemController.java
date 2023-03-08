package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.Constants.ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping
    public List<ItemDto> getAllItemsOfUserWithId(@NotEmpty @RequestHeader(ID) Long userId) {
        log.info("ItemController : GET /items");
        return itemRepository.findByOwnerId(userId).stream()
                .map(itemMapper::toDTO)
                .map(itemService::concatBooking)
                .map(itemService::concatComment)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@NotEmpty @RequestHeader(ID) Long userId, @PathVariable Long itemId) {
        log.info("ItemController : GET /items/{}", itemId);
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        if (text != null && !text.isEmpty()) {
            log.info("ItemController : GET /items/search?test={} ...", text.substring(0, text.length() % 5));
            return itemRepository.findByDescriptionContainsIgnoreCaseOrNameContainsIgnoreCase(text, text)
                    .stream()
                    .filter(Item::getAvailable)
                    .map(itemMapper::toDTO)
                    .collect(Collectors.toList());
        } else {
            log.info("ItemController EMPTY REQUEST PARAM: GET /items/search?test=...");
            return new ArrayList<>();
        }
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @NotEmpty @RequestHeader(ID) Long userId,
                              @RequestBody ItemDto itemDto) {
        log.info("ItemController : PATCH /items/{}", itemId);
        return itemService.updateItem(itemId, userId, itemDto);
    }

    @PostMapping
    public ItemDto addItem(@NotEmpty @RequestHeader(ID) Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("ItemController : POST /items");
        return itemService.addItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId,
                                 @NotEmpty @RequestHeader(ID) Long userId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("ItemController : POST /items/{}/comment", itemId);
        return itemService.addComment(itemId, userId, commentDto);
    }
}
