package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRequestServiceImpl itemRequestService;

    private final Long userId = 0L;
    private ItemRequestDto itemRequestDto;
    private ItemRequestDto itemRequestDto2;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("userName");
        userDto.setEmail("test@gmail.ru");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemName1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(true);
        ItemDto itemDto2 = new ItemDto();
        itemDto2.setId(2L);
        itemDto2.setName("itemName2");
        itemDto2.setDescription("description2");
        itemDto2.setAvailable(true);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("description1");
        itemRequestDto2 = new ItemRequestDto();
        itemRequestDto2.setId(2L);
        itemRequestDto2.setDescription("description2");
    }

    @Test
    void getAllItemRequestsByUser() throws Exception {
        List<ItemRequestDto> itemRequests = Arrays.asList(itemRequestDto, itemRequestDto2);
        when(itemRequestService.getAllItemRequestsByUser(anyLong()))
                .thenReturn(itemRequests);

        String result = mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(result, objectMapper.writeValueAsString(itemRequests));
        verify(itemRequestService, times(1)).getAllItemRequestsByUser(userId);
    }

    @Test
    void getAllItemRequestsByOtherUsers() throws Exception {
        List<ItemRequestDto> itemRequests = Arrays.asList(itemRequestDto, itemRequestDto2);
        when(itemRequestService.getAllItemRequestsByOtherUsers(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemRequests);

        String result = mockMvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(result, objectMapper.writeValueAsString(itemRequests));
        verify(itemRequestService, times(1))
                .getAllItemRequestsByOtherUsers(userId, 0, 10);
    }

    @Test
    void getItemRequestById() throws Exception {
        long itemRequestId = 0L;
        when(itemRequestService.getItemRequestById(anyLong(), anyLong())).thenReturn(itemRequestDto);

        String result = mockMvc.perform(get("/requests/{itemRequestId}", itemRequestId)
                        .header(USER_ID_HEADER, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(result, objectMapper.writeValueAsString(itemRequestDto));
        verify(itemRequestService, times(1)).getItemRequestById(itemRequestId, userId);
    }

    @Test
    void saveItemRequest() throws Exception {
        when(itemRequestService.saveItemRequest(any(ItemRequestDto.class), anyLong()))
                .thenReturn(itemRequestDto);

        String result = mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(result, objectMapper.writeValueAsString(itemRequestDto));
        verify(itemRequestService, times(1))
                .saveItemRequest(any(ItemRequestDto.class), anyLong());
    }

}