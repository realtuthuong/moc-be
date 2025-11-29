package com.example.MocBE.service.imp;

import com.example.MocBE.dto.request.ChatRequest;
import com.example.MocBE.dto.response.ChatResponse;
import com.example.MocBE.model.ChatMessage;
import com.example.MocBE.model.Conversation;
import com.example.MocBE.repository.ChatMessageRepository;
import com.example.MocBE.repository.ConversationRepository;
import com.example.MocBE.service.ChatService;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final OpenAiService openAiService;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.base-url}")
    private String baseUrl;

    @Override
    public ChatResponse processMessage(ChatRequest request) {
        Conversation conversation = conversationRepository
                .findTopByUserIdOrderByLastUpdatedDesc(request.getUserId())
                .orElseGet(() -> conversationRepository.save(
                        Conversation.builder()
                                .userId(request.getUserId())
                                .startTime(LocalDateTime.now())
                                .lastUpdated(LocalDateTime.now())
                                .status("ACTIVE")
                                .build()
                ));

        // Gọi GPT để tạo câu trả lời
        String reply = generateReplyFromGPT(request.getMessage());

        chatMessageRepository.save(ChatMessage.builder()
                .sender("user")
                .message(request.getMessage())
                .timestamp(LocalDateTime.now())
                .conversation(conversation)
                .build());

        chatMessageRepository.save(ChatMessage.builder()
                .sender("ai")
                .message(reply)
                .timestamp(LocalDateTime.now())
                .conversation(conversation)
                .build());

        conversation.setLastUpdated(LocalDateTime.now());
        conversationRepository.save(conversation);

        return ChatResponse.builder()
                .reply(reply)
                .conversationId(conversation.getId())
                .build();
    }

    private String generateReplyFromGPT(String message) {
        String apiUrl = baseUrl + "/chat/completions";

        OkHttpClient client = new OkHttpClient();

        // Tạo JSON request body
        String jsonBody = """
        {
          "model": "gpt-4o-mini",
          "messages": [
            {
              "role": "system",
              "content": "Bạn là trợ lý AI của nhà hàng Mộc, chuyên tư vấn đặt bàn, menu món ăn, giờ mở cửa, khuyến mãi, và hỗ trợ khách hàng. Hãy trả lời ngắn gọn, tự nhiên, lịch sự, bằng tiếng Việt."
            },
            {
              "role": "user",
              "content": "%s"
            }
          ],
          "temperature": 0.7,
          "max_tokens": 200
        }
    """.formatted(message);

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody, mediaType);


        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("GPT request failed: " + response.code() + " " + response.message());
                return "Xin lỗi, tôi hiện không thể trả lời. Vui lòng thử lại sau.";
            }

            String resString = response.body().string();
            JSONObject resJson = new JSONObject(resString);
            return resJson
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Xin lỗi, hiện hệ thống đang gặp sự cố. Vui lòng thử lại sau.";
        }
    }

}
