package com.example.grpcspring.controllers;

import com.example.grpcspring.client.MessagerClient;
import com.example.grpcspring.dtos.MessageDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessagerClient client;

    public MessageController(MessagerClient client) {
        this.client = client;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getMessages() {
        return client.getMessages();
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MessageDto dto) {
        client.sendMessage(dto.message());
    }
}
