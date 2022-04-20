package com.chivapchichi.model;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class Users {

    @Email(message = "Не правильный формат email")
    private String userName;

    private String password;

    private String role;
}
