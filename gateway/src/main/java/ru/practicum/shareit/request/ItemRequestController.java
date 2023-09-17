package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsByUser(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestClient.getAllItemRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequestsByOtherUsers(@RequestHeader(USER_ID_HEADER) Long userId,
                                                                 @RequestParam(defaultValue = "0") Integer from,
                                                                 @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestClient.getAllItemRequestsByOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable Long requestId,
                                                     @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestClient.getItemRequestById(requestId, userId);
    }

    @PostMapping
    @Validated
    public ResponseEntity<Object> saveItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                                  @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestClient.saveItemRequest(userId, itemRequestDto);
    }

}
