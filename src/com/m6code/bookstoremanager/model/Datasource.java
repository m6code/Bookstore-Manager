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


    private Connection conn;
    private Statement createBookTable;
    private PreparedStatement insertInitialBookData;

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

        } catch (SQLException e) {
            System.out.println("Error closing connection <- "+ e.getMessage());
        }

    }

    public List<Book> queryBooks() {

        try(Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(QUERY_BOOKS)){

            List<Book> books = new ArrayList<>();
            while (results.next()){
                Book book = new Book();
                book.set_id(results.getInt(INDEX_BOOK_ID));
                book.setTitle(results.getString(INDEX_BOOK_TITLE));
                books.add(book);
            }
            return books;

        }catch (SQLException e){
            System.out.println("Error getting books: "+e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
}
