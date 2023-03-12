package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingShortMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.items.ItemNotFoundException;
import ru.practicum.shareit.exception.bookings.UnsupportedStatusException;
import ru.practicum.shareit.exception.users.UserMismatchException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.Status.REJECTED;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final BookingShortMapper bookingShortMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;


    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        Item newItem = itemMapper.toModel(itemDto);
        newItem.setOwner(userService.getUser(userId));
        log.info("ItemServiceImpl.addItem : DONE");
        return itemMapper.toDTO(itemRepository.save(newItem));
    }

    @Override
    public ItemDto updateItem(Long itemId, Long userId, ItemDto itemDto) {
        Item itemToUpdate = getItem(itemId);
        Item updateItem;

        if (itemToUpdate.getOwner().getId().equals(userId)) {
            updateItem = composeItem(itemToUpdate, itemDto);
            itemRepository.update(updateItem.getOwner(),
                                updateItem.getName(),
                                updateItem.getDescription(),
                                updateItem.getAvailable(),
                                updateItem.getId());
        } else {
            throw new UserMismatchException(userId, itemId);
        }
        log.info("ItemServiceImpl.updateItem : DONE");
        return itemMapper.toDTO(updateItem);
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        Item item = getItem(itemId);
        if (item.getOwner().getId().equals(userId)) {
            return concatComment(concatBooking(itemMapper.toDTO(item)));
        }
        log.info("ItemServiceImpl.getItemById : DONE");
        return concatComment(itemMapper.toDTO(item));
    }

    @Override
    public CommentDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        Comment comment;
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findAllByItem_IdAndBooker_Id(itemId, userId);

        if (!bookings.isEmpty() && bookings.stream()
                .anyMatch(x -> !x.getStatus().equals(REJECTED) && x.getStart().isBefore(now))) {
            comment = Comment.builder()
                    .text(commentDto.getText())
                    .author(bookings.get(0).getBooker())
                    .item(bookings.get(0).getItem())
                    .created(LocalDateTime.now())
                    .build();
            commentRepository.save(comment);
        } else {
            throw new UnsupportedStatusException("User with id-" + userId + " can't comment item with id -" + itemId);
        }
        log.info("ItemServiceImpl.addComment : DONE");
        return commentMapper.toDTO(comment);
    }

    @Override
    public Item getItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        log.info("ItemServiceImpl.getItem : DONE");
        return item;
    }

    @Override
    public Item composeItem(Item item, ItemDto itemDto) {
        Item it = Item.builder()
                .id(item.getId())
                .name(itemDto.getName() != null ?
                        itemDto.getName() : item.getName())
                .owner(item.getOwner())
                .description(itemDto.getDescription() != null ?
                        itemDto.getDescription() : item.getDescription())
                .available(itemDto.getAvailable() != null ?
                        itemDto.getAvailable() : item.getAvailable())
                .build();
        log.info("ItemServiceImpl.composeItem : DONE");
        return it;
    }

    @Override
    public ItemDto concatBooking(ItemDto itemDto) {
        itemDto.setLastBooking(getLastAndNextItemBookings(itemDto).get(0));
        itemDto.setNextBooking(getLastAndNextItemBookings(itemDto).get(1));
        log.info("ItemServiceImpl.concatBooking : DONE");
        return itemDto;
    }

    @Override
    public ItemDto concatComment(ItemDto itemDto) {
        itemDto.setComments(getItemComments(itemDto));
        log.info("ItemServiceImpl.concatComment : DONE");
        return itemDto;
    }

    private List<BookingDtoResponse> getLastAndNextItemBookings(ItemDto itemDto) {
        List<BookingDtoResponse> lastAndNextBokings = new ArrayList<>();
        lastAndNextBokings.add(0, null);
        lastAndNextBokings.add(1, null);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> itemBookings = bookingRepository.findAllByItem_Id(itemDto.getId());
        lastAndNextBokings.add(0, bookingShortMapper.toDTO(itemBookings.stream()
                .filter(x -> !x.getStatus().equals(REJECTED))
                .filter(x -> x.getEnd().isBefore(now))
                .sorted(Comparator.comparing(Booking::getStart))
                .findFirst().orElse(null)));
        lastAndNextBokings.add(1, bookingShortMapper.toDTO(itemBookings.stream()
                .filter(x -> !x.getStatus().equals(REJECTED))
                .filter(x -> x.getStart().isAfter(now))
                .sorted(Comparator.comparing(Booking::getStart))
                .findFirst().orElse(null)));
        return lastAndNextBokings;
    }

    private List<CommentDto> getItemComments(ItemDto itemDto) {
        return commentRepository.findAllByItem_Id(itemDto.getId()).stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
