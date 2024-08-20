package com.example.apiestoque.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class IdDTO {
    @NotNull(message = "Informe o ID")
    @Positive(message = "O ID deve ser um número positivo")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}