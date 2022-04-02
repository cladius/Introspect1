package com.github.simplesteph.grpc.claim.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.File;
import java.io.IOException;

public class ClaimServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Start introspecting...");

        // plaintext server
        Server server = ServerBuilder.forPort(50051)
                .addService(new ClaimServiceImpl())
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received Shutdown Request");
            server.shutdown();
            System.out.println("Successfully stopped the server");
        }));

        server.awaitTermination();
    }

}
