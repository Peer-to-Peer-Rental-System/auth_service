package ru.serkov.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.serkov.model.dto.UserAuthRequest;
import ru.serkov.model.dto.UserAuthResponse;
import ru.serkov.service.KeycloakUserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/api/v1")
public class Controller {
    private final KeycloakUserService keycloakUserService;

    @PostMapping("/register")
    public ResponseEntity<UserAuthResponse> register(@Valid @RequestBody UserAuthRequest userAuthRequest) {
        UserAuthResponse user = keycloakUserService.createUser(userAuthRequest);
        return ResponseEntity
                .ok().build();
    }
}
