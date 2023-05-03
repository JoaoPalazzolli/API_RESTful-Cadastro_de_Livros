package br.com.listadelivros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.listadelivros.data.vo.v1.security.LoginRequest;
import br.com.listadelivros.data.vo.v1.security.RegisterRequest;
import br.com.listadelivros.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServices services;
    
    @SuppressWarnings("rawtypes")
    @Operation(summary = "Register a user and returns a token")
    @PostMapping(value = "/register")
    public ResponseEntity register(@RequestBody RegisterRequest request){
        return services.register(request);
    }

    @SuppressWarnings("rawtypes")
	@Operation(summary = "Authenticates a user and returns a token")
    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody LoginRequest request){
        return services.login(request);
    }
}
