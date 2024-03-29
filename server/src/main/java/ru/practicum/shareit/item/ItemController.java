package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import static ru.practicum.shareit.utils.Consts.REQUESTOR_ID_TAG;
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader(REQUESTOR_ID_TAG) Long userId,
                                              @RequestBody ItemDto dto) {
        log.info("ItemController: POST /items with ID: {}", userId);
        return new ResponseEntity<>(itemService.create(userId, dto), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader(REQUESTOR_ID_TAG) Long userId,
                                              @PathVariable("id") Long itemId,
                                              @RequestBody ItemDto dto) {
        log.info("ItemController: PATCH /items/{} with ID: {}", itemId, userId);
        return new ResponseEntity<>(itemService.update(userId, itemId, dto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@RequestHeader(REQUESTOR_ID_TAG) Long userId,
                                           @PathVariable("id") Long itemId) {
        log.info("ItemController: GET /items/{} with ID: {}", itemId, userId);
        return new ResponseEntity<>(itemService.getById(itemId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItems(@RequestHeader(REQUESTOR_ID_TAG) Long userId,
                                                  @RequestParam(name = "from", required = false) Integer from,
                                                  @RequestParam(name = "size", required = false) Integer size) {
        log.info("ItemController: GET /items with ID: {}", userId);
        return new ResponseEntity<>(itemService.getByUserId(userId, from, size).getContent(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam("text") String text,
                                                     @RequestParam(name = "from", required = false) Integer from,
                                                     @RequestParam(name = "size", required = false) Integer size) {
        log.info("ItemController: GET /items/search");
        return new ResponseEntity<>(itemService.getByText(text, from, size).getContent(), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@RequestHeader(REQUESTOR_ID_TAG) Long authorId,
                                                    @PathVariable("itemId") Long itemId,
                                                    @RequestBody CommentDto dto) {
        log.info("ItemController: POST /items/{}/comment with ID: {}", itemId, authorId);
        return new ResponseEntity<>(itemService.createComment(authorId, itemId, dto), HttpStatus.OK);
    }
}