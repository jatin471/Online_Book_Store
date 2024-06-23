package com.example.bookstore;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class BookServiceServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                .addService(new BookServiceImpl())
                .build();

        server.start();
        System.out.println("Server started on port 8080");
        server.awaitTermination();
    }
}
