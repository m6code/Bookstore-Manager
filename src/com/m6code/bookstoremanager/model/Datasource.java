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

    public static final String CREATE_BOOK_TABLE_IF_NOT_EXISTS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKS
                    + " (" + COLUMN_BOOK_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_BOOK_TITLE + " TEXT NOT NULL" + ")";

    public static final String INSERT_INITIAL_BOOK_DATA =
            "INSERT INTO " + TABLE_BOOKS + "(" + COLUMN_BOOK_ID + ", " + COLUMN_BOOK_TITLE + ")"
                    + " VALUES(?,?) ";
    public static final String QUERY_BOOKS =
            "SELECT * FROM "+TABLE_BOOKS;
    public static final String INSERT_BOOK =
            "INSERT INTO "+ TABLE_BOOKS + " ("+COLUMN_BOOK_TITLE+") VALUES(?)";

    public static final String DELETE_SELECTED_BOOK_BY_ID =
            "DELETE FROM "+TABLE_BOOKS+" WHERE "+COLUMN_BOOK_ID+"=?";


    private Connection conn;
    private Statement createBookTable;
    private PreparedStatement insertInitialBookData;
    private PreparedStatement insertBook;
    private PreparedStatement deleteBook;

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

            // TODO : Query books table initial book data exits
            // before adding or skipping it
            // Insert initial data into books table
//            insertInitialBookData = conn.prepareStatement(INSERT_INITIAL_BOOK_DATA);
//            insertInitialBookData.setInt(1,110);
//            insertInitialBookData.setString(2,"Things Fall Apart");
//            insertInitialBookData.executeUpdate();

            // Prepare Insert into books preparedStatement
            insertBook = conn.prepareStatement(INSERT_BOOK);
            // Prepare Delete book preparedStatement
            deleteBook = conn.prepareStatement(DELETE_SELECTED_BOOK_BY_ID);

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
            if (insertInitialBookData != null){
                insertInitialBookData.close();
            }
            if (insertBook != null){
                insertBook.close();
            }
            if (deleteBook!=null){
                deleteBook.close();
            }

        } catch (SQLException e) {
            System.out.println("Error closing connection <- "+ e.getMessage());
        }

    }

    public List<Book> queryBooks() {

        try(Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(QUERY_BOOKS)){

            List<Book> books = new ArrayList<>();
            while (results.next()){
                books.add(new Book(
                        results.getString(INDEX_BOOK_ID),
                        results.getString(INDEX_BOOK_TITLE)));
            }
            return books;

        }catch (SQLException e){
            System.out.println("Error getting books: "+e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public void insertBook(String title){
        try {
            insertBook.setString(1,title);
            int affectedRows = insertBook.executeUpdate();
            if (affectedRows == 1){
                System.out.println("Added Book: "+title);
            }
        } catch (SQLException e) {
            System.out.println("Error inserting book: "+e.getMessage());
        }
    }

    public void deleteBook(String id){
        try{
            deleteBook.setString(1,id);
            int affectedRows = deleteBook.executeUpdate(); // delete the book
            if (affectedRows == 1){
                System.out.println("Deleted book with id "+id);
            }
        }catch (SQLException e){
            System.out.println("Delete failed: "+e.getMessage());
        }
    }
}
