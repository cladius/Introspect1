package com.github.introspect.grpc.claim.client;

import com.proto.claim.*;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import javax.net.ssl.SSLException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClaimClient {

    public static void main(String[] args) throws SSLException {
        System.out.println("Let's begin introspecting on the client side.");

        com.github.introspect.grpc.claim.client.ClaimClient main = new com.github.introspect.grpc.claim.client.ClaimClient();
        main.run();
    }

    private void run() throws SSLException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();


        doUpdateCall(channel);

        doDeleteCall(channel);

        doBiDiStreamingCall(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();

    }

    private void doUpdateCall(ManagedChannel channel) {
        // created a claim service client (blocking - synchronous)
        ClaimServiceGrpc.ClaimServiceBlockingStub claimClient = ClaimServiceGrpc.newBlockingStub(channel);

        // Unary
        // created a protocol buffer claiming message
        Claim request = Claim.newBuilder()
                .setCustomerId(3)
                .setCustomerName("Bruce Wayne")
                .setStatus("Closed")
                .setId(1)
                .build();

        // call the RPC and get back a ClaimResponse (protocol buffers)
        Claim response = claimClient.updateClaim(request);

        System.out.println("Updated claim is: " + response);
    }

    private void doDeleteCall(ManagedChannel channel) {
        // created a claim service client (blocking - synchronous)
        ClaimServiceGrpc.ClaimServiceBlockingStub claimClient = ClaimServiceGrpc.newBlockingStub(channel);

        // Unary
        // created a protocol buffer claiming message
        ClaimId request = ClaimId.newBuilder()
                .setId(2)
                .build();

        // call the RPC and get back a ClaimResponse (protocol buffers)
        Claim response = claimClient.deleteClaim(request);

        System.out.println("Deleted claim is: " + response);
    }

    private void doBiDiStreamingCall(ManagedChannel channel) {
        ClaimServiceGrpc.ClaimServiceStub asyncClient = ClaimServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<ClaimId> requestObserver = asyncClient.getClaims(new StreamObserver<Claim>() {
            @Override
            public void onNext(Claim claim) {
                System.out.println("Response from server: " + claim);
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server is done sending data");
                latch.countDown();
            }
        });

        Arrays.asList(1, 2, 3).forEach(
                claimId -> {
                    System.out.println("Requesting details for claim id : " + claimId);
                    requestObserver.onNext(ClaimId.newBuilder()
                            .setId(claimId)
                            .build());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        requestObserver.onCompleted();

        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
