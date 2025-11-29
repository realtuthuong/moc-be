package com.example.MocBE.dto.request;

import com.example.MocBE.enums.TableStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TableRequest {
    private UUID id;

    @NotBlank
    private String name;

    @NotNull
    private TableStatus status;

    @Min(1)
    private byte capacity;

    @NotNull
    private UUID locationId;
}
