package AdminDash;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SimpleUserData {

    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty ID;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty usertype;
    private final StringProperty homeroom;
    private final StringProperty comments;
    private final StringProperty fines;
    private final StringProperty books;

    public String getFines() {
        return fines.get();
    }

    public StringProperty finesProperty() {
        return fines;
    }

    public void setFines(String fines) {
        this.fines.set(fines);
    }

    public String getBooks() {
        return books.get();
    }

    public StringProperty booksProperty() {
        return books;
    }

    public void setBooks(String books) {
        this.books.set(books);
    }

    public String getComments() {
        return comments.get();
    }

    public StringProperty commentsProperty() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments.set(comments);
    }

    public String getHomeroom() {
        return homeroom.get();
    }

    public StringProperty homeroomProperty() {
        return homeroom;
    }

    public void setHomeroom(String homeroom) {
        this.homeroom.set(homeroom);
    }

    public String getUsertype() {
        return usertype.get();
    }

    public StringProperty usertypeProperty() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype.set(usertype);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getID() {
        return ID.get();
    }

    public StringProperty IDProperty() {
        return ID;
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public SimpleUserData(String fname, String lname, String id, String username, String password, String usertype, String homeroom, String comments, String fines, String books){

        this.firstName = new SimpleStringProperty(fname);
        this.lastName = new SimpleStringProperty(lname);
        this.ID = new SimpleStringProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.usertype = new SimpleStringProperty(usertype);
        this.homeroom = new SimpleStringProperty(homeroom);
        this.comments = new SimpleStringProperty(comments);
        this.fines = new SimpleStringProperty(fines);
        this.books = new SimpleStringProperty(books);
    }
}
