package com.example.grpcspring.server;

import com.example.grpcspring.GreeterGrpc;
import com.example.grpcspring.HelloRequest;
import com.example.grpcspring.HelloResponse;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class GreeterService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        HelloResponse response = HelloResponse.newBuilder()
                .setMessage("Hello, " + request.getName() + "!")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
