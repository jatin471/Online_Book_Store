package com.example.bookstore;

import com.example.bookstore.BookServiceGrpc.*;
import com.example.bookstore.Bookstore.*;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BookServiceImpl extends BookServiceGrpc.BookServiceImplBase {
    private final ConcurrentMap<String, Book> bookStore = new ConcurrentHashMap<>();

    @Override
    public void addBook(AddBookRequest request, StreamObserver<AddBookResponse> responseObserver) {
        Book book = request.getBook();
        bookStore.put(book.getIsbn(), book);
        AddBookResponse response = AddBookResponse.newBuilder().setSuccess(true).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<UpdateBookResponse> responseObserver) {
        Book book = request.getBook();
        bookStore.put(book.getIsbn(), book);
        UpdateBookResponse response = UpdateBookResponse.newBuilder().setSuccess(true).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBook(DeleteBookRequest request, StreamObserver<DeleteBookResponse> responseObserver) {
        bookStore.remove(request.getIsbn());
        DeleteBookResponse response = DeleteBookResponse.newBuilder().setSuccess(true).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getBooks(GetBooksRequest request, StreamObserver<GetBooksResponse> responseObserver) {
        GetBooksResponse.Builder responseBuilder = GetBooksResponse.newBuilder();
        bookStore.values().forEach(responseBuilder::addBooks);
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
