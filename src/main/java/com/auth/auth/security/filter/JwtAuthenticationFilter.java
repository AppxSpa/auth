package com.auth.auth.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.auth.auth.dto.AuthenticationResponse;
import com.auth.auth.dto.PerfilDto;
import com.auth.auth.utils.PerfilMapper;
import com.auth.auth.entities.Perfil;
import com.auth.auth.entities.Sistema;
import com.auth.auth.entities.Usuario;
import com.auth.auth.entities.UsuarioSistemaPerfil;
import com.auth.auth.repositories.UsuarioRepository;
import com.auth.auth.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.auth.auth.security.TokenJwtConfig.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;
    private final PlatformTransactionManager transactionManager;
    private static final Logger log = LoggerFactory
            .getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
            UsuarioRepository usuarioRepository, PlatformTransactionManager transactionManager) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        Usuario user = null;
        String username = null;
        String password = null;
        

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
            
            username = user.getUsername();
            password = user.getPassword();

        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        return authenticationManager.authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();
        String username = user.getUsername();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        List<PerfilDto> perfilesDto = transactionTemplate.execute(status -> {
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("no existe el usuario"));

            // LOG: imprimir perfiles y sistemas relacionados para depuración
            try {
                log.info("Login usuario='{}' id={} - perfiles asociados:", username, usuario.getId());
                if (usuario.getAccesosSistemas() != null) {
                    for (UsuarioSistemaPerfil acceso : usuario.getAccesosSistemas()) {
                        Perfil p = acceso.getPerfil();
                        Sistema s = acceso.getSistema();
                        String sistemaInfo = s != null
                                ? String.format("sistema[id=%d,name=%s]", s.getId(), s.getNombre())
                                : "sistema=null";
                        log.info("  perfil[id={},name={}] -> {}", p.getId(), p.getNombre(), sistemaInfo);
                        if (p.getModulos() != null) {
                            p.getModulos().forEach(m -> log.info("    modulo[id={},nombre={}]", m.getId(), m.getNombre()));
                        }
                    }
                }
            } catch (Exception ex) {
                log.warn("Error al loggear perfiles del usuario {}: {}", username, ex.getMessage());
            }

            List<Perfil> perfilesUsuario = usuario.getAccesosSistemas().stream().map(UsuarioSistemaPerfil::getPerfil).toList();

            return perfilesUsuario.stream().map(PerfilMapper::toDto).toList();
        });

        Claims claims = Jwts.claims()
                .add("authorities", new ObjectMapper().writeValueAsString(roles))
                .add("username", username).build();

        SecretKey secretKey = jwtUtils.getSecretKey();

        String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .issuedAt(new Date())
                .signWith(secretKey)
                .compact();

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

        boolean isFunc = roles.stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_FUNC"));

        AuthenticationResponse body = new AuthenticationResponse(token, true, isFunc, perfilesDto);

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        AuthenticationResponse body = new AuthenticationResponse("", false, false);

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(CONTENT_TYPE);

    }

}
