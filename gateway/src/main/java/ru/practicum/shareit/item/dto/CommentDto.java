package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;

    @NotEmpty(message = "The comment text cannot be empty")
    private String text;
    private String authorName;
    private LocalDateTime created;

}
