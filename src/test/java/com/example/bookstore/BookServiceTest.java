package com.example.bookstore;

import com.example.bookstore.Bookstore.*;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.Test;
import io.grpc.ManagedChannel;
import io.grpc.Server;

import static org.junit.Assert.*;

public class BookServiceTest {
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private BookServiceGrpc.BookServiceBlockingStub bookServiceStub;

    public BookServiceTest() throws Exception {
        // Generate a unique in-process server name
        String serverName = InProcessServerBuilder.generateName();

        // Create a new in-process server
        Server server = InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(new BookServiceImpl())
                .build()
                .start();

        // Register the server for automatic graceful shutdown
        grpcCleanup.register(server);

        // Create a client channel to the in-process server
        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());

        // Create a blocking stub for client communication
        bookServiceStub = BookServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void addBookTest() {
        Book book = Book.newBuilder()
                .setIsbn("123")
                .setTitle("Test Book")
                .addAuthors("Author1")
                .setPageCount(100)
                .build();

        AddBookResponse response = bookServiceStub.addBook(
                AddBookRequest.newBuilder().setBook(book).build());

        assertTrue(response.getSuccess());
    }

    @Test
    public void updateBookTest() {
        Book book = Book.newBuilder()
                .setIsbn("123")
                .setTitle("Updated Book")
                .addAuthors("Author1")
                .setPageCount(120)
                .build();

        // Ensure the book exists before updating
        bookServiceStub.addBook(AddBookRequest.newBuilder().setBook(book).build());

        UpdateBookResponse response = bookServiceStub.updateBook(
                UpdateBookRequest.newBuilder().setBook(book).build());

        assertTrue(response.getSuccess());
    }

    @Test
    public void deleteBookTest() {
        Book book = Book.newBuilder()
                .setIsbn("123")
                .setTitle("Test Book")
                .addAuthors("Author1")
                .setPageCount(100)
                .build();

        // Add a book first
        bookServiceStub.addBook(AddBookRequest.newBuilder().setBook(book).build());

        // Delete the book
        DeleteBookResponse response = bookServiceStub.deleteBook(
                DeleteBookRequest.newBuilder().setIsbn("123").build());

        assertTrue(response.getSuccess());
    }

    @Test
    public void getBooksTest() {
        Book book1 = Book.newBuilder()
                .setIsbn("123")
                .setTitle("Test Book1")
                .addAuthors("Author1")
                .setPageCount(100)
                .build();

        Book book2 = Book.newBuilder()
                .setIsbn("124")
                .setTitle("Test Book2")
                .addAuthors("Author2")
                .setPageCount(150)
                .build();

        // Add two books
        bookServiceStub.addBook(AddBookRequest.newBuilder().setBook(book1).build());
        bookServiceStub.addBook(AddBookRequest.newBuilder().setBook(book2).build());

        // Retrieve all books
        GetBooksResponse response = bookServiceStub.getBooks(GetBooksRequest.newBuilder().build());

        assertEquals(2, response.getBooksCount());
    }

    @Test
    public void addDuplicateBookTest() {
        Book book = Book.newBuilder()
                .setIsbn("123")
                .setTitle("Test Book")
                .addAuthors("Author1")
                .setPageCount(100)
                .build();

        // Add the same book twice
        bookServiceStub.addBook(AddBookRequest.newBuilder().setBook(book).build());
        AddBookResponse response = bookServiceStub.addBook(
                AddBookRequest.newBuilder().setBook(book).build());

        assertFalse(response.getSuccess());
    }

    @Test
    public void updateNonExistingBookTest() {
        Book book = Book.newBuilder()
                .setIsbn("123")
                .setTitle("Updated Book")
                .addAuthors("Author1")
                .setPageCount(120)
                .build();

        UpdateBookResponse response = bookServiceStub.updateBook(
                UpdateBookRequest.newBuilder().setBook(book).build());

        assertFalse(response.getSuccess());
    }

    @Test
    public void deleteNonExistingBookTest() {
        DeleteBookResponse response = bookServiceStub.deleteBook(
                DeleteBookRequest.newBuilder().setIsbn("123").build());

        assertFalse(response.getSuccess());
    }
}
