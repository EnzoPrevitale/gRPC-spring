package com.example.grpcspring.server;

import com.example.grpcspring.MessageRequest;
import com.example.grpcspring.MessageResponse;
import com.example.grpcspring.MessagerGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class MessagerServiceImpl extends MessagerGrpc.MessagerImplBase {

    @Override
    public StreamObserver<MessageRequest> sendMessage(StreamObserver<MessageResponse> responseObserver) {
        return new StreamObserver<MessageRequest>() {
            @Override
            public void onNext(MessageRequest messageRequest) {
                MessageResponse response = MessageResponse.newBuilder()
                        .setMessage(messageRequest.getMessage())
                        .build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
