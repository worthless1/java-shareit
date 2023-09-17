package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingAnswerDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void testGetAllBookingsByUser() {
        Long userId = 1L;
        int from = 0;
        int size = 10;
        List<BookingAnswerDto> expectedBookings = Collections.singletonList(new BookingAnswerDto());

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(userId, PageRequest.of(from, size)))
                .thenReturn(Collections.singletonList(new Booking()));

        List<BookingAnswerDto> result = bookingService.getAllBookingsByUser(userId, State.ALL, from, size);

        assertEquals(expectedBookings, result);
    }


    @Test
    void testSaveBooking() {
        Long userId = 0L;
        User user = new User();
        user.setId(1L);
        Item item = new Item();
        Long itemId = 0L;
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwnerId(user.getId());

        BookingRequestDto bookingToSave = new BookingRequestDto();
        bookingToSave.setItemId(itemId);
        bookingToSave.setStart(LocalDateTime.now());
        bookingToSave.setEnd(LocalDateTime.now().plusMinutes(1));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(BookingMapper.INSTANCE.toBooking(bookingToSave, user, item));

        BookingAnswerDto actualBooking = bookingService.saveBooking(bookingToSave, userId);

        assertEquals(bookingToSave.getId(), actualBooking.getId());
        assertEquals(bookingToSave.getStart(), actualBooking.getStart());
        assertEquals(bookingToSave.getEnd(), actualBooking.getEnd());
        assertEquals(bookingToSave.getItemId(), actualBooking.getItem().getId());
        assertEquals(1L, actualBooking.getBooker().getId());
        assertNull(actualBooking.getStatus());

        InOrder inOrder = inOrder(userRepository, itemRepository, bookingRepository);
        inOrder.verify(userRepository, times(1)).findById(userId);
        inOrder.verify(itemRepository, times(1)).findById(itemId);
        inOrder.verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testUpdateBooking() {
        Item item = new Item();
        item.setOwnerId(1L);

        Long bookingId = 0L;
        Booking oldBooking = new Booking();
        oldBooking.setItem(item);
        oldBooking.setStatus(BookingStatus.WAITING);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(oldBooking));

        Booking newBooking = new Booking();
        newBooking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.saveAndFlush(any(Booking.class))).thenReturn(newBooking);

        BookingAnswerDto actualBooking = bookingService.updateBooking(bookingId, true, 1L);

        assertEquals(newBooking.getId(), actualBooking.getId());
        assertEquals(newBooking.getStart(), actualBooking.getStart());
        assertEquals(newBooking.getEnd(), actualBooking.getEnd());
        assertEquals(newBooking.getStatus(), actualBooking.getStatus());

        verify(bookingRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).saveAndFlush(any(Booking.class));
    }

    @Test
    void testUpdateBookingWithInvalidStatus() {
        Long bookingId = 1L;
        Boolean approved = true;
        Long userId = 2L;

        Booking booking = new Booking();
        Item item = mock(Item.class);
        when(item.getOwnerId()).thenReturn(3L);

        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.updateBooking(bookingId, approved, userId));
    }

    @Test
    void testUpdateBookingWithInvalidOwnership() {
        Item item = new Item();
        item.setOwnerId(1L);
        Long bookingId = 0L;
        Booking oldBooking = new Booking();
        oldBooking.setItem(item);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.updateBooking(bookingId, true, 0L));

        assertEquals("Booking with id = 0 not found", exception.getMessage());
        verify(bookingRepository, times(1)).findById(anyLong());
        verify(bookingRepository, never()).saveAndFlush(any(Booking.class));
    }

}