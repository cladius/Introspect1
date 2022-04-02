package com.github.simplesteph.grpc.claim.client;

import com.proto.claim.*;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import javax.net.ssl.SSLException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClaimClient {

    public static void main(String[] args) throws SSLException {
        System.out.println("Hello I'm a gRPC client");

        com.github.simplesteph.grpc.claim.client.ClaimClient main = new com.github.simplesteph.grpc.claim.client.ClaimClient();
        main.run();
    }

    private void run() throws SSLException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();


        doUnaryCall(channel);

//        doBiDiStreamingCall(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();

    }

    private void doUnaryCall(ManagedChannel channel) {
        // created a claim service client (blocking - synchronous)
        ClaimServiceGrpc.ClaimServiceBlockingStub claimClient = ClaimServiceGrpc.newBlockingStub(channel);

        // Unary
        // created a protocol buffer claiming message
        Claim request = Claim.newBuilder()
                .setCustomerId(3)
                .setCustomerName("John Smith")
                .setStatus("Open")
                .setId(1)
                .build();

        // call the RPC and get back a ClaimResponse (protocol buffers)
        Claim response = claimClient.updateClaim(request);

        System.out.println(response);
    }

//    private void doBiDiStreamingCall(ManagedChannel channel) {
//        ClaimServiceGrpc.ClaimServiceStub asyncClient = ClaimServiceGrpc.newStub(channel);
//
//        CountDownLatch latch = new CountDownLatch(1);
//
//        StreamObserver<ClaimEveryoneRequest> requestObserver = asyncClient.claimEveryone(new StreamObserver<ClaimEveryoneResponse>() {
//            @Override
//            public void onNext(ClaimEveryoneResponse value) {
//                System.out.println("Response from server: " + value.getResult());
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                latch.countDown();
//            }
//
//            @Override
//            public void onCompleted() {
//                System.out.println("Server is done sending data");
//                latch.countDown();
//            }
//        });
//
//        Arrays.asList("Stephane", "John", "Marc", "Patricia").forEach(
//                name -> {
//                    System.out.println("Sending: " + name);
//                    requestObserver.onNext(ClaimEveryoneRequest.newBuilder()
//                            .setClaiming(Claiming.newBuilder()
//                                    .setFirstName(name))
//                            .build());
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//        );
//
//        requestObserver.onCompleted();
//
//        try {
//            latch.await(3, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }


}
