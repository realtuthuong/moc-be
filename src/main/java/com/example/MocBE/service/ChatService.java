package com.example.MocBE.service;

import com.example.MocBE.dto.request.ChatRequest;
import com.example.MocBE.dto.response.ChatResponse;

public interface ChatService {
    ChatResponse processMessage(ChatRequest request);
}
