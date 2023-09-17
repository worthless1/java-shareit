package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemServiceImpl itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final Long userId = 0L;
    private final UserDto userDto = new UserDto();
    private final ItemDto itemDto = new ItemDto();
    private final ItemDto itemDto2 = new ItemDto();

    @BeforeEach
    public void addItems() {
        userDto.setId(1L);
        userDto.setName("userName");
        userDto.setEmail("test@gmail.ru");

        itemDto.setId(1L);
        itemDto.setName("itemName1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(true);
        itemDto2.setId(2L);
        itemDto2.setName("itemName2");
        itemDto2.setDescription("description2");
        itemDto2.setAvailable(true);
    }

    @Test
    void getAllItemsByUser() throws Exception {
        List<ItemDto> items = Arrays.asList(itemDto, itemDto2);
        when(itemService.getAllItemsByUser(anyLong(), anyInt(), anyInt())).thenReturn(items);

        String result = mockMvc.perform(get("/items")
                        .header(USER_ID_HEADER, userId)
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(result, objectMapper.writeValueAsString(items));
        verify(itemService, times(1)).getAllItemsByUser(userId, 0, 10);
    }

    @Test
    void getAllItemsByUserThenResponseStatusBadRequest() throws Exception {
        mockMvc.perform(get("/items")
                        .param("from", "0")
                        .param("size", "5")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).getAllItemsByUser(userId, 0, 5);
    }

    @Test
    void getItemById() throws Exception {
        long itemId = 0L;
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemDto);

        String result = mockMvc.perform(get("/items/{itemId}", itemId)
                        .header(USER_ID_HEADER, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(result, objectMapper.writeValueAsString(itemDto));
        verify(itemService, times(1)).getItemById(itemId, userId);
    }

    @Test
    void addItem() throws Exception {
        when(itemService.addItem(any(ItemDto.class), anyLong())).thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(result, objectMapper.writeValueAsString(itemDto));

        verify(itemService, times(1)).addItem(itemDto, userId);
    }

//    @Test
//    void saveItemWhenItemNotValid() throws Exception {
//        itemDto.setName("");
//
//        mockMvc.perform(post("/items")
//                        .header(USER_ID_HEADER, userId)
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(itemDto)))
//                .andExpect(status().isBadRequest());
//
//        verify(itemService, never()).addItem(itemDto, userId);
//    }

    @Test
    void updateItem() throws Exception {
        long itemId = 0L;
        when(itemService.updateItem(anyLong(), any(ItemDto.class), anyLong())).thenReturn(itemDto2);

        String result = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header(USER_ID_HEADER, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto2)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(result, objectMapper.writeValueAsString(itemDto2));

        verify(itemService, times(1)).updateItem(itemId, itemDto2, userId);
    }

    @Test
    void searchAvailableItems() throws Exception {
        List<ItemDto> items = Arrays.asList(itemDto, itemDto2);
        when(itemService.searchAvailableItems(anyString(), anyInt(), anyInt())).thenReturn(items);

        String result = mockMvc.perform(get("/items/search")
                        .header(USER_ID_HEADER, userId)
                        .param("text", "test")
                        .param("from", "0")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(result, objectMapper.writeValueAsString(items));
        verify(itemService, times(1)).searchAvailableItems("test", 0, 10);
    }

    @Test
    void saveComment() throws Exception {
        long itemId = 0L;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("commentText");

        when(itemService.addComment(any(CommentDto.class), anyLong(), anyLong())).thenReturn(commentDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header(USER_ID_HEADER, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(result, objectMapper.writeValueAsString(commentDto));
        verify(itemService, times(1)).addComment(commentDto, itemId, userId);
    }

}