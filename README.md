# Online Book Store Service

## Instructions to Build and Run the Service

### Prerequisites
- Java 8 or higher
- Gradle
- Git

### Clone the Repository
git clone https://github.com/jatin471/OnlineBookStore.git

cd OnlineBookStore

## Build the Project
./gradlew build

## Run the Server
java -jar build/libs/OnlineBookStore.jar

Or

Click run icon in BookServiceServer file.

## Run the Client
java -cp build/libs/OnlineBookStore.jar com.example.bookstore.BookServiceClient

Or 

Click run icon in BookServiceClient file.

## Implemented RPCs and Their Expected Behavior

### AddBook
Request: AddBookRequest containing a Book object with isbn, title, authors, and page_count.

Response: AddBookResponse with a message indicating the success or failure of the operation.

Behavior: Adds a new book to the in-memory store.

### UpdateBook
Request: UpdateBookRequest containing a Book object with updated details.

Response: UpdateBookResponse with a message indicating the success or failure of the operation.

Behavior: Updates an existing book in the in-memory store.

### DeleteBook

Request: DeleteBookRequest containing the isbn of the book to be deleted.

Response: DeleteBookResponse with a message indicating the success or failure of the operation.

Behavior: Deletes a book from the in-memory store based on the provided isbn.

### GetBooks
Request: GetBooksRequest (no parameters needed).

Response: GetBooksResponse containing a list of Book objects.

Behavior: Retrieves all books from the in-memory store.


## Run Tests
./gradlew test

Or 

Run BookService in src/test/java/com/example/bookstore .

[7 example testcases are there 4 are valid and 4 are invalid].