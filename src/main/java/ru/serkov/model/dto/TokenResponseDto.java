package ru.serkov.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TokenResponseDto {
    private final String accessToken;
    private final Integer expiresIn;
    private final Integer refreshExpiresIn;
    private final String refreshToken;
    private final String tokenType;
    private final Integer notBeforePolicy;
    private final String sessionState;
    private final String scope;
    private final LocalDateTime createdAt;

    @JsonCreator
    public TokenResponseDto(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("expires_in") Integer expiresIn,
            @JsonProperty("refresh_expires_in") Integer refreshExpiresIn,
            @JsonProperty("refresh_token") String refreshToken,
            @JsonProperty("token_type") String tokenType,
            @JsonProperty("not-before-policy") Integer notBeforePolicy,
            @JsonProperty("session_state") String sessionState,
            @JsonProperty("scope") String scope) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshExpiresIn = refreshExpiresIn;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.notBeforePolicy = notBeforePolicy;
        this.sessionState = sessionState;
        this.scope = scope;
        this.createdAt = LocalDateTime.now();
    }

    @JsonIgnore
    public LocalDateTime getDateEnd() {
        if (expiresIn == null) {
            return null;
        }
        return createdAt.plusSeconds(expiresIn);
    }

    @JsonIgnore
    public boolean isExpired() {
        if (expiresIn == null || createdAt == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(getDateEnd());
    }
}