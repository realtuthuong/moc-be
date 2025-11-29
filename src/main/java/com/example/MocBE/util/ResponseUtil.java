package com.example.MocBE.util;

import com.example.MocBE.dto.response.SuccessResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ResponseUtil {


     //Trả về response thành công mặc định với HttpStatus.CREATED (201)
    public static SuccessResponse success(String message, String path) {
        return new SuccessResponse(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                message,
                path
        );
    }

    //Trả về response thành công với status tùy chọn
    public static SuccessResponse success(HttpStatus status, String message, String path) {
        return new SuccessResponse(
                LocalDateTime.now(),
                status.value(),
                message,
                path
        );
    }
}
