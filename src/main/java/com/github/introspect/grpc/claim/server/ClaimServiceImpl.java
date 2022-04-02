package com.github.introspect.grpc.claim.server;

import com.proto.claim.Claim;
import com.proto.claim.*;
import io.grpc.stub.StreamObserver;

import java.util.Map;
import java.util.HashMap;

public class ClaimServiceImpl extends ClaimServiceGrpc.ClaimServiceImplBase {

    //Holds the claims data.
    Map<Integer, Claim> claims;

    public ClaimServiceImpl() {
        //Set up test data
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

        claims.put(3, Claim.newBuilder()
                .setCustomerId(4)
                .setCustomerName("Clark Kent")
                .setStatus("Open")
                .setId(3)
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
        System.out.println("Update request received for claim (id) = " + request.getId());

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
        System.out.println("Delete request received for id = " + request.getId());

        //Delete the claim from the 'DB'
        Claim deletedClaim = claims.remove(request.getId());

        Claim response;
        if(deletedClaim != null)
            // Create the response
            response = Claim.newBuilder()
                    .setCustomerId(deletedClaim.getCustomerId())
                    .setStatus(deletedClaim.getStatus())
                    .setId(deletedClaim.getId())
                    .setCustomerName(deletedClaim.getCustomerName())
                    .build();
        else
            response = Claim.newBuilder()
                    .setId(0)
                    .setStatus("Not Found")
                    .build();

        // Send the response
        responseObserver.onNext(response);

        // Complete the RPC call
        responseObserver.onCompleted();

        System.out.println("Data post delete for claim (id) = " + request.getId());
        printClaimsData();
    }

    @Override
    public StreamObserver<ClaimId> getClaims(StreamObserver<Claim> responseObserver) {
        StreamObserver<ClaimId> requestObserver = new StreamObserver<ClaimId>() {
            @Override
            public void onNext(ClaimId claimId) {
                int id = claimId.getId();

                Claim response = claims.get(id);

                if(response == null) {
                    System.out.println("No claim found.");
                    response = Claim.newBuilder()
                            .setId(0)
                            .setStatus("Not Found")
                            .build();
                }

                responseObserver.onNext(response);
                System.out.println("Returned response for incoming claim id : " + id);
            }

            @Override
            public void onError(Throwable t) {
                // do nothing
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }


}
