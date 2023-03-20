package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.exception.items.CommentServiceException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.exception.items.ItemServiceException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemStatus;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    public ItemDto create(Long userId, ItemDto dto) {
        if (BooleanUtils.isNotTrue(dto.getAvailable()) || StringUtils.isBlank(dto.getName()) ||
                StringUtils.isBlank(dto.getDescription())) {
            log.warn("ItemServiceImpl.create({}(userId), {}(UserDto)) : ItemServiceException(\"invalid properties of a thing\")", userId, dto);
            throw new ItemServiceException("invalid properties of a thing");
        }
        Item item = itemMapper.toItem(dto);
        item.setOwner(userId);

        log.info("ItemServiceImpl.create({}(userId), {}(ItemDto)) : DONE", userId, dto);
        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto dto) {
        Item item = itemMapper.toItem(dto);
        item.setId(itemId);

        log.info("ItemServiceImpl.update({}(userId), {}(itemId), {}(ItemDto)) : DONE", userId, itemId, dto);
        return itemMapper.toDto(itemRepository.update(userId, item));
    }

    @Override
    public ItemDto getById(Long itemId, Long userId) {
        ItemDto itemDto = itemMapper.toDto(itemRepository.getById(itemId));
        prepareDto(itemId, userId, itemDto);

        log.info("ItemServiceImpl.getById({}(itemId), {}(userId)) : DONE", itemId, userId);
        return itemDto;
    }

    private void prepareDto(Long itemId, Long userId, ItemDto itemDto) {
        List<Booking> itemBooking = bookingRepository.findByItemIdAndItemOwner(itemId, userId);
        itemBooking.stream()
                .filter(i -> i.getStart().isBefore(LocalDateTime.now()) && !i.getStatus().equals(ItemStatus.REJECTED))
                .max(Comparator.comparing(Booking::getEnd))
                .ifPresent(lastBooking -> itemDto.setLastBooking(bookingMapper.toBookingItemDto(lastBooking)));
        itemBooking.stream()
                .filter(i -> i.getStart().isAfter(LocalDateTime.now()) && !i.getStatus().equals(ItemStatus.REJECTED))
                .min(Comparator.comparing(Booking::getStart))
                .ifPresent(nextBooking -> itemDto.setNextBooking(bookingMapper.toBookingItemDto(nextBooking)));
        itemDto.setComments(commentMapper.toDto(commentRepository.findByItemId(itemId)));
    }

    @Override
    public List<ItemDto> getByUserId(Long userId, Integer from, Integer size) {

        Pageable page;
        if (size == null || from == null) {
            page = Pageable.unpaged();
        } else {
            Sort sortById = Sort.by(Sort.Direction.ASC, "id");
            page = PageRequest.of(from / size, size, sortById);
        }

        Page<Item> itemPage = itemRepository.getByUserId(userId, page);
        List<ItemDto> itemDto = itemMapper.toDtoItems(itemPage.getContent());

        for (ItemDto dto : itemDto) {
            prepareDto(dto.getId(), userId, dto);
        }

        log.info("ItemServiceImpl.getByUserId({}(userId), {}(from), {}(size)) : DONE", userId, from, size);
        return itemDto.stream()
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getByText(String text, Integer from, Integer size) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        Pageable page;
        if (size == null || from == null) {
            page = Pageable.unpaged();
        } else {
            Sort sortById = Sort.by(Sort.Direction.ASC, "id");
            page = PageRequest.of(from / size, size, sortById);
        }
        Page<Item> itemPage = itemRepository.getByText(text, page);

        log.info("ItemServiceImpl.getByText({}(text), {}(from), {}(size)) : DONE", text, from, size);
        return itemMapper.toDtoItems(itemPage.getContent());
    }

    @Override
    public CommentDto createComment(Long authorId, Long itemId, CommentDto dto) {
        List<Booking> bookings = bookingRepository.findByItemIdAndBookerId(itemId, authorId).stream()
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now())).collect(Collectors.toList());

        if (bookings.isEmpty()) {
            log.warn("ItemServiceImpl.createComment({}(authorId), {}(itemId), {}(CommentDto)) : UserServiceException" +
                    "(\"the name and/or description fields are not filled in\")", authorId, itemId, dto);
            throw new CommentServiceException("it is impossible to add a review for this thing");
        }

        Comment comment = commentMapper.toComment(dto);
        comment.setItem(itemRepository.getById(itemId));
        comment.setAuthor(userRepository.getById(authorId));
        commentRepository.save(comment);

        log.info("ItemServiceImpl.createComment({}(authorId), {}(itemId), {}(CommentDto)) : DONE", authorId, itemId, dto);
        return commentMapper.toDto(comment);
    }
}