package ru.serkov.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ru.serkov.properties.KeycloakProperties;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {

    private final KeycloakProperties props;

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, props.getTimeout())
                .responseTimeout(Duration.ofMillis(props.getTimeout()))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(props.getTimeout(), TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(props.getTimeout(), TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(props.getServerUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
