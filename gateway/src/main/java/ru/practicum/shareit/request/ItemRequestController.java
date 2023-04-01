package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.utils.Consts.REQUESTOR_ID_TAG;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(REQUESTOR_ID_TAG) @NotNull @Positive Long requestorId,
                                                @RequestBody @NotNull @Valid ItemRequestDto dto) {
        log.info("ItemRequestController : POST /requests with ID: {}", requestorId);
        return requestClient.createRequest(dto, requestorId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(REQUESTOR_ID_TAG) @NotNull @Positive Long requestorId) {
        log.info("ItemRequestController : GET /requests with ID: {}", requestorId);
        return requestClient.getUserRequests(requestorId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(REQUESTOR_ID_TAG) @NotNull @Positive Long requestorId,
                                             @PathVariable("requestId") @Positive Long itemRequestId) {
        log.info("ItemRequestController : GET /requests/{} with ID: {}", itemRequestId, requestorId);
        return requestClient.getRequest(requestorId, itemRequestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequest(@RequestHeader(REQUESTOR_ID_TAG) @NotNull @Positive Long requestorId,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("ItemRequestController : GET /requests/all");
        return requestClient.getAll(requestorId, from, size);
    }
}