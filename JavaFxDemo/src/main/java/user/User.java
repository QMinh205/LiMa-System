package user;

public class User {
    private int userId;          // Corresponds to user_id in the database
    private String fullName;     // Corresponds to fullName in the database
    private String userName;     // Corresponds to userName in the database
    private String password;     // Corresponds to password in the database
    private String email;        // Corresponds to email in the database
    private String phoneNumber;  // Corresponds to phoneNumber in the database
    private String safeCode;     // Corresponds to safeCode in the database
    private String dateOfBirth;  // Corresponds to dateOfBirth in the database (using String for flexibility, can be converted to LocalDate)

    // Default constructor
    public User() {}

    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    // Constructor with all fields
    public User(int userId, String fullName, String userName, String password, String email,
                String phoneNumber, String safeCode, String dateOfBirth) {
        this.userId = userId;
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.safeCode = safeCode;
        this.dateOfBirth = dateOfBirth;
    }

    // Constructor without userId (useful for creating new users)
    public User(String fullName, String userName, String password, String email,
                String phoneNumber, String safeCode, String dateOfBirth) {
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.safeCode = safeCode;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSafeCode() {
        return safeCode;
    }

    public void setSafeCode(String safeCode) {
        this.safeCode = safeCode;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // toString() method for displaying user details
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", safeCode='" + safeCode + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                '}';
    }
}
