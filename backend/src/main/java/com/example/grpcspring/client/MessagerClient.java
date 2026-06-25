package com.example.grpcspring.client;

import com.example.grpcspring.GreeterGrpc;
import com.example.grpcspring.MessageRequest;
import com.example.grpcspring.MessageResponse;
import com.example.grpcspring.MessagerGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class MessagerClient {
    private final MessagerGrpc.MessagerBlockingStub blockingStub;

    public MessagerClient() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        this.blockingStub = MessagerGrpc.newBlockingStub(channel);
    }

    /*public String sendMessage(String message) {
        MessageRequest request = MessageRequest.newBuilder().setMessage(message).build();
        MessageResponse response = blockingStub.sendMessage(request);
    }*/
}
