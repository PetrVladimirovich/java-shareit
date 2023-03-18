package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.Constants.ID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestDto> createRequest(@RequestHeader(ID) @NotNull @Positive Long requestorId,
                                                        @RequestBody @NotNull @Valid ItemRequestDto dto) {
        log.info("ItemRequestController: POST /requests");
        return new ResponseEntity<>(itemRequestService.create(dto, requestorId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getUserRequests(@RequestHeader(ID) @NotNull @Positive Long requestorId) {
        log.info("ItemRequestController: GET /requests");
        return new ResponseEntity<List<ItemRequestDto>>(itemRequestService.getUserRequests(requestorId), HttpStatus.OK);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getUserRequest(@RequestHeader(ID) @NotNull @Positive Long requestorId,
                                                         @PathVariable("requestId") @Positive Long itemRequestId) {
        log.info("ItemRequestController: GET /requests/{}", requestorId);
        return new ResponseEntity<ItemRequestDto>(itemRequestService.getRequest(requestorId, itemRequestId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getAllRequest(@RequestHeader(ID) @NotNull @Positive Long requestorId,
                                                              @RequestParam(name = "from", required = false) @PositiveOrZero Integer from,
                                                              @RequestParam(name = "size", required = false) @Positive Integer size) {
        log.info("ItemRequestController: GET /requests/all");
        return new ResponseEntity<List<ItemRequestDto>>(itemRequestService.getAll(requestorId, from, size), HttpStatus.OK);
    }
}