package ru.liga.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.liga.dto.JwtRequest;
import ru.liga.service.impl.AuthService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@Valid @RequestBody JwtRequest authRequest) {
        log.info("Поступил запрос на получения токена от {}", authRequest.getUsername());
        return authService.createAuthToken(authRequest);
    }
}
