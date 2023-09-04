package ru.practicum.shareit.utils;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

class PaginationMapperTest {

    @Test
    void toPageTest() {
        Pageable page = PaginationMapper.toPage(20, 20);
        assertEquals(1, page.getPageNumber());
        assertEquals(20, page.getPageSize());
    }

    @Test
    void toPageTestNull() {
        Pageable page = PaginationMapper.toPage(null, 20);
        assertNull(page);
    }

    @Test
    void toPageFromErrorTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> PaginationMapper.toPage(-1, 20));

        assertEquals("The from parameter cannot be less than 0", exception.getMessage());
    }

    @Test
    void toPageSizeErrorTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> PaginationMapper.toPage(20, 0));

        assertEquals("The size parameter must be positive", exception.getMessage());
    }

}