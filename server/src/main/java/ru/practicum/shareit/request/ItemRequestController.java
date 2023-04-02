package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;
import static ru.practicum.shareit.utils.Consts.REQUESTOR_ID_TAG;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestDto> createRequest(@RequestHeader(REQUESTOR_ID_TAG) Long requestorId,
                                                        @RequestBody ItemRequestDto dto) {
        log.info("ItemRequestController : POST /request");
        return new ResponseEntity<>(itemRequestService.create(dto, requestorId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getUserRequests(@RequestHeader(REQUESTOR_ID_TAG) Long requestorId) {
        log.info("ItemRequestController : GET /request");
        return new ResponseEntity<List<ItemRequestDto>>(itemRequestService.getUserRequests(requestorId), HttpStatus.OK);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getUserRequest(@RequestHeader(REQUESTOR_ID_TAG) Long requestorId,
                                                         @PathVariable("requestId") Long itemRequestId) {
        log.info("ItemRequestController : GET /request/{}", requestorId);
        return new ResponseEntity<ItemRequestDto>(itemRequestService.getRequest(requestorId, itemRequestId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getAllRequest(@RequestHeader(REQUESTOR_ID_TAG) Long requestorId,
                                                              @RequestParam(name = "from", required = false) Integer from,
                                                              @RequestParam(name = "size", required = false) Integer size) {
        log.info("ItemRequestController : GET /request/all");
        return new ResponseEntity<List<ItemRequestDto>>(itemRequestService.getAll(requestorId, from, size), HttpStatus.OK);
    }
}