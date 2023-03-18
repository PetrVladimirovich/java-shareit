package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.exception.requests.ItemRequestServiceException;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemRequestServiceImpl(ItemRequestMapper itemRequestMapper, ItemRequestRepository itemRequestRepository,
                                  @Qualifier("dbStorage") UserRepository userRepository,
                                  @Qualifier("itemDbStorage") ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRequestMapper = itemRequestMapper;
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemRequestDto create(ItemRequestDto dto, Long requestorId) {
        dto.setRequestor(userRepository.getById(requestorId).getId());
        ItemRequest itemRequest = itemRequestRepository.save(itemRequestMapper.toItemRequest(dto));

        log.info("ItemRequestServiceImpl.create({}(ItemRequestDto), {}(requestorId)) : DONE", dto, requestorId);
        return itemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Long requestorId) {
        userRepository.getById(requestorId);
        List<ItemRequest> itemsRequests = itemRequestRepository.findAllByRequestor(requestorId);
        List<ItemRequestDto> dto = new ArrayList<>();
        itemsRequests.forEach(itemRequest -> dto.add(fillItemRequestDto(itemRequest)));

        log.info("ItemRequestServiceImpl.create({}(ItemRequestDto), {}(requestorId)) : DONE", dto, requestorId);
        return dto.stream()
                .sorted(Comparator.comparing(ItemRequestDto::getCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequest(Long requestorId, Long itemRequestId) {
        userRepository.getById(requestorId);
        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new ItemRequestServiceException("no request found with id: " + itemRequestId));

        log.info("ItemRequestServiceImpl.getRequest({}(requestorId), {}(itemRequestId)) : DONE", requestorId, requestorId);
        return fillItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getAll(Long requestorId, Integer from, Integer size) {
        userRepository.getById(requestorId);
        Pageable page;
        if (size == null || from == null) {
            page = Pageable.unpaged();
        } else {
            Sort sortByCreated = Sort.by(Sort.Direction.DESC, "created");
            page = PageRequest.of(from / size, size, sortByCreated);
        }

        Page<ItemRequest> itemRequestPage = itemRequestRepository.findByRequestorNot(requestorId, page);
        List<ItemRequestDto> itemsRequestsDto = new ArrayList<>();
        itemRequestPage.getContent().forEach(itemRequest -> {
                                                itemsRequestsDto.add(fillItemRequestDto(itemRequest));
                                            });
        log.info("ItemRequestServiceImpl.getAll({}(requestorId), {}(from), {}(size)) : DONE", requestorId, from, size);
        return itemsRequestsDto;
    }

    private ItemRequestDto fillItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(itemMapper.toItemForItemRequestDto(itemRepository.getByRequest(itemRequest.getId())));
        return itemRequestDto;

    }
}