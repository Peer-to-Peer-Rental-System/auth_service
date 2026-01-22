package ru.serkov.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import ru.serkov.model.dto.UserAuthRequest;
import ru.serkov.model.dto.UserAuthResponse;
import ru.serkov.properties.KeycloakProperties;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {
    private final Keycloak keycloak;
    private final KeycloakProperties props;

    public UserAuthResponse createUser(UserAuthRequest userAuthRequest) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userAuthRequest.getUsername());
        user.setEmail(userAuthRequest.getEmail());
        user.setEnabled(true);
        user.setFirstName(userAuthRequest.getFirstName());
        user.setLastName(userAuthRequest.getLastName());

        Response response = keycloak.realm(props.getRealm())
                .users()
                .create(user);

        String userId = CreatedResponseUtil.getCreatedId(response);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userAuthRequest.getPassword());

        UserResource userResource = keycloak.realm(props.getRealm())
                .users()
                .get(userId);

        userResource.resetPassword(credential);

        return UserAuthResponse.builder()
                .id(1L)
                .email(userResource.toRepresentation().getEmail())
                .username(userResource.toRepresentation().getUsername())
                .build();


    }
}
