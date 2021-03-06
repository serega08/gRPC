package greeting.impl;

import com.proto.greeting.GreetingRequest;
import com.proto.greeting.GreetingResponse;
import com.proto.greeting.GreetingServiceGrpc.GreetingServiceImplBase;
import io.grpc.stub.StreamObserver;

public class GreetingServiceImpl extends GreetingServiceImplBase {

    @Override
    public void great(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        responseObserver.onNext(GreetingResponse.newBuilder().setResult("Hello " + request.getFirstName()).build());
        responseObserver.onCompleted();
        super.great(request, responseObserver);
    }

    @Override
    public void greatStreamResponse(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        GreetingResponse response = GreetingResponse.newBuilder().setResult("Hello " + request.getFirstName()).build();
        for (int i = 0; i < 10; i++) {
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
        super.greatStreamResponse(request, responseObserver);
    }

    @Override
    public StreamObserver<GreetingRequest> greatStreamRequest(StreamObserver<GreetingResponse> responseObserver) {
        StringBuilder sb = new StringBuilder();

        return new StreamObserver<GreetingRequest>() {
            @Override
            public void onNext(GreetingRequest request) {
                sb.append("Hello ");
                sb.append(request.getFirstName());
                sb.append("!");
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(GreetingResponse.newBuilder().setResult(sb.toString()).build());
                responseObserver.onCompleted();
            }
        };
    }
}
