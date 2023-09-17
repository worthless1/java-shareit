package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl userService;

    private final UserDto userDto = new UserDto();
    private final UserDto userDto2 = new UserDto();

    @BeforeEach
    public void addItems() {
        userDto.setId(1L);
        userDto.setName("userName");
        userDto.setEmail("test@gmail.ru");

        userDto2.setId(2L);
        userDto2.setName("userName2");
        userDto2.setEmail("test2@gmail.ru");
    }

    @Test
    void getAllUsersWhenInvokedThenResponseStatusOkWithUsersCollectionInBody() throws Exception {
        List<UserDto> users = Arrays.asList(userDto, userDto2);
        when(userService.getAllUsers()).thenReturn(users);

        String result = mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(result, objectMapper.writeValueAsString(users));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getAllUsersWhenInvokedThenResponseStatusOkWithEmptyList() throws Exception {
        String result = mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(Collections.EMPTY_LIST), result);
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserByIdWhenUserFoundThenReturnedUser() throws Exception {
        long userId = 0L;
        when(userService.getUserById(anyLong())).thenReturn(userDto);

        String result = mockMvc.perform(get("/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(result, objectMapper.writeValueAsString(userDto));
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUserByIdWhenUserNotFoundThenReturnedNotFound() throws Exception {
        long userId = 0L;
        NotFoundException exception = new NotFoundException("User with id 0 not found");
        when(userService.getUserById(anyLong()))
                .thenThrow(exception);

        mockMvc.perform(get("/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void saveUserWhenUserValidThenSavedUser() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        String result = mockMvc.perform(post("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(userDto), result);
        verify(userService, times(1)).createUser(userDto);
    }

    @Test
    void updateUserWhenUserValidThenUpdatedUser() throws Exception {
        long userId = 0L;
        when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(userDto2);

        String result = mockMvc.perform(patch("/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto2)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(userDto2), result);
        verify(userService, times(1)).updateUser(userId, userDto2);
    }

    @Test
    void deleteUserByIdWhenInvokedThenDeletedUser() throws Exception {
        long userId = 0L;

        mockMvc.perform(delete("/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }

}