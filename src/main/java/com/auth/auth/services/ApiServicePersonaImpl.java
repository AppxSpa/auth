package com.auth.auth.services;


import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.auth.auth.api.PersonaRequest;
import com.auth.auth.api.PersonaResponse;
import com.auth.auth.configuration.ApiProperties;
import com.auth.auth.services.interfaces.ApiServicePersona;
import com.auth.auth.exceptions.SistemaException;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class ApiServicePersonaImpl implements ApiServicePersona {

    private final WebClient webClientPersona;

    public ApiServicePersonaImpl(WebClient.Builder webClientBuilder, ApiProperties apiProperties) {
        this.webClientPersona = webClientBuilder.baseUrl(apiProperties.getPersonaUrl()).build();
    }

    @Override
    public void createPersona(PersonaRequest persona) {
        try {
            webClientPersona.post()
                    .uri("/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(persona)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> response.bodyToMono(String.class)
                                    .flatMap(error -> Mono.error(new SistemaException("Error en la API de persona: " + error))))
                    .bodyToMono(Void.class) //
                    .block();

        } catch (Exception e) {
            throw new SistemaException("Fallo de comunicación al crear persona: " + e.getMessage());
        }

    }

    @Override
    public PersonaResponse getPersonaInfo(Integer rut) {
        return webClientPersona.get()
                .uri("/{rut}", rut)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(PersonaResponse.class)
                .onErrorResume(Exception.class, e -> Mono.empty())
                .block();
    }

}
