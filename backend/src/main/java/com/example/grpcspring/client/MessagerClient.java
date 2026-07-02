package com.example.grpcspring.client;

import com.example.grpcspring.MessageRequest;
import com.example.grpcspring.MessageResponse;
import com.example.grpcspring.MessagerGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class MessagerClient {
    private final MessagerGrpc.MessagerStub stub;
    private final ManagedChannel channel;

    Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    private volatile StreamObserver<MessageRequest> requestObserver;
    private final StreamObserver<MessageResponse> responseObserver = new StreamObserver<MessageResponse>() {
        @Override
        public void onNext(MessageResponse messageResponse) {
            sink.tryEmitNext(messageResponse.getMessage());
        }

        @Override
        public void onError(Throwable throwable) {
            System.err.println("gRPC error: " + throwable.getMessage());
            requestObserver = null;
        }

        @Override
        public void onCompleted() {
            requestObserver = null;
        }
    };

    public MessagerClient(GrpcChannelFactory channelFactory) {
        this.channel = channelFactory.createChannel("static://127.0.0.1:9090");
        this.stub = MessagerGrpc.newStub(channel);
    }

    private synchronized void ensureStreamOpen() {
        if (requestObserver == null) {
            requestObserver = stub.sendMessage(responseObserver);
        }
    }

    public void sendMessage(String message) {
        ensureStreamOpen();
        MessageRequest request = MessageRequest.newBuilder()
                .setMessage(message)
                .build();
        requestObserver.onNext(request);
    }

    public Flux<String> getMessages() {
        return Flux.defer(() -> {
            ensureStreamOpen();
            return sink.asFlux();
        });
    }

    @PreDestroy
    public void shutdown() {
        StreamObserver<MessageRequest> current = requestObserver;
        if(current != null) {
            current.onCompleted();
        }
        channel.shutdown();
    }
}
