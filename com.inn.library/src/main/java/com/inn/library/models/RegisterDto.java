package com.inn.library.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterDto {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private String address;

    @NotEmpty
    @Size(min = 6, message ="Password must be at least 6 characters")
    private String password;

}
