package com.example.bookstore;

import com.example.bookstore.BookServiceGrpc.*;
import com.example.bookstore.Bookstore.*;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BookServiceImpl extends BookServiceGrpc.BookServiceImplBase {
    private final ConcurrentMap<String, Book> bookStore = new ConcurrentHashMap<>();
    BookServiceMongo bookServiceMongo;

    public BookServiceImpl(){
        bookServiceMongo = new BookServiceMongo();
    }

    @Override
    public void addBook(AddBookRequest request, StreamObserver<AddBookResponse> responseObserver) {
        //HashMap
//        Book book = request.getBook();
//        bookStore.put(book.getIsbn(), book);
//        bookServiceMongo.addBook("Harry Potter");
        
        // HARD CODED
        // Book book = Book.newBuilder()
        //         .setTitle("Harry Potter")
        //         .setIsbn("1")
        //         .build();

        Book book = request.getBook();
        try {
            bookServiceMongo.addBook(book);
        } catch (IOException e) {
            // Handle the exception, e.g., log it or show an error message
            System.err.println("Error adding book: " + e.getMessage());
        }
        AddBookResponse response = AddBookResponse.newBuilder().setSuccess(true).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<UpdateBookResponse> responseObserver) {
        // Book book = request.getBook();
        // bookStore.put(book.getIsbn(), book);
        // UpdateBookResponse response = UpdateBookResponse.newBuilder().setSuccess(true).build();
        // responseObserver.onNext(response);
        // responseObserver.onCompleted();


        Book book = request.getBook();
        try {
            bookServiceMongo.updateBook(book);
            UpdateBookResponse response = UpdateBookResponse.newBuilder().setSuccess(true).build();
            responseObserver.onNext(response);
        } catch (IOException e) {
            UpdateBookResponse response = UpdateBookResponse.newBuilder().setSuccess(false).build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBook(DeleteBookRequest request, StreamObserver<DeleteBookResponse> responseObserver) {
        // bookStore.remove(request.getIsbn());
        // DeleteBookResponse response = DeleteBookResponse.newBuilder().setSuccess(true).build();
        // responseObserver.onNext(response);
        // responseObserver.onCompleted();


        try {
            bookServiceMongo.deleteBook(request.getIsbn());
            DeleteBookResponse response = DeleteBookResponse.newBuilder().setSuccess(true).build();
            responseObserver.onNext(response);
        } catch (IOException e) {
            DeleteBookResponse response = DeleteBookResponse.newBuilder().setSuccess(false).build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getBooks(GetBooksRequest request, StreamObserver<GetBooksResponse> responseObserver) {
        // GetBooksResponse.Builder responseBuilder = GetBooksResponse.newBuilder();
        // bookStore.values().forEach(responseBuilder::addBooks);
        // responseObserver.onNext(responseBuilder.build());
        // responseObserver.onCompleted();


        try {
            GetBooksResponse.Builder responseBuilder = GetBooksResponse.newBuilder();
            bookServiceMongo.getBooks().forEach(responseBuilder::addBooks);
            responseObserver.onNext(responseBuilder.build());
        } catch (IOException e) {
            responseObserver.onError(e);
        }
        responseObserver.onCompleted();
    }

    public ConcurrentMap<String, Book> getBookStore() {
        return bookStore;
    }

}
