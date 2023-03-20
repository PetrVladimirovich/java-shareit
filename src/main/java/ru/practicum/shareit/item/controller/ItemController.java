package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.Constants.ID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader(ID) @NotNull Long userId,
                                              @RequestBody @NotNull ItemDto dto) {
        log.info("ItemController: POST /items");
        return new ResponseEntity<>(itemService.create(userId, dto), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader(ID) @NotNull Long userId,
                                              @PathVariable("id") Long itemId,
                                              @RequestBody @NotNull ItemDto dto) {
        log.info("ItemController: PATCH /items/{}", itemId);
        return new ResponseEntity<>(itemService.update(userId, itemId, dto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@RequestHeader(ID) Long userId,
                                           @PathVariable("id") Long itemId) {
        log.info("ItemController: GET /items/{}", itemId);
        return new ResponseEntity<>(itemService.getById(itemId, userId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<ItemDto>> getItems(@RequestHeader(ID) @NotNull Long userId,
                                                  @RequestParam(name = "from", required = false) @PositiveOrZero Integer from,
                                                  @RequestParam(name = "size", required = false) @Positive Integer size) {
        log.info("ItemController: GET /items");
        return new ResponseEntity<>(itemService.getByUserId(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam("text") String text,
                                                     @RequestParam(name = "from", required = false) @PositiveOrZero Integer from,
                                                     @RequestParam(name = "size", required = false) @Positive Integer size) {
        log.info("ItemController: GET /items/search");
        return new ResponseEntity<>(itemService.getByText(text, from, size), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@RequestHeader(ID) @NotNull Long authorId,
                                                    @PathVariable("itemId") Long itemId,
                                                    @RequestBody @NotNull @Valid CommentDto dto) {
        log.info("ItemController: POST /items/{}/comment", itemId);
        return new ResponseEntity<>(itemService.createComment(authorId, itemId, dto), HttpStatus.OK);
    }
}