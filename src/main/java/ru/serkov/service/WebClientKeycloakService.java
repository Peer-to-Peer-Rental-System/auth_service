package ru.serkov.service;

import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.serkov.model.dto.TokenResponseDto;
import ru.serkov.properties.KeycloakProperties;

@Service
@RequiredArgsConstructor
public class WebClientKeycloakService {
    private final WebClient webClient;
    private final KeycloakProperties props;

    public TokenResponseDto getAdminAccessToken() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", props.getGrantType());
        body.add("client_id", props.getClientId());
        body.add("client_secret", props.getClientSecret());

        return webClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", props.getRealm())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .bodyToMono(TokenResponseDto.class)
                .block();
    }

    public void createUser(UserRepresentation user, TokenResponseDto tokenDto) {
        webClient
                .post()
                .uri("/admin/realms/{realm}/users", props.getRealm())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .bodyValue(user)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public TokenResponseDto getUserAccessToken(String userName, String password) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", props.getClientId());
        body.add("username", userName);
        body.add("password", password);
        body.add("client_secret", props.getClientSecret());

        return webClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", props.getRealm())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .bodyToMono(TokenResponseDto.class)
                .block();
    }
}
