package com.example.grpc;

import io.grpc.stub.StreamObserver;

public class PingServiceImpl extends PingServiceGrpc.PingServiceImplBase {
    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        System.out.println("Received: " + request.getMessage() + " at " + request.getTimestamp());

        String message = request.getMessage();
        PingResponse response = PingResponse.newBuilder().setMessage("Pong: " + message).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
