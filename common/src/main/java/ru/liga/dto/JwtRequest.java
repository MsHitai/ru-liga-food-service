package ru.liga.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class JwtRequest {

    @NotEmpty(message = "Имя пользователя не может быть пустым")
    private String username;

    @NotEmpty(message = "Пароль не может быть пустым")
    private String password;
}