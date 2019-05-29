package AdminDash;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QuickReportData {
    private final StringProperty firstname;
    private final StringProperty lastname;
    private final StringProperty userid;
    private final StringProperty homeroom;
    private final StringProperty fines;
    private final StringProperty books;

    public QuickReportData(String firstname, String lastname, String userid, String homeroom, String fines, String books){
        this.firstname = new SimpleStringProperty(firstname);
        this.lastname = new SimpleStringProperty(lastname);
        this.userid = new SimpleStringProperty(userid);
        this.homeroom = new SimpleStringProperty(homeroom);
        this.fines = new SimpleStringProperty(fines);
        this.books = new SimpleStringProperty(books);
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

    public String getHomeroom() {
        return homeroom.get();
    }

    public StringProperty homeroomProperty() {
        return homeroom;
    }

    public void setHomeroom(String homeroom) {
        this.homeroom.set(homeroom);
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
}
