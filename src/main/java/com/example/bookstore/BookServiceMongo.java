package com.example.bookstore;
import com.example.bookstore.Bookstore.*;

import com.google.protobuf.TextFormat;
import com.google.protobuf.util.JsonFormat;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.grpc.stub.StreamObserver;
import org.hypertrace.core.documentstore.*;
import org.hypertrace.core.documentstore.model.config.ConnectionConfig;
import org.hypertrace.core.documentstore.model.config.DatabaseType;
import org.hypertrace.core.documentstore.model.config.DatastoreConfig;
import org.hypertrace.core.documentstore.model.config.Endpoint;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;



import com.google.protobuf.util.JsonFormat;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.hypertrace.core.documentstore.model.config.ConnectionConfig;
import org.hypertrace.core.documentstore.model.config.DatabaseType;
import org.hypertrace.core.documentstore.model.config.DatastoreConfig;
import org.hypertrace.core.documentstore.model.config.Endpoint;
import org.hypertrace.core.documentstore.query.Query;

import java.io.IOException;

import java.util.Map;

public class BookServiceMongo {

    private static final JsonFormat.Printer printer = JsonFormat.printer().preservingProtoFieldNames();
    private static final JsonFormat.Parser parser = JsonFormat.parser().ignoringUnknownFields();
    BookServiceServer bookServiceServer;
    private final Collection collection;

    public BookServiceMongo() {
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
                .database(config.getString("mongo.database"))
                .build();

        DatastoreConfig datastoreConfig = DatastoreConfig.builder()
                .connectionConfig(connectionConfig)
                .type(DatabaseType.MONGO)
                .build();

        Datastore datastore = DatastoreProvider.getDatastore(datastoreConfig);
        this.collection = datastore.getCollection("books");
    }

    public void addBook(final Book book) throws IOException {
        collection.create(Key.from(book.getIsbn()), new JSONDocument(printer.print(book)));
    }

    public void updateBook(final Book book) throws IOException {
        Filter filter = Filter.eq("_id", book.getIsbn());
        collection.update(Key.from(book.getIsbn()), new JSONDocument(printer.print(book)), filter);
    }

    public void deleteBook(final String isbn) throws IOException {
        collection.delete(Key.from(isbn));
    }

    public List<Book> getBooks() throws IOException {
        List<Book> books = new ArrayList<>();
        Query query = Query.builder().build();
        try (CloseableIterator<Document> iterator = collection.find(query)) {
            while (iterator.hasNext()) {
                Document document = iterator.next();
                Book.Builder bookBuilder = Book.newBuilder();
                parser.merge(document.toJson(), bookBuilder);
                books.add(bookBuilder.build());
            }
        }
        return books;
    }

}