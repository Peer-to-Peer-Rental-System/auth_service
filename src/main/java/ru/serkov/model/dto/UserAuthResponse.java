package ru.serkov.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class UserAuthResponse {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
