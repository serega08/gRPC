package greeting.server;

import greeting.impl.CalculatorServiceImpl;
import greeting.impl.GreetingServiceImpl;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class Server {

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 50051;

        io.grpc.Server server = ServerBuilder
            .forPort(port)
            .addService(new GreetingServiceImpl())
            .addService(new CalculatorServiceImpl())
            .build();

        server.start();
        System.out.println("Server started");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received shutdown hook...");
            server.shutdown();
            System.out.println("Server stopped");
        }));

        server.awaitTermination();
    }
}
