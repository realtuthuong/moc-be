package com.example.MocBE.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class UnitDto {
    private UUID id;
    @NotBlank(message = "name not null")
    private String name;
}
