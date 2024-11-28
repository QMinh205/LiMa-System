package dao;

import app.Book;
import app.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public List<Book> fetchBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books"; // Adjust to match your table name and schema

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                // Map the result set to a Book object
                Book book = new Book();
                book.setBookId(resultSet.getString("book_id")); // Adjust column name
                book.setTitle(resultSet.getString("title")); // Adjust column name
                book.setAuthor(resultSet.getString("author")); // Adjust column name
                book.setPublisher(resultSet.getString("publisher"));
                book.setDescription(resultSet.getString("description"));
                book.setImageUrl(resultSet.getString("image_url"));
                book.setPublishedDate(resultSet.getString("published_date")); // Adjust column name
                book.setPageCount(resultSet.getInt("page_count")); // Adjust column name
                book.setCategories(resultSet.getString("categories")); // Adjust column name
                book.setAverageRating(resultSet.getString("average_rating")); // Adjust column name
                book.setPreviewLink(resultSet.getString("preview_link")); // Adjust column name

                // Add to the list
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}
