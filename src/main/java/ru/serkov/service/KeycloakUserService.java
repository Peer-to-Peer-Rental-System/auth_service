package ru.serkov.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import ru.serkov.exceptions.UserRegisterException;
import ru.serkov.model.UserCredentials;
import ru.serkov.model.dto.TokenResponseDto;
import ru.serkov.model.dto.UserAuthRequest;
import ru.serkov.model.dto.UserAuthResponse;
import ru.serkov.repository.UserCredentialsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {
    private static TokenResponseDto tokenDto = null;

    private final UserCredentialsRepository userCredentialsRepository;
    private final WebClientKeycloakService webClientKeycloakService;


    @Transactional
    public UserAuthResponse createUser(UserAuthRequest userAuthRequest) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setId(UUID.randomUUID().toString());
        credentialRepresentation.setValue(userAuthRequest.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(userAuthRequest.getUsername());
        user.setEmail(userAuthRequest.getEmail());
        user.setEnabled(true);
        user.setFirstName(userAuthRequest.getFirstName());
        user.setLastName(userAuthRequest.getLastName());
        user.setCredentials(List.of(credentialRepresentation));

        UserCredentials userCredentials = UserCredentials.builder()
                .email(userAuthRequest.getEmail())
                .phone(userAuthRequest.getPhone())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userCredentialsRepository.save(userCredentials);

        TokenResponseDto token = null;

        try {
            if (Objects.isNull(tokenDto) || tokenDto.isExpired()) {
                tokenDto = webClientKeycloakService.getAdminAccessToken();
            }

            webClientKeycloakService.createUser(user,tokenDto);

            token = webClientKeycloakService.getUserAccessToken(userAuthRequest.getUsername(), userAuthRequest.getPassword());

        } catch (Exception ex) {
            throw new UserRegisterException("User creation process failed", ex);
        }

        return UserAuthResponse.builder()
                .id(userCredentials.getId())
                .email(userAuthRequest.getEmail())
                .username(userAuthRequest.getUsername())
                .tokenResponseDto(token)
                .build();

    }

}
