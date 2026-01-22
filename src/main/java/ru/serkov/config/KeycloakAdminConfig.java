package ru.serkov.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.serkov.properties.KeycloakProperties;

@Configuration
@RequiredArgsConstructor
public class KeycloakAdminConfig {

    private final KeycloakProperties props;

    @Bean
    public Keycloak getAdminKeycloakUser() {
        return KeycloakBuilder.builder()
                .serverUrl(props.getServerUrl())
                .realm(props.getRealm())
                .username(props.getUsername())
                .password(props.getPassword())
                .grantType(props.getGrantType())
                .clientId(props.getClientId())
                .build();
    }
}
