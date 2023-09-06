package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomExceptionHandlerTest {

    @InjectMocks
    private CustomExceptionHandler customExceptionHandler;

    @Mock
    private UnsupportedStatusException unsupportedStatusException;

    @Test
    void testHandleUnsupportedStatusException() {

        String exceptionMessage = "Test exception message";

        when(unsupportedStatusException.getLocalizedMessage()).thenReturn(exceptionMessage);

        ResponseEntity<CustomError> responseEntity = customExceptionHandler.handleUnsupportedStatusException(unsupportedStatusException);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        CustomError responseBody = responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, responseBody.getHttpStatus());
    }

}