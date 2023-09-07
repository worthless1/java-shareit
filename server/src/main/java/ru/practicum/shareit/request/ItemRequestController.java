package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @GetMapping
    public List<ItemRequestDto> getAllItemRequestsByUser(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestService.getAllItemRequestsByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequestsByOtherUsers(@RequestHeader(USER_ID_HEADER) Long userId,
                                                               @RequestParam(defaultValue = "0") Integer from,
                                                               @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getAllItemRequestsByOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@PathVariable Long requestId,
                                             @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestService.getItemRequestById(requestId, userId);
    }

    @PostMapping
    @Validated
    public ItemRequestDto saveItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                          @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemRequestService.saveItemRequest(itemRequestDto, userId);
    }

}