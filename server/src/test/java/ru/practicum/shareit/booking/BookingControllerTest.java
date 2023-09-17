package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingAnswerDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private UserDto userDto;
    private ItemDto itemDto;
    private BookingAnswerDto bookingAnswerDto;
    private BookingAnswerDto bookingAnswerDto2;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final Long bookerId = 1L;
    private final Long bookingId = 1L;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "userName", "test@gmail.com");

        itemDto = new ItemDto(1L, "itemName", "description", true, 1L);

        ItemDto itemDto2 = new ItemDto(1L, "itemName2", "description2", true, 1L);


        bookingAnswerDto = new BookingAnswerDto(
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                itemDto,
                userDto,
                BookingStatus.WAITING);

        bookingAnswerDto2 = new BookingAnswerDto(
                1L,
                LocalDateTime.now().plusHours(3),
                LocalDateTime.now().plusDays(5),
                itemDto2,
                userDto,
                BookingStatus.WAITING);
    }

    @Test
    void testAddBooking() throws Exception {
        BookingRequestDto bookingRequestDto = new BookingRequestDto(1L,
                LocalDateTime.now().plusMinutes(2),
                LocalDateTime.now().plusHours(2),
                itemDto.getId(),
                userDto,
                BookingStatus.WAITING);


        when(bookingService.saveBooking(any(BookingRequestDto.class), anyLong())).thenReturn(bookingAnswerDto);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, bookerId)
                        .content(objectMapper.writeValueAsString(bookingRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingAnswerDto)));
    }

    @Test
    void testUpdateItem() throws Exception {
        when(bookingService.updateBooking(anyLong(), anyBoolean(), anyLong())).thenReturn(bookingAnswerDto2);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingAnswerDto2)));
    }

    @Test
    void testGetAllBookingsByUser() throws Exception {
        List<BookingAnswerDto> expectedBookings = Arrays.asList(bookingAnswerDto, bookingAnswerDto2);

        when(bookingService.getAllBookingsByUser(1L, State.ALL, 0, 10)).thenReturn(expectedBookings);


        mockMvc.perform(get("/bookings").header(USER_ID_HEADER, bookerId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBookings)));
    }

    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(bookingAnswerDto);

        mockMvc.perform(get("/bookings/" + bookingId).header(USER_ID_HEADER, bookerId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingAnswerDto)));
    }

    @Test
    void getByBookingIdShouldReturnNotFound() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong())).thenThrow(new NotFoundException(""));

        mockMvc.perform(get("/bookings/" + bookingId).header(USER_ID_HEADER, bookerId))
                .andExpect(status().isNotFound());
    }

}