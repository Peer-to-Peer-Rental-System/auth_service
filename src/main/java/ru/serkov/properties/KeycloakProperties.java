package ru.serkov.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value = "app.keycloak")
@Getter
@Setter
public class KeycloakProperties {
    private String serverUrl;

    private String realm;

    private String username;

    private String password;

    private String grantType;

    private String clientId;

}
