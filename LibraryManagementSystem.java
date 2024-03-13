package com.studyopedia;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class Book implements Serializable {
    private int bookID;
    private String title;
    private String author;
    private String genre;
    private boolean availability;

    public Book(int bookID, String title, String author, String genre, boolean availability) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.availability = availability;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookID=" + bookID +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", availability=" + availability +
                '}';
    }
}

class User implements Serializable {
    private int userID;
    private String name;
    private String contactInfo;
    private ArrayList<Book> borrowedBooks;

    public User(int userID, String name, String contactInfo) {
        this.userID = userID;
        this.name = name;
        this.contactInfo = contactInfo;
        this.borrowedBooks = new ArrayList<>();
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(ArrayList<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", borrowedBooks=" + borrowedBooks +
                '}';
    }
}

class Library {
    private ArrayList<Book> books;
    private ArrayList<User> users;
    private static final String BOOKS_FILE = "books.ser";
    private static final String USERS_FILE = "users.ser";

    public Library() {
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
        loadBooks();
        loadUsers();
    }

    public void addBook(Scanner scanner) {
        System.out.println("\nEnter details for a new book:");
        try {
            System.out.print("Book ID: ");
            int bookID = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Title: ");
            String title = scanner.nextLine();
            System.out.print("Author: ");
            String author = scanner.nextLine();
            System.out.print("Genre: ");
            String genre = scanner.nextLine();
            System.out.print("Availability (true/false): ");
            boolean availability = scanner.nextBoolean();
            Book newBook = new Book(bookID, title, author, genre, availability);
            books.add(newBook);
            saveBooks();
            System.out.println("Book added successfully:\n" + newBook);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid data.");
            scanner.nextLine(); // Clear the input buffer
        }
    }

    public void addUser(Scanner scanner) {
        System.out.println("\nEnter details for a new user:");
        try {
            System.out.print("User ID: ");
            int userID = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Contact Information: ");
            String contactInfo = scanner.nextLine();
            User newUser = new User(userID, name, contactInfo);
            users.add(newUser);
            saveUsers();
            System.out.println("User added successfully:\n" + newUser);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid data.");
            scanner.nextLine(); // Clear the input buffer
        }
    }

    public void checkOutBook(Scanner scanner, int userID) {
        System.out.println("\nEnter details for the book you want to check out:");
        try {
            System.out.print("Book Title: ");
            String bookTitle = scanner.nextLine();
            Book foundBook = searchBookByTitle(bookTitle);
            if (foundBook != null && foundBook.isAvailable()) {
                foundBook.setAvailability(false);
                users.stream().filter(user -> user.getUserID() == userID).findFirst()
                        .ifPresent(user -> user.getBorrowedBooks().add(foundBook));
                System.out.println("Book checked out successfully!");
            } else if (foundBook == null) {
                System.out.println("Book not found.");
            } else {
                System.out.println("Book is not available for checkout.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid data.");
            scanner.nextLine(); // Clear the input buffer
        }
    }

    public void returnBook(Scanner scanner, int userID) {
        System.out.println("\nEnter details for the book you want to return:");
        try {
            System.out.print("Book Title: ");
            String bookTitle = scanner.nextLine();
            User currentUser = null;
            for (User user : users) {
                if (user.getUserID() == userID) {
                    currentUser = user;
                    break;
                }
            }
            if (currentUser != null) {
                Book bookToReturn = currentUser.getBorrowedBooks().stream()
                        .filter(book -> book.getTitle().equalsIgnoreCase(bookTitle)).findFirst().orElse(null);
                if (bookToReturn != null) {
                    bookToReturn.setAvailability(true);
                    currentUser.getBorrowedBooks().remove(bookToReturn);
                    System.out.println("Book returned successfully!");
                } else {
                    System.out.println("You haven't borrowed this book.");
                }
            } else {
                System.out.println("User not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid data.");
            scanner.nextLine(); // Clear the input buffer
        }
    }

    public ArrayList<Book> searchBooksByTitle(String title) {
        ArrayList<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                foundBooks.add(book);
            }
        }
        return foundBooks;
    }

    public ArrayList<Book> searchBooksByAuthor(String author) {
        ArrayList<Book> foundBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                foundBooks.add(book);
            }
        }
        return foundBooks;
    }

    public Book searchBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    public Book searchBookByID(int id) {
        for (Book book : books) {
            if (book.getBookID() == id) {
                return book;
            }
        }
        return null;
    }

    private void saveBooks() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(BOOKS_FILE));
            out.writeObject(books);
            out.close();
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    private void loadBooks() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(BOOKS_FILE));
            books = (ArrayList<Book>) in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading books: " + e.getMessage());
            books = new ArrayList<>();
        }
    }

    private void saveUsers() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USERS_FILE));
            out.writeObject(users);
            out.close();
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private void loadUsers() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(USERS_FILE));
            users = (ArrayList<User>) in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading users: " + e.getMessage());
            users = new ArrayList<>();
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        int userID = 1; // Assume the first user has ID 1
        int choice;
        do {
            System.out.println("\n==== Library Management System ====");
            System.out.println("1. Add Book");
            System.out.println("2. Add User");
            System.out.println("3. Check Out Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Books by Title");
            System.out.println("6. Search Books by Author");
            System.out.println("7. Search Book by ID");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                switch (choice) {
                    case 1:
                        library.addBook(scanner);
                        break;
                    case 2:
                        library.addUser(scanner);
                        break;
                    case 3:
                        library.checkOutBook(scanner, userID);
                        break;
                    case 4:
                        library.returnBook(scanner, userID);
                        break;
                    case 5:
                        System.out.print("Enter the title of the book: ");
                        String searchTitle = scanner.nextLine();
                        ArrayList<Book> titleResults = library.searchBooksByTitle(searchTitle);
                        if (titleResults.isEmpty()) {
                            System.out.println("No books found with that title.");
                        } else {
                            System.out.println("Books found with title \"" + searchTitle + "\":");
                            for (Book book : titleResults) {
                                System.out.println(book);
                            }
                        }
                        break;
                    case 6:
                        System.out.print("Enter the author of the book: ");
                        String searchAuthor = scanner.nextLine();
                        ArrayList<Book> authorResults = library.searchBooksByAuthor(searchAuthor);
                        if (authorResults.isEmpty()) {
                            System.out.println("No books found by that author.");
                        } else {
                            System.out.println("Books found by author \"" + searchAuthor + "\":");
                            for (Book book : authorResults) {
                                System.out.println(book);
                            }
                        }
                        break;
                    case 7:
                        System.out.print("Enter the ID of the book: ");
                        int searchID = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character
                        Book bookByID = library.searchBookByID(searchID);
                        if (bookByID != null) {
                            System.out.println("Book found:\n" + bookByID);
                        } else {
                            System.out.println("No book found with ID: " + searchID);
                        }
                        break;
                    case 0:
                        System.out.println("Exiting the Library Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Consume invalid input
                choice = -1; // Reset choice to trigger the loop again
            }
        } while (choice != 0);
        scanner.close();
    }
}

