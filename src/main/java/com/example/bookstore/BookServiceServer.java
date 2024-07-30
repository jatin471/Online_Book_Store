//package com.example.bookstore;
//
//import io.grpc.Server;
//import io.grpc.ServerBuilder;
//
//import java.io.IOException;
//
//public class BookServiceServer {
//    public static void main(String[] args) throws IOException, InterruptedException {
//
//        Server server = ServerBuilder.forPort(8080)
//                .addService(new BookServiceImpl())
//                .build();
//
//        server.start();
//        System.out.println("Server started on port 8080");
//        server.awaitTermination();
//    }
//}








//package com.example.bookstore;
//
//
//import io.grpc.Server;
//import io.grpc.ServerBuilder;
//import com.typesafe.config.Config;
//import com.typesafe.config.ConfigFactory;
//import org.hypertrace.core.documentstore.Datastore;
//import org.hypertrace.core.documentstore.DatastoreProvider;
//import org.hypertrace.core.documentstore.model.config.ConnectionConfig;
//import org.hypertrace.core.documentstore.model.config.DatabaseType;
//import org.hypertrace.core.documentstore.model.config.DatastoreConfig;
//
//import java.io.IOException;
//import java.util.Map;
//
//public class BookServiceServer {
//
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//        // Initialize MongoDB datastore
//        Config config = ConfigFactory.parseMap(Map.of(
//                "type", "MONGO",
//                "mongo.uri", "mongodb://localhost:28018",
//                "mongo.database", "bookstore"
//        ));
////        (DatabaseType.MONGO, config)
//
//
//
//        ConnectionConfig connectionConfig = ConnectionConfig.builder()
//                .connectionUrl(config.getString("mongo.uri"))
//                .database(config.getString("mongo.database"))
//                .build();
//
//        DatastoreConfig datastoreConfig =  DatastoreConfig.builder().connectionConfig(ConnectionConfig.builder().build()).type(DatabaseType.MONGO).build();
//        Datastore datastore = DatastoreProvider.getDatastore(datastoreConfig);
//
//        Server server = ServerBuilder.forPort(8080)
//                .addService(new BookServiceImpl(datastore))
//                .build();
//
//        server.start();
//        System.out.println("Server started on port 8080");
//        server.awaitTermination();
//    }
//}








package com.example.bookstore;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.hypertrace.core.documentstore.Collection;
import org.hypertrace.core.documentstore.Datastore;
import org.hypertrace.core.documentstore.DatastoreProvider;
import org.hypertrace.core.documentstore.model.config.ConnectionConfig;
import org.hypertrace.core.documentstore.model.config.DatabaseType;
import org.hypertrace.core.documentstore.model.config.DatastoreConfig;
import org.hypertrace.core.documentstore.model.config.Endpoint;

import java.io.IOException;
import java.util.Map;

public class BookServiceServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Initialize MongoDB configuration
        Config config = ConfigFactory.parseMap(Map.of(
                "type", "MONGO",
                "mongo.uri", "mongodb://localhost:28018",
                "mongo.database", "bookstore"
        ));

        // Create Endpoint and ConnectionConfig
        Endpoint endpoint = Endpoint.builder()
                .host("localhost")
                .port(28018)
                .build();

        ConnectionConfig connectionConfig = ConnectionConfig.builder()
                .type(DatabaseType.MONGO)
                .addEndpoint(endpoint)
                .build();

        DatastoreConfig datastoreConfig = DatastoreConfig.builder()
                .connectionConfig(connectionConfig)
                .type(DatabaseType.MONGO)
                .build();

        Datastore datastore = DatastoreProvider.getDatastore(datastoreConfig);
        Collection collection = datastore.getCollection("books");
        System.out.println("MongoDB connection successful");

        // Start gRPC server
        Server server = ServerBuilder.forPort(8080)
                .addService(new BookServiceImpl()) // Assuming BookServiceImpl takes Datastore in the constructor
                .build();

        server.start();
        System.out.println("Server started on port 8080");
        server.awaitTermination();
    }
}