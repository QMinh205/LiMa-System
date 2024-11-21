package user;

public class Admin extends User {
    private String adminCode;

    public Admin(String userId, String name, String email, String password, String adminCode) {
        super(userId, name, email, password);
        this.adminCode = adminCode;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    public void addBook(String bookId, String title, String author) {
        System.out.println("Admin " + getName() + " added the book: " + title);
        // Logic to add the book to the library
    }

    public void removeBook(String bookId) {
        System.out.println("Admin " + getName() + " removed the book with ID: " + bookId);
        // Logic to remove the book from the library
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Admin");
    }
}
