package ru.practicum.shareit.utils;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.ValidationException;

public final class PaginationMapper {

    private PaginationMapper() {
    }

    public static PageRequest toPage(Integer from, Integer size) {
        if (from == null || size == null) {
            return null;
        }

        if (from < 0) {
            throw new ValidationException("The from parameter cannot be less than 0");
        }

        if (size <= 0) {
            throw new ValidationException("The size parameter must be positive");
        }

        int page = from > 0 ? from / size : 0;
        return PageRequest.of(page, size);
    }

}