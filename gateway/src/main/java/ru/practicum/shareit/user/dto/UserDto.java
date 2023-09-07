package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @Size(min = 2, max = 32, message = "Minimum name length is 2 characters，the maximum is 32 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be correct")
    private String email;

}
