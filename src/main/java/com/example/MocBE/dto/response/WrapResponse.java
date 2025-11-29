package com.example.MocBE.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WrapResponse<T> {
    String requestId;
    String requestAction;
    boolean error;
    String errorType;
    String errorReason;
    String toastMessage;
    Object errorFields;
    T objectJson;
}