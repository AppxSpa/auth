package com.auth.auth.services;

import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.auth.auth.configuration.ApiProperties;
import com.auth.auth.services.interfaces.ApiServiceMail;
import com.auth.auth.exceptions.SendMailExceptions;

import reactor.core.publisher.Mono;

@Service
public class ApiServiceMailImpl implements ApiServiceMail {

    private final WebClient webClientMail;

    public ApiServiceMailImpl(WebClient.Builder webClientBuilder, ApiProperties apiProperties) {
        this.webClientMail = webClientBuilder.baseUrl(apiProperties.getMailUrl()).build();
    }

    @Override
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            webClientMail.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("send")
                            .queryParam("to", to)
                            .queryParam("subject", subject)
                            .queryParam("templateName", templateName)
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(variables) // JSON con variables para la plantilla
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> response.bodyToMono(String.class)
                                    .flatMap(error -> Mono.error(new SendMailExceptions("Error en la API de correos: " + error))))
                    .bodyToMono(Void.class)
                    .block();

        } catch (Exception e) {
            throw new SendMailExceptions("Error de comunicación al intentar enviar el correo: " + e.getMessage());
        }
    }

}
