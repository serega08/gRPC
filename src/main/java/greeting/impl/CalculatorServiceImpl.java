package greeting.impl;

import com.proto.calculator.CalculatorRequest;
import com.proto.calculator.CalculatorResponse;
import com.proto.calculator.CalculatorServiceGrpc.CalculatorServiceImplBase;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceImplBase {

    @Override
    public void calculate(CalculatorRequest request, StreamObserver<CalculatorResponse> responseObserver) {
        int result = request.getFirstElement() + request.getSecondElement();

        responseObserver.onNext(CalculatorResponse.newBuilder().setResult(result).build());
        responseObserver.onCompleted();
        super.calculate(request, responseObserver);
    }
}
