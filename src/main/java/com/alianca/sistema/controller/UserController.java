package com.alianca.sistema.controller;

import com.alianca.sistema.dto.*;
import com.alianca.sistema.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import com.alianca.sistema.service.JwtService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }


    @PostMapping
    public ResponseEntity<UserResponseDTO> create(
            @Valid
            @RequestBody UserRequestDTO request
    ) {
        UserResponseDTO user = userService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        userService.authenticate(request);

        String token = jwtService.generateToken(request.email());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> findCurrentUser(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(
                userService.findByEmail(jwt.getSubject())
        );
    }
    @PatchMapping("/me")
    public ResponseEntity<UserResponseDTO> updateCurrentUser(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserUpdateDTO request
    ) {
        return ResponseEntity.ok(
                userService.updateName(jwt.getSubject(), request)
        );
    }

}