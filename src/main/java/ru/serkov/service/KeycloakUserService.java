package ru.serkov.service;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import ru.serkov.exceptions.UserRegisterException;
import ru.serkov.model.UserCredentials;
import ru.serkov.model.dto.UserAuthRequest;
import ru.serkov.model.dto.UserAuthResponse;
import ru.serkov.properties.KeycloakProperties;
import ru.serkov.repository.UserCredentialsRepository;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {
    private final Keycloak keycloak;
    private final KeycloakProperties props;
    private final UserCredentialsRepository userCredentialsRepository;

    @Transactional
    public UserAuthResponse createUser(UserAuthRequest userAuthRequest) {
        String userId = null;
        try {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(userAuthRequest.getUsername());
            user.setEmail(userAuthRequest.getEmail());
            user.setEnabled(true);
            user.setFirstName(userAuthRequest.getFirstName());
            user.setLastName(userAuthRequest.getLastName());

            Response response = keycloak.realm(props.getRealm())
                    .users()
                    .create(user);

            userId = CreatedResponseUtil.getCreatedId(response);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(userAuthRequest.getPassword());

            UserResource userResource = keycloak.realm(props.getRealm())
                    .users()
                    .get(userId);

            userResource.resetPassword(credential);

            UserCredentials userCredentials = UserCredentials.builder()
                    .email(userResource.toRepresentation().getEmail())
                    .phone(userAuthRequest.getPhone())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userCredentialsRepository.save(userCredentials);

            return UserAuthResponse.builder()
                    .id(userCredentials.getId())
                    .email(userResource.toRepresentation().getEmail())
                    .username(userResource.toRepresentation().getUsername())
                    .build();
        } catch (Exception ex) {
            if (Objects.nonNull(userId)) {
                keycloak.realm(props.getRealm())
                        .users()
                        .get(userId)
                        .remove();
            }
            throw new UserRegisterException("User creation process failed", ex);
        }
    }
}
