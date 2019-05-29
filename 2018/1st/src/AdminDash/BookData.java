package AdminDash;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BookData {
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty serial;
    private final StringProperty available;
    private final StringProperty comments;
    private final StringProperty ownerid;
    private final StringProperty hold;
    private final StringProperty outDate;

    public String getOwnerid() {
        return ownerid.get();
    }

    public StringProperty owneridProperty() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid.set(ownerid);
    }

    public String getHold() {
        return hold.get();
    }

    public StringProperty holdProperty() {
        return hold;
    }

    public void setHold(String hold) {
        this.hold.set(hold);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getSerial() {
        return serial.get();
    }

    public StringProperty serialProperty() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial.set(serial);
    }

    public String getAvailable() {
        return available.get();
    }

    public StringProperty availableProperty() {
        return available;
    }

    public void setAvailable(String available) {
        this.available.set(available);
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

    public BookData (String title, String author, String serial, String available, String comments, String ownerid, String hold, String outdate){
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.serial = new SimpleStringProperty(serial);
        this.available = new SimpleStringProperty(available);
        this.comments = new SimpleStringProperty(comments);
        this.ownerid = new SimpleStringProperty(ownerid);
        this.hold = new SimpleStringProperty(hold);
        this.outDate = new SimpleStringProperty(outdate);
    }
}
