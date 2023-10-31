package ru.liga.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.liga.dto.RegDto;
import ru.liga.service.UserService;

@RestController
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody RegDto request) {
        return userService.createUser(request);
    }
}
