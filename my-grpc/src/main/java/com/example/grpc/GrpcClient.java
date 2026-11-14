package com.example.grpc;

import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.time.Instant;

public class GrpcClient {
    public static void main(String[] args) {
        String host = System.getenv().getOrDefault("GRPC_HOST", "localhost");
        int port = Integer.parseInt(
                System.getenv().getOrDefault("GRPC_PORT", "8080")
        );

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        Instant now = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();

        try {
            PingServiceGrpc.PingServiceBlockingStub stub =
                    PingServiceGrpc.newBlockingStub(channel);

            PingResponse response = stub.ping(
                    PingRequest.newBuilder()
                            .setMessage("Ping")
                            .setTimestamp(timestamp)
                            .build()
            );

            System.out.println("Server replied: " + response.getMessage());
        } finally {
            channel.shutdown();
        }
    }
}
