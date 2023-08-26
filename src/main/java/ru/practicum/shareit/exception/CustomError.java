package ru.practicum.shareit.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
public class CustomError {
    private final HttpStatus httpStatus;
    private final String error;
}
