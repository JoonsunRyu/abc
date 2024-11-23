package com.ssafy.mvc.controller;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin("*")
public class OpenAIRestController {

    private final OpenAiChatModel chatModel;
    private final Map<String, StringBuilder> conversationHistory = new ConcurrentHashMap<>();

    @Autowired
    public OpenAIRestController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/openai/generate")
    public Map<String, String> generate(@RequestParam(value = "message") String message,
                                      @RequestParam(value = "userId") String userId,
                                      @RequestParam(value = "reset", required = false, defaultValue = "false") boolean reset) {

        

        // 사용자의 대화 기록 가져오기 (없으면 새로 생성)
        StringBuilder userConversation = conversationHistory.computeIfAbsent(userId, k -> new StringBuilder());
        
        // 새로운 대화 내용 추가
        userConversation.append("User: ").append(message).append("\n");
        
        // OpenAI에 전체 대화 내용 전송
        String response = chatModel.call(userConversation.toString());
        
        // 봇의 응답 저장
        userConversation.append("Assistant: ").append(response).append("\n");
        
        return Map.of("generation", response);
    }

    @DeleteMapping("/openai/conversation/{userId}")
    public Map<String, String> clearConversation(@PathVariable String userId) {
        // 대화 기록 완전 삭제
        conversationHistory.remove(userId);
        return Map.of("message", "Conversation history cleared successfully");
    }
}