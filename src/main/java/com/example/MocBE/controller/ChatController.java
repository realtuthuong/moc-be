package com.example.MocBE.controller;
import com.example.MocBE.dto.request.ChatRequest;
import com.example.MocBE.dto.response.ChatResponse;
import com.example.MocBE.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public Map<String, Object> chat(@RequestBody ChatRequest request) {
        ChatResponse response = chatService.processMessage(request);
        return Map.of("objectJson", response);
    }
}
