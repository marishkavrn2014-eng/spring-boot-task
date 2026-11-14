package com.example.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(8080)
                .addService(new PingServiceImpl())
                .build()
                .start();

        System.out.println("gRPC server started on port 8080");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping gRPC server...");
            server.shutdown();
        }));

        server.awaitTermination();
    }
}
