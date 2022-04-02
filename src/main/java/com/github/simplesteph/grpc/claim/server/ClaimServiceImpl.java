package com.github.simplesteph.grpc.claim.server;

import com.proto.claim.Claim;
import com.proto.claim.*;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;

import java.util.stream.Stream;

public class ClaimServiceImpl extends ClaimServiceGrpc.ClaimServiceImplBase {

    @Override
    public void updateClaim(Claim request, StreamObserver<Claim> responseObserver) {
        // Log the incoming request
        System.out.println("Update req received for: " + request);

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
