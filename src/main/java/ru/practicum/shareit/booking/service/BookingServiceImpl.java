package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingAnswerDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<BookingAnswerDto> getAllBookingsByUser(Long userId, String state) {
        getUserById(userId);

        List<Booking> bookings;
        switch (state) {
            case ("ALL"):
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            case ("CURRENT"):
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case ("PAST"):
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case ("FUTURE"):
                bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case ("WAITING"):
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            case ("REJECTED"):
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
            default:
                throw new UnsupportedStatusException();
        }

        return BookingMapper.INSTANCE.convertBookingListToBookingOutDTOList(bookings);
    }

    @Override
    public List<BookingAnswerDto> getAllBookingsAllItemsByOwner(Long ownerId, String state) {
        getUserById(ownerId);

        List<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId);
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        ownerId,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                break;
            case "PAST":
                bookings = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(
                        ownerId,
                        LocalDateTime.now()
                );
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(
                        ownerId,
                        LocalDateTime.now()
                );
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                        ownerId,
                        BookingStatus.WAITING
                );
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(
                        ownerId,
                        BookingStatus.REJECTED
                );
                break;
            default:
                throw new UnsupportedStatusException();
        }
        return BookingMapper.INSTANCE.convertBookingListToBookingOutDTOList(bookings);
    }

    @Override
    public BookingAnswerDto getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));

        Long bookerId = booking.getBooker().getId();
        Long ownerId = booking.getItem().getOwnerId();

        if (!bookerId.equals(userId) && !ownerId.equals(userId)) {
            throw new NotFoundException("User with id = " + userId +
                    " did not make a booking with id = " + bookingId);
        }

        return BookingMapper.INSTANCE.toBookingOutDto(booking);
    }


    @Transactional
    @Override
    public BookingAnswerDto saveBooking(BookingRequestDto bookingInDto, Long userId) {
        BookingRequestDto validatedBookingDto = validateBookingDto(bookingInDto);
        User user = getUserById(userId);
        Item item = getItemById(validatedBookingDto.getItemId());

        checkItemAvailability(item);
        checkOwnership(item, userId);

        Booking booking = createBooking(validatedBookingDto, user, item);
        return BookingMapper.INSTANCE.toBookingOutDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingAnswerDto updateBooking(Long bookingId, Boolean approved, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Booking with id = " + bookingId + " not found"));
        Item item = booking.getItem();

        if (!item.getOwnerId().equals(userId)) {
            throw new NotFoundException("User with id = " + userId +
                    " does not own the item: " + ItemMapper.INSTANCE.toItemDto(item));
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException("Booking status with id = " + bookingId +
                    " has not been changed by user with id = " + userId);
        }

        if (Boolean.TRUE.equals(approved)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.INSTANCE.toBookingOutDto(bookingRepository.saveAndFlush(booking));

    }

    private BookingRequestDto validateBookingDto(BookingRequestDto bookingInDto) {
        if (bookingInDto.getEnd().isBefore(bookingInDto.getStart())) {
            throw new ValidationException("The start date and time of the booking must be earlier than " +
                    "the end date and time of the booking");
        }
        if (bookingInDto.getEnd().isEqual(bookingInDto.getStart())) {
            throw new ValidationException("The start date and time of the reservation may not coincide with " +
                    "the end date and time of the booking");
        }
        return bookingInDto;
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found"));
    }

    private Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
    }

    private void checkItemAvailability(Item item) {
        if (Boolean.FALSE.equals(item.getAvailable())) {
            throw new ValidationException("Item " + ItemMapper.INSTANCE.toItemDto(item) +
                    " is unavailable for booking!");
        }
    }

    private void checkOwnership(Item item, Long userId) {
        if (item.getOwnerId().equals(userId)) {
            throw new NotFoundException("User with id = " + userId +
                    " does not own the item with the id = " + item.getId());
        }
    }

    private Booking createBooking(BookingRequestDto bookingDto, User user, Item item) {
        bookingDto.setStatus(BookingStatus.WAITING);
        Booking booking = BookingMapper.INSTANCE.toBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        return booking;
    }

}
