package com.auth.auth.services;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.auth.auth.configuration.ApiProperties;
import com.auth.auth.dto.DepartamentoResponse;
import com.auth.auth.services.interfaces.ApiDepartamentoService;

import reactor.core.publisher.Mono;

@Service
public class ApiDepartamentoServiceImpl implements ApiDepartamentoService {
    private final WebClient webClientPersona;

    public ApiDepartamentoServiceImpl(WebClient.Builder webClientBuilder, ApiProperties apiProperties) {
        this.webClientPersona = webClientBuilder.baseUrl(apiProperties.getParamUrl()).build();
    }

    @Override
    public DepartamentoResponse getDepartamentoById(Long id) {
        return webClientPersona.get()
                .uri("/api/param/departamento/detail/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(DepartamentoResponse.class)
                .onErrorResume(Exception.class, e -> Mono.empty())
                .block();
    }

}
