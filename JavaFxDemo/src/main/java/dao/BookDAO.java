package dao;

import app.Book;
import app.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public List<Book> fetchBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                // Map the result set to a Book object
                Book book = new Book();
                book.setBookId(resultSet.getString("book_id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setDescription(resultSet.getString("description"));
                book.setImageUrl(resultSet.getString("image_url"));
                book.setPublishedDate(resultSet.getString("published_date"));
                book.setPageCount(resultSet.getInt("page_count"));
                book.setCategories(resultSet.getString("categories"));
                book.setAverageRating(resultSet.getString("average_rating"));
                book.setPreviewLink(resultSet.getString("preview_link"));

                // Add to the list
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}
