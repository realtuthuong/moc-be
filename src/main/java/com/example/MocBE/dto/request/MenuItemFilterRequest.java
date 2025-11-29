package com.example.MocBE.dto.request;

import com.example.MocBE.enums.MenuItemStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class MenuItemFilterRequest {
    private String name;
    private UUID categoryId;
    private MenuItemStatus status;
//    private UUID locationId;
}
