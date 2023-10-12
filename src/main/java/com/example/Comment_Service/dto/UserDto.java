package com.example.Comment_Service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
public class UserDto {

    private long id;
    @NotEmpty(message = "Username must not be empty.")
    private String username;
    @Email
    private String email;
    @Size(min = 4,max = 8,message = "password must be minimum of 3 and maximum of 8 chars.")
    private String password;
    private String firstName;
    private String lastName;
}
