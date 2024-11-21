package user;

public class Member extends User {
    private int borrowedBooks;

    public Member(String userId, String name, String email, String password, int borrowedBooks) {
        super(userId, name, email, password);
        this.borrowedBooks = borrowedBooks;
    }

    public int getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(int borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public void borrowBook(String bookId) {
        System.out.println("Member " + getName() + " borrowed the book with ID: " + bookId);
        borrowedBooks++;
        // Logic to borrow the book
    }

    public void returnBook(String bookId) {
        System.out.println("Member " + getName() + " returned the book with ID: " + bookId);
        borrowedBooks--;
        // Logic to return the book
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Member");
    }
}
