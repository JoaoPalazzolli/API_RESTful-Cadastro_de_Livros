package br.com.listadelivros.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.listadelivros.data.vo.v1.security.LoginRequest;
import br.com.listadelivros.data.vo.v1.security.RegisterRequest;
import br.com.listadelivros.model.User;
import br.com.listadelivros.model.UserRoles;
import br.com.listadelivros.repositories.UserRepository;

@Service
public class AuthServices {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtServices service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @SuppressWarnings("rawtypes")
    public ResponseEntity register(RegisterRequest request) {

        if (checkIfParamsIsNotNull(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .cpf(request.getCpf())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(UserRoles.USER)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .credentialsNonExpired(true)
                .build();

        repository.save(user);

        return ResponseEntity.ok(service.createToken(user));
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity login(LoginRequest request) {
        try {
            if (checkIfParamsIsNotNull(request)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
            }

            var username = request.getUsername();
            var password = request.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            var user = repository.findByUsername(username)
                    .orElseThrow();

            return ResponseEntity.ok(service.createToken(user));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }

    private Boolean checkIfParamsIsNotNull(LoginRequest request) {
        return request.getUsername() == null || request.getPassword() == null || request == null
                || request.getPassword().isBlank() || request.getUsername().isBlank();
    }

    private Boolean checkIfParamsIsNotNull(RegisterRequest request) {
        return request.getUsername() == null || request.getPassword() == null || request == null
                || request.getPassword().isBlank() || request.getUsername().isBlank()
                || request.getCpf() == null || request.getCpf().isBlank() || request.getFirstName() == null
                || request.getFirstName().isBlank() || request.getLastName() == null ||
                request.getLastName().isBlank();
    }
}
