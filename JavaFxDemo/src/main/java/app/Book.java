package app;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String publisher;
    private String description;
    private String imageUrl;
    private String publishedDate;
    private int pageCount;
    private String categories;
    private String averageRating;
    private String previewLink;

    // Constructor
    public Book(String bookId, String title, String author, String publisher, String description, String imageUrl,
                String publishedDate, int pageCount, String categories, String averageRating, String previewLink) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.imageUrl = imageUrl;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
        this.categories = categories;
        this.averageRating = averageRating;
        this.previewLink = previewLink;
    }

    // Getters and setters for all fields
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getPublishedDate() { return publishedDate; }
    public int getPageCount() { return pageCount; }
    public String getCategories() { return categories; }
    public String getAverageRating() { return averageRating; }
    public String getPreviewLink() { return previewLink; }
}
