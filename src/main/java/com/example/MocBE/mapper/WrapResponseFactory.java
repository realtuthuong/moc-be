package com.example.MocBE.mapper;


import com.example.MocBE.dto.response.WrapResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WrapResponseFactory {

    public <T> WrapResponse<T> success(T data, String action) {
        return WrapResponse.<T>builder()
                .requestId(UUID.randomUUID().toString())
                .requestAction(action)
                .error(false)
                .toastMessage("Success")
                .objectJson(data)
                .build();
    }

    public <T> WrapResponse<T> error(String action, String errorType, String errorReason, Object errorFields) {
        return WrapResponse.<T>builder()
                .requestId(UUID.randomUUID().toString())
                .requestAction(action)
                .error(true)
                .errorType(errorType)
                .errorReason(errorReason)
                .toastMessage("Thất bại")
                .errorFields(errorFields)
                .build();
    }
}
