package com.alianca.sistema.dto;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateDTO(
        @NotBlank(message = "O nome não pode estar vazio")
        String name
) {
}