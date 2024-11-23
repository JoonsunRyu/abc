package com.ssafy.mvc.controller;

import java.util.Map;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@CrossOrigin("*")
public class OllamaAIRestController {
    private final OllamaChatModel chatModel;

    @Autowired
    public OllamaAIRestController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ollama/generate")
    public Map<String,String> generate(@RequestParam(value = "message") String message) {
        return Map.of("generation", chatModel.call(message));
    }

    @GetMapping("/ollama/generateStream")
	public Flux<ChatResponse> generateStream(@RequestParam(value = "message") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }
}
