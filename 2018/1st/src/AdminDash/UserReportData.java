package AdminDash;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserReportData {
    private final StringProperty firstname;
    private final StringProperty lastname;
    private final StringProperty userid;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty usertype;
    private final StringProperty homeroom;
    private final StringProperty comments;
    private final StringProperty fines;
    private final StringProperty books;
    private final StringProperty bookcount;

    public UserReportData(String fn, String ln, String id, String un, String pw, String ut, String hr, String comments, String fines, String books, String bookcount){
        firstname = new SimpleStringProperty(fn);
        lastname = new SimpleStringProperty(ln);
        userid = new SimpleStringProperty(id);
        username = new SimpleStringProperty(un);
        password = new SimpleStringProperty(pw);
        usertype = new SimpleStringProperty(ut);
        homeroom = new SimpleStringProperty(hr);
        this.comments = new SimpleStringProperty(comments);
        this.fines = new SimpleStringProperty(fines);
        this.books = new SimpleStringProperty(books);
        this.bookcount = new SimpleStringProperty(bookcount);
    }

    public String getFirstname() {
        return firstname.get();
    }

    public StringProperty firstnameProperty() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }

    public String getLastname() {
        return lastname.get();
    }

    public StringProperty lastnameProperty() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    public String getUserid() {
        return userid.get();
    }

    public StringProperty useridProperty() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid.set(userid);
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

    public String getUsertype() {
        return usertype.get();
    }

    public StringProperty usertypeProperty() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype.set(usertype);
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

    public String getComments() {
        return comments.get();
    }

    public StringProperty commentsProperty() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments.set(comments);
    }

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

    public String getBookcount() {
        return bookcount.get();
    }

    public StringProperty bookcountProperty() {
        return bookcount;
    }

    public void setBookcount(String bookcount) {
        this.bookcount.set(bookcount);
    }
}
