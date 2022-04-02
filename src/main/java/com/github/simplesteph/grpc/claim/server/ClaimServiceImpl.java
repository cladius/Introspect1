package com.github.simplesteph.grpc.claim.server;

import com.proto.claim.Claim;
import com.proto.claim.*;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;

import java.util.Map;
import java.util.HashMap;
import java.util.stream.Stream;

public class ClaimServiceImpl extends ClaimServiceGrpc.ClaimServiceImplBase {

    //Holds the claims data.
    Map<Integer, Claim> claims;

    //Set up test data
    public ClaimServiceImpl() {
        claims = new HashMap<>();

        claims.put(1, Claim.newBuilder()
                .setCustomerId(3)
                .setCustomerName("Bruce Wayne")
                .setStatus("Open")
                .setId(1)
                .build());

        claims.put(2, Claim.newBuilder()
                .setCustomerId(3)
                .setCustomerName("Bruce Wayne")
                .setStatus("Closed")
                .setId(2)
                .build());

        System.out.println("Data post setup.");
        printClaimsData();
    }

    private void printClaimsData(){
        System.out.println("Claims in the 'DB': ");
        for(Claim claim : claims.values()){
            System.out.println(claim);
        }
    }

    @Override
    public void updateClaim(Claim request, StreamObserver<Claim> responseObserver) {
        // Log the incoming request
        System.out.println("Update request received for: " + request);

        //Update the claim in the 'DB'
        claims.put(request.getId(), request);

        // Create the response
        Claim response = Claim.newBuilder()
                .setCustomerId(request.getCustomerId())
                .setStatus(request.getStatus())
                .setId(request.getId())
                .setCustomerName(request.getCustomerName())
                .build();

        // Send the response
        responseObserver.onNext(response);

        // Complete the RPC call
        responseObserver.onCompleted();

        System.out.println("Data post update for claim (id) = " + request.getId());
        printClaimsData();
    }

    @Override
    public void deleteClaim(ClaimId request, StreamObserver<Claim> responseObserver) {
        // Log the incoming request
        System.out.println("Delete request received for: " + request);

        //Delete the claim from the 'DB'
        Claim deletedClaim = claims.remove(request.getId());

        // Create the response
        Claim response = Claim.newBuilder()
                .setCustomerId(deletedClaim.getCustomerId())
                .setStatus(deletedClaim.getStatus())
                .setId(deletedClaim.getId())
                .setCustomerName(deletedClaim.getCustomerName())
                .build();

        // Send the response
        responseObserver.onNext(response);

        // Complete the RPC call
        responseObserver.onCompleted();

        System.out.println("Data post delete for claim (id) = " + request.getId());
        printClaimsData();
    }

//    @Override
//    public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {
//        StreamObserver<GreetEveryoneRequest> requestObserver = new StreamObserver<GreetEveryoneRequest>() {
//            @Override
//            public void onNext(GreetEveryoneRequest value) {
//                String result = "Hello " + value.getGreeting().getFirstName();
//                GreetEveryoneResponse greetEveryoneResponse = GreetEveryoneResponse.newBuilder()
//                        .setResult(result)
//                        .build();
//
//                responseObserver.onNext(greetEveryoneResponse);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                // do nothing
//            }
//
//            @Override
//            public void onCompleted() {
//                responseObserver.onCompleted();
//            }
//        };
//
//        return requestObserver;
//    }


}
