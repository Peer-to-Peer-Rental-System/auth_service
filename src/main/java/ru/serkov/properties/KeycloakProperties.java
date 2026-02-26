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
    private Integer timeout;

    private String serverUrl;

    private String grantType;

    private String clientId;

    private String clientSecret;

    private String realm;

}
