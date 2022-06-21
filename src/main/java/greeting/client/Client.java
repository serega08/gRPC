package greeting.client;

import com.proto.calculator.CalculatorRequest;
import com.proto.calculator.CalculatorResponse;
import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.greeting.GreetingRequest;
import com.proto.greeting.GreetingResponse;
import com.proto.greeting.GreetingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            System.out.println("Enter function...");
            return;
        }

        ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 50051)
            .usePlaintext()
            .build();

        switch (args[0]) {
            case "greet" -> doGreat(channel);
            case "calculate" -> doCalculation(channel);
            case "great_stream" -> doGreatStream(channel);
            case "great_stream_request" -> doGreatRequestStream(channel);
            default -> System.out.println("Invalid function: " + args[0]);
        }

        System.out.println("shutting down client");
        channel.shutdown();
    }

    private static void doGreatRequestStream(ManagedChannel channel) throws InterruptedException {
        System.out.println("In doGreatRequestStream");

        GreetingServiceGrpc.GreetingServiceStub stub = GreetingServiceGrpc.newStub(channel);
        List<String> names = List.of("Alex", "Bob", "John");
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GreetingRequest> stream = stub.greatStreamRequest(
            new StreamObserver<GreetingResponse>() {
                @Override
                public void onNext(GreetingResponse response) {
                    System.out.println(response.getResult());
                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onCompleted() {
                    latch.countDown();
                }
            });

        names.forEach(name -> {
            stream.onNext(GreetingRequest.newBuilder().setFirstName(name).build());
        });
        stream.onCompleted();
        latch.await(3, TimeUnit.SECONDS);
    }

    private static void doGreatStream(ManagedChannel channel) {
        System.out.println("In doGreatStream");
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        stub.greatStreamResponse(GreetingRequest.newBuilder().setFirstName("Joe").build())
            .forEachRemaining(System.out::println);
    }

    private static void doCalculation(ManagedChannel channel) {
        System.out.println("In doCalculation");
        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub = CalculatorServiceGrpc.newBlockingStub(channel);
        CalculatorResponse response = stub.calculate(
            CalculatorRequest.newBuilder().setFirstElement(1).setSecondElement(1).build());

        System.out.println("Sum of operation is " + response.getResult());
    }

    private static void doGreat(ManagedChannel channel) {
        System.out.println("In doGreat");
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        GreetingResponse response = stub.great(GreetingRequest.newBuilder().setFirstName("Jon doe").build());

        System.out.println(response.getResult());
    }
}
