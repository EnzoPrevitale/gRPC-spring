package com.example.grpcspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.grpc.client.ImportGrpcClients;

@SpringBootApplication
@ImportGrpcClients()
public class GRpcSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(GRpcSpringApplication.class, args);
    }

}
