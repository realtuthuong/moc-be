package com.example.MocBE.dto.request;


import com.example.MocBE.enums.TableStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableFilterRequest {
    private String name;
    private TableStatus tableStatus;
    private String location;
}
