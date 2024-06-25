package com.example.bookstore.unit;

import com.example.bookstore.Bookstore.*;
import com.example.bookstore.BookServiceImpl;
import io.grpc.stub.StreamObserver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {
    private BookServiceImpl bookService;
    private StreamObserver<AddBookResponse> addBookResponseObserver;
    private StreamObserver<UpdateBookResponse> updateBookResponseObserver;
    private StreamObserver<DeleteBookResponse> deleteBookResponseObserver;
    private StreamObserver<GetBooksResponse> getBooksResponseObserver;

    @Before
    public void setUp() {
        bookService = new BookServiceImpl();
        addBookResponseObserver = mock(StreamObserver.class);
        updateBookResponseObserver = mock(StreamObserver.class);
        deleteBookResponseObserver = mock(StreamObserver.class);
        getBooksResponseObserver = mock(StreamObserver.class);
    }

    @Test
    public void addBookTest() {
        Book book = Book.newBuilder()
                .setIsbn("123")
                .setTitle("Test Book")
                .addAuthors("Author1")
                .setPageCount(100)
                .build();

        AddBookRequest request = AddBookRequest.newBuilder().setBook(book).build();
        bookService.addBook(request, addBookResponseObserver);

        ArgumentCaptor<AddBookResponse> responseCaptor = ArgumentCaptor.forClass(AddBookResponse.class);
        verify(addBookResponseObserver).onNext(responseCaptor.capture());
        verify(addBookResponseObserver).onCompleted();

        AddBookResponse response = responseCaptor.getValue();
        assertTrue(response.getSuccess());
        assertEquals(book, bookService.getBookStore().get("123"));
    }

    @Test
    public void updateBookTest() {
        Book book = Book.newBuilder()
                .setIsbn("123")
                .setTitle("Test Book")
                .addAuthors("Author1")
                .setPageCount(100)
                .build();

        bookService.getBookStore().put("123", book);

        Book updatedBook = Book.newBuilder()
                .setIsbn("123")
                .setTitle("Updated Book")
                .addAuthors("Author1")
                .setPageCount(120)
                .build();

        UpdateBookRequest request = UpdateBookRequest.newBuilder().setBook(updatedBook).build();
        bookService.updateBook(request, updateBookResponseObserver);

        ArgumentCaptor<UpdateBookResponse> responseCaptor = ArgumentCaptor.forClass(UpdateBookResponse.class);
        verify(updateBookResponseObserver).onNext(responseCaptor.capture());
        verify(updateBookResponseObserver).onCompleted();

        UpdateBookResponse response = responseCaptor.getValue();
        assertTrue(response.getSuccess());
        assertEquals(updatedBook, bookService.getBookStore().get("123"));
    }

    @Test
    public void deleteBookTest() {
        Book book = Book.newBuilder()
                .setIsbn("123")
                .setTitle("Test Book")
                .addAuthors("Author1")
                .setPageCount(100)
                .build();

        bookService.getBookStore().put("123", book);

        DeleteBookRequest request = DeleteBookRequest.newBuilder().setIsbn("123").build();
        bookService.deleteBook(request, deleteBookResponseObserver);

        ArgumentCaptor<DeleteBookResponse> responseCaptor = ArgumentCaptor.forClass(DeleteBookResponse.class);
        verify(deleteBookResponseObserver).onNext(responseCaptor.capture());
        verify(deleteBookResponseObserver).onCompleted();

        DeleteBookResponse response = responseCaptor.getValue();
        assertTrue(response.getSuccess());
        assertFalse(bookService.getBookStore().containsKey("123"));
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

        bookService.getBookStore().put("123", book1);
        bookService.getBookStore().put("124", book2);

        GetBooksRequest request = GetBooksRequest.newBuilder().build();
        bookService.getBooks(request, getBooksResponseObserver);

        ArgumentCaptor<GetBooksResponse> responseCaptor = ArgumentCaptor.forClass(GetBooksResponse.class);
        verify(getBooksResponseObserver).onNext(responseCaptor.capture());
        verify(getBooksResponseObserver).onCompleted();

        GetBooksResponse response = responseCaptor.getValue();
        assertEquals(2, response.getBooksCount());
        assertTrue(response.getBooksList().contains(book1));
        assertTrue(response.getBooksList().contains(book2));
    }
}
