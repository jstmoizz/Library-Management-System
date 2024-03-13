Library Management System

Description:
This is a simple command-line library management system written in Java. It allows users to add books, add users, check out books, return books, and search for books by title, author, or ID.

Usage:
1. Compile the Java files using a Java compiler (e.g., javac LibraryManagementSystem.java).
2. Run the compiled program (e.g., java LibraryManagementSystem).
3. Follow the on-screen menu to perform various operations.

Features:
- Add Book: Add a new book to the library system.
- Add User: Add a new user to the library system.
- Check Out Book: Allow a user to check out a book from the library.
- Return Book: Allow a user to return a book to the library.
- Search Books by Title: Search for books in the library by title.
- Search Books by Author: Search for books in the library by author.
- Search Book by ID: Search for a book in the library by its ID.

Error Handling:
- The program handles invalid input using try-catch blocks for InputMismatchException.
- It provides appropriate error messages and prompts users to enter valid data.

Data Persistence:
- The program uses serialization to save and load data (books and users) from files.
- Data is saved in binary files (books.ser and users.ser) to preserve object state between program runs.

Files:
- LibraryManagementSystem.java: Contains the main program code.
- Book.java: Defines the Book class.
- User.java: Defines the User class.

Note: Make sure to have appropriate read and write permissions for file operations.

Author:
[Abdul Moiz/jstmoizz]

Date:
[13/03/24]

Version:
1.0
