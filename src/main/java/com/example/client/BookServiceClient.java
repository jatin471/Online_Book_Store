package com.example.client;

import com.example.bookstore.BookServiceGrpc;
import com.example.bookstore.BookServiceGrpc.*;
import com.example.bookstore.Bookstore.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BookServiceClient {
    private static final Logger logger = Logger.getLogger(BookServiceClient.class.getName());

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        BookServiceBlockingStub stub = BookServiceGrpc.newBlockingStub(channel);

        // Add a book
        Book book1 = Book.newBuilder()
                .setIsbn("1")
                .setTitle("Harry Potter")
                .addAuthors("JK Rowling")
                .setPageCount(100)
                .build();

        AddBookRequest addBookRequest1 = AddBookRequest.newBuilder()
                .setBook(book1)
                .build();

        AddBookResponse addBookResponse1;
        try {
            addBookResponse1 = stub.addBook(addBookRequest1);
            logger.info("Add Book Response (Book One): " + addBookResponse1.getSuccess());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error adding Book One: " + e.getMessage());
        }

        // Add another book
        Book book2 = Book.newBuilder()
                .setIsbn("2")
                .setTitle("Zero To One")
                .addAuthors("Peter Thiel")
                .setPageCount(200)
                .build();

        AddBookRequest addBookRequest2 = AddBookRequest.newBuilder()
                .setBook(book2)
                .build();

        AddBookResponse addBookResponse2;
        try {
            addBookResponse2 = stub.addBook(addBookRequest2);
            logger.info("Add Book Response (Book Two): " + addBookResponse2.getSuccess());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error adding Book Two: " + e.getMessage());
        }

        // Get all books
        GetBooksRequest getBooksRequest = GetBooksRequest.newBuilder().build();
        GetBooksResponse getBooksResponse = stub.getBooks(getBooksRequest);
        logger.info("Get Books Response:");
        getBooksResponse.getBooksList().forEach(book -> logger.info(book.toString()));

        // Update a book
        Book updatedBook1 = Book.newBuilder()
                .setIsbn("1")
                .setTitle("Harry Potter Deathly Hallows")
                .addAuthors("Joanne Rowling")
                .setPageCount(150)
                .build();

        UpdateBookRequest updateBookRequest = UpdateBookRequest.newBuilder()
                .setBook(updatedBook1)
                .build();

        UpdateBookResponse updateBookResponse;
        try {
            updateBookResponse = stub.updateBook(updateBookRequest);
            logger.info("Update Book Response: " + updateBookResponse.getSuccess());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error updating Book: " + e.getMessage());
        }

        // Get all books after update
        getBooksResponse = stub.getBooks(getBooksRequest);
        logger.info("Get Books Response After Update:");
        getBooksResponse.getBooksList().forEach(book -> logger.info(book.toString()));

        // Delete a book
        DeleteBookRequest deleteBookRequest = DeleteBookRequest.newBuilder()
                .setIsbn("1")
                .build();

        DeleteBookResponse deleteBookResponse;
        try {
            deleteBookResponse = stub.deleteBook(deleteBookRequest);
            logger.info("Delete Book Response: " + deleteBookResponse.getSuccess());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error deleting Book: " + e.getMessage());
        }

        // Get all books after delete
        getBooksResponse = stub.getBooks(getBooksRequest);
        logger.info("Get Books Response After Delete:");
        getBooksResponse.getBooksList().forEach(book -> logger.info(book.toString()));

        // Cleanup
        channel.shutdown();
    }
}
