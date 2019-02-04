package com.m6code.bookstoremanager.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {
    public static final String DB_NAME = "bookstore.db";
    public static final String CONN_STRING = "jdbc:sqlite:./src/resources/db/" + DB_NAME;

    public static final String TABLE_BOOKS = "books";
    public static final String COLUMN_BOOK_ID = "_id";
    public static final String COLUMN_BOOK_TITLE = "title";
    public static final int INDEX_BOOK_ID = 1;
    public static final int INDEX_BOOK_TITLE = 2;

    public static final String TABLE_SQL_SEQUENCE = "sqlite_sequence";
    public static final String COLUMN_SSQ_NAME = "name";
    public static final String COLUMN_SSQ_SEQ = "seq";
    public static final int INDEX_SSQ_NAME = 1;
    public static final int INDEX_SSQ_SEQ = 2;

    public static final String CREATE_BOOK_TABLE_IF_NOT_EXISTS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKS
                    + " (" + COLUMN_BOOK_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_BOOK_TITLE + " TEXT NOT NULL" + ")";

    public static final String INSERT_INITIAL_BOOK_DATA =
            "INSERT INTO " + TABLE_BOOKS + "(" + COLUMN_BOOK_ID + ", " + COLUMN_BOOK_TITLE + ")"
                    + " VALUES(?,?) ";
    public static final String QUERY_BOOKS =
            "SELECT * FROM " + TABLE_BOOKS;
    public static final String INSERT_BOOK =
            "INSERT INTO " + TABLE_BOOKS + " (" + COLUMN_BOOK_TITLE + ") VALUES(?)";

    public static final String DELETE_SELECTED_BOOK_BY_ID =
            "DELETE FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOK_ID + "=?";

    public static final String UPDATE_SELECTED_BOOK =
            "UPDATE " + TABLE_BOOKS + " SET " + COLUMN_BOOK_TITLE + "=? " +"WHERE "+COLUMN_BOOK_ID+"=?";
    public static final String QUERY_SEQUENCE_TABLE = "SELECT * FROM "+TABLE_SQL_SEQUENCE;
    public static final String INSERT_SEQUENCE = "INSERT INTO "+ TABLE_SQL_SEQUENCE
            + " ("+COLUMN_SSQ_NAME+", "+COLUMN_SSQ_SEQ+") VALUES(?,?)";


    private Connection conn;
    private Statement createBookTable;
    private PreparedStatement insertInitialSequenceData;
    private PreparedStatement insertBook;
    private PreparedStatement deleteBook;
    private PreparedStatement updateBook;

    // Create an instance of the datasource to be accessed by other classes
    private static Datasource instance = new Datasource();

    private Datasource() {
    }

    public static Datasource getInstance() {
        return instance;
    }

    /* open database connection */
    public boolean open() {
        System.out.println("Connecting to db ...");
        try {
            conn = DriverManager.getConnection(CONN_STRING);
            System.out.println("Connected to db <> " + DB_NAME);

            // Create books table if not exists
            createBookTable = conn.createStatement();
            createBookTable.execute(CREATE_BOOK_TABLE_IF_NOT_EXISTS);

            // Prepare Insert into squence table statement
            insertInitialSequenceData = conn.prepareStatement(INSERT_SEQUENCE);

            //Query sqlite_sequence table and set book sequence if not exists
            createBookSequence();

            // Prepare Insert into books preparedStatement
            insertBook = conn.prepareStatement(INSERT_BOOK);
            // Prepare Delete book preparedStatement
            deleteBook = conn.prepareStatement(DELETE_SELECTED_BOOK_BY_ID);
            // Prepare Update book statement
            updateBook = conn.prepareStatement(UPDATE_SELECTED_BOOK);

            return true;

        } catch (SQLException e) {
            System.out.println("Error connecting to db <- " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /* Closes database connection */
    public void close() {
        try {
            if (createBookTable != null) {
                createBookTable.close();
            }
            if (insertInitialSequenceData != null) {
                insertInitialSequenceData.close();
            }
            if (insertBook != null) {
                insertBook.close();
            }
            if (deleteBook != null) {
                deleteBook.close();
            }
            if (updateBook != null){
                deleteBook.close();
            }

        } catch (SQLException e) {
            System.out.println("Error closing connection <- " + e.getMessage());
        }

    }

    public List<Book> queryBooks() {

        try (Statement stmt = conn.createStatement();
             ResultSet results = stmt.executeQuery(QUERY_BOOKS)) {

            List<Book> books = new ArrayList<>();
            while (results.next()) {
                books.add(new Book(
                        results.getString(INDEX_BOOK_ID),
                        results.getString(INDEX_BOOK_TITLE)));
            }
            return books;

        } catch (SQLException e) {
            System.out.println("Error getting books: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public void insertBook(String title) {
        try {
            insertBook.setString(1, title);
            int affectedRows = insertBook.executeUpdate();
            if (affectedRows == 1) {
                System.out.println("Added Book: " + title);
            }
        } catch (SQLException e) {
            System.out.println("Error inserting book: " + e.getMessage());
        }
    }

    public void deleteBook(String id) {
        try {
            deleteBook.setString(1, id);
            int affectedRows = deleteBook.executeUpdate(); // delete the book
            if (affectedRows == 1) {
                System.out.println("Deleted book with id " + id);
            }
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    public void updateBook(String id, String title) {
        try{
            updateBook.setString(1,title);
            updateBook.setString(2,id);
            updateBook.executeUpdate();
        }catch (SQLException e){
            System.out.println("Cannot update: "+e.getMessage());
        }
    }

    private void createBookSequence(){
        try(Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(QUERY_SEQUENCE_TABLE)) {
            if (!results.next()){
                insertInitialSequenceData.setString(1,TABLE_BOOKS);
                insertInitialSequenceData.setString(2,"109");
                insertInitialSequenceData.executeUpdate();
            }else {
                System.out.println("books table sequence already created");
            }

        }catch (SQLException e){
            e.getStackTrace();
        }
    }
}
