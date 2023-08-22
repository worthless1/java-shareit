package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ItemDto {
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 32, message = "Minimum name length is 2 characters，the maximum is 32 characters")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 500, message = "Minimum description length is 500 characters")
    private String description;

    @NotNull(message = "Available cannot be empty")
    private Boolean available;
}
