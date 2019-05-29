package AdminDash;

import Login.LoginApp;
import com.sun.xml.internal.txw2.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import Database.dbConnect;
import Login.Options;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class AdminController implements Initializable {


    // Immediately following is the code for the first tab, the Users tab

    private int control = 1;

    @FXML
    private AnchorPane ar;

    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField ID;
    @FXML
    private TextField user;
    @FXML
    private TextField pw;
    @FXML
    private TextField homeroomfield;
    @FXML
    private ComboBox combobox;
    @FXML
    private TextField searchbox;
    @FXML
    private TextField removeuserfield;
    @FXML
    private TextField userchangefield;
    @FXML
    private Button removeuserbutton;
    @FXML
    private Label userchangelabel;
    @FXML
    private TextField changefn;
    @FXML
    private TextField changeln;
    @FXML
    private TextField changeun;
    @FXML
    private TextField changepw;
    @FXML
    private TextField changeut;
    @FXML
    private TextField changehr;
    @FXML
    private Pane visible1;
    @FXML
    private Button changeuserbutton;
    @FXML
    private TextArea changeusercom;
    @FXML
    private Label adduserlabel;

    @FXML
    private TableView<SimpleUserData> usertable;

    @FXML
    private TableColumn<SimpleUserData, String> fnamecol;
    @FXML
    private TableColumn<SimpleUserData, String> lnamecol;
    @FXML
    private TableColumn<SimpleUserData, String> idcol;
    @FXML
    private TableColumn<SimpleUserData, String> usernamecol;
    @FXML
    private TableColumn<SimpleUserData, String> passwordcol;
    @FXML
    private TableColumn<SimpleUserData, String> usertypecol;
    @FXML
    private TableColumn<SimpleUserData, String> homeroomcol;
    @FXML
    private TableColumn<SimpleUserData, String> finescol;
    @FXML
    private TableColumn<SimpleUserData, String> bookscol;

    private Alert alert;
    private boolean resume = false;
    private dbConnect conn;
    private ObservableList<SimpleUserData> data;
    private String sql = "SELECT * FROM login";
    private int timercounter = 5;
    private DateTime currentDate;
    private Stage dialog;
    private Button cancelBut;
    private Button continueBut;
    private Text alertText;

    public void initialize(URL url, ResourceBundle rb) {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(LoginApp.getStage());
        VBox dialogVbox = new VBox(20);
        HBox dialogHbox = new HBox(20);
        HBox dialogHboxtwo = new HBox(20);
        cancelBut = new Button("Cancel");
        continueBut = new Button("Continue");
        alertText = new Text();
        alertText.setText("Alert");
        dialogHboxtwo.getChildren().add(continueBut);
        dialogHboxtwo.getChildren().add(cancelBut);
        dialogHboxtwo.setAlignment(Pos.CENTER);
        dialogVbox.setAlignment(Pos.TOP_CENTER);
        dialogHbox.setAlignment(Pos.CENTER);
        dialogHbox.getChildren().add(alertText);
        dialogVbox.getChildren().add(dialogHbox);
        dialogVbox.getChildren().add(dialogHboxtwo);
        Scene dialogScene = new Scene(dialogVbox, 300, 100);
        dialog.setScene(dialogScene);

        this.userreportbox.setItems(FXCollections.observableArrayList(UserOpts.values()));
        this.libraryreportbox.setItems(FXCollections.observableArrayList(LibraryOpts.values()));
        this.conn = new dbConnect();
        this.combobox.setItems(FXCollections.observableArrayList(Options.values()));
        currentDate = new DateTime();
        updateAll();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (timercounter > 5) {
                    if (control > 0) {
                        timercounter--;
                        search();
                    } else if (control < 0) {
                        timercounter--;
                        bookSearch();
                    }
                }
            }
        }, 0, 200);
    }

    public void updateFines() {
            try {
                Connection connection = dbConnect.getConnection();

                String resetter = "UPDATE login SET fines='$0.00'";

                PreparedStatement statement = connection.prepareStatement(resetter);
                statement.execute();
                statement.close();

                String sqlbookfind = "SELECT * FROM library WHERE available='no'";

                ResultSet rs = connection.createStatement().executeQuery(sqlbookfind);
                while (rs.next()) {
                    if(rs.getString(7).equals("no")) {
                        String userid = rs.getString(6);
                        String outd = rs.getString(8);
                        String[] date = outd.split("-");
                        int year = Integer.parseInt(date[0]);
                        int month = Integer.parseInt(date[1]);
                        int day = Integer.parseInt(date[2]);
                        DateTime outdate = new DateTime(year, month, day, 0, 0);
                        String userfind = "SELECT * FROM login WHERE userID ='" + userid + "'";
                        ResultSet r = connection.createStatement().executeQuery(userfind);
                        String fine = r.getString(9);
                        String usertype = r.getString(6);
                        int daysout = Days.daysBetween(outdate.toLocalDate(), currentDate.toLocalDate()).getDays();
                        if (usertype.equals("Admin") && daysout > 365) {
                            double newfines = (daysout - 365) * .15 + Double.parseDouble(fine.substring(1));
                            String calculatedfines = "" + newfines;
                            String update = "UPDATE login SET fines ='$" + calculatedfines + "' WHERE userID='" + userid + "'";
                            PreparedStatement state = connection.prepareStatement(update);
                            state.execute();
                            state.close();
                            r.close();
                        } else if (daysout > 14){
                            double newfines = (daysout - 14) * .15 + Double.parseDouble(fine.substring(1));
                            String calculatedfines = "" + newfines;
                            String update = "UPDATE login SET fines ='$" + calculatedfines + "' WHERE userID ='" + userid + "'";
                            PreparedStatement state = connection.prepareStatement(update);
                            state.execute();
                            state.close();
                            r.close();
                        }
                    }
                }
                rs.close();
                connection.close();
            } catch (SQLException exc) {
                System.out.print("Line " + exc.getStackTrace()[0].getLineNumber());
            }
        }

    @SuppressWarnings("Duplicates")
    @FXML
    private void loadUserData(ActionEvent event) {
        try {
            Connection connection = dbConnect.getConnection();
            this.data = FXCollections.observableArrayList();

            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                this.data.add(new SimpleUserData(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10)));
            }
            this.fnamecol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("firstName"));
            this.lnamecol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("lastName"));
            this.idcol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("ID"));
            this.usernamecol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("Username"));
            this.passwordcol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("Password"));
            this.usertypecol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("usertype"));
            this.homeroomcol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("homeroom"));
            this.finescol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("fines"));
            this.bookscol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("books"));
            fnamecol.setCellFactory(TextFieldTableCell.forTableColumn());
            lnamecol.setCellFactory(TextFieldTableCell.forTableColumn());
            idcol.setCellFactory(TextFieldTableCell.forTableColumn());
            usernamecol.setCellFactory(TextFieldTableCell.forTableColumn());
            passwordcol.setCellFactory(TextFieldTableCell.forTableColumn());
            usertypecol.setCellFactory(TextFieldTableCell.forTableColumn());
            homeroomcol.setCellFactory(TextFieldTableCell.forTableColumn());
            finescol.setCellFactory(TextFieldTableCell.forTableColumn());
            bookscol.setCellFactory(TextFieldTableCell.forTableColumn());

            this.usertable.setItems(null);
            this.usertable.setItems(this.data);
            connection.close();
        } catch (SQLException ex) {
            ex.getCause().printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    private void loadUserData() {
        try {
            Connection connection = dbConnect.getConnection();
            this.data = FXCollections.observableArrayList();

            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                this.data.add(new SimpleUserData(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10)));
            }
            this.fnamecol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("firstName"));
            this.lnamecol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("lastName"));
            this.idcol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("ID"));
            this.usernamecol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("Username"));
            this.passwordcol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("Password"));
            this.usertypecol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("usertype"));
            this.homeroomcol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("homeroom"));
            this.finescol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("fines"));
            this.bookscol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("books"));
            fnamecol.setCellFactory(TextFieldTableCell.forTableColumn());
            lnamecol.setCellFactory(TextFieldTableCell.forTableColumn());
            idcol.setCellFactory(TextFieldTableCell.forTableColumn());
            usernamecol.setCellFactory(TextFieldTableCell.forTableColumn());
            passwordcol.setCellFactory(TextFieldTableCell.forTableColumn());
            usertypecol.setCellFactory(TextFieldTableCell.forTableColumn());
            homeroomcol.setCellFactory(TextFieldTableCell.forTableColumn());
            finescol.setCellFactory(TextFieldTableCell.forTableColumn());
            bookscol.setCellFactory(TextFieldTableCell.forTableColumn());

            this.usertable.setItems(null);
            this.usertable.setItems(this.data);
            connection.close();
        } catch (SQLException ex) {
            ex.getCause().printStackTrace();
        }
    }

    @FXML
    public void userPrint(ActionEvent event){
        print(usertable);
    }

    public void search() {
        try {
            Connection connection = dbConnect.getConnection();
            this.data = FXCollections.observableArrayList();
            String current = searchbox.getText();
            String currentSQL = ("SELECT * FROM login WHERE firstname LIKE '%" + current + "%' OR lastname LIKE '%" + current + "%'" +
                    " OR userID LIKE '%" + current + "%' OR username LIKE '%" + current + "%' OR password LIKE '%" + current + "%'" +
                    " OR usertype LIKE '%" + current + "%' OR homeroom LIKE '%" + current + "%' OR comments LIKE '%" + current + "%'" +
                    "OR books LIKE '%" + current + "%'");

            ResultSet rs = connection.createStatement().executeQuery(currentSQL);

            while (rs.next()) {
                this.data.add(new SimpleUserData(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10)));
            }
            this.fnamecol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("firstName"));
            this.lnamecol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("lastName"));
            this.idcol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("ID"));
            this.usernamecol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("Username"));
            this.passwordcol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("Password"));
            this.usertypecol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("usertype"));
            this.homeroomcol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("homeroom"));
            this.finescol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("fines"));
            this.bookscol.setCellValueFactory(new PropertyValueFactory<SimpleUserData, String>("books"));
            fnamecol.setCellFactory(TextFieldTableCell.forTableColumn());
            lnamecol.setCellFactory(TextFieldTableCell.forTableColumn());
            idcol.setCellFactory(TextFieldTableCell.forTableColumn());
            usernamecol.setCellFactory(TextFieldTableCell.forTableColumn());
            passwordcol.setCellFactory(TextFieldTableCell.forTableColumn());
            usertypecol.setCellFactory(TextFieldTableCell.forTableColumn());
            homeroomcol.setCellFactory(TextFieldTableCell.forTableColumn());
            finescol.setCellFactory(TextFieldTableCell.forTableColumn());
            bookscol.setCellFactory(TextFieldTableCell.forTableColumn());

            this.usertable.setItems(null);
            this.usertable.setItems(this.data);
            connection.close();
        } catch (SQLException ex) {
            System.out.print(ex.getStackTrace()[0].getLineNumber());
            System.err.print("Error" + ex);
        }
    }

    @FXML
    public void addUserToSearch() {
        timercounter = timercounter + 1;
        control = 1;
    }

    @FXML
    private void addStudent(ActionEvent e) {
        String sqlInsert = "INSERT INTO login(firstname, lastname, userID, username, password, usertype, homeroom, bookcount) VALUES (?,?,?,?,?,?,?,?)";
        if (usernameExists(user.getText()) == false) {
            if (userExists(ID.getText()) == false) {
                try {
                    Connection connection = dbConnect.getConnection();
                    PreparedStatement statement = connection.prepareStatement(sqlInsert);
                    statement.setString(1, this.firstname.getText());
                    statement.setString(2, this.lastname.getText());
                    statement.setString(3, this.ID.getText());
                    statement.setString(4, this.user.getText());
                    statement.setString(5, this.pw.getText());
                    statement.setString(6, (this.combobox.getValue()).toString());
                    statement.setString(7, this.homeroomfield.getText());
                    statement.setString(8, "0");

                    statement.execute();
                    statement.close();

                    firstname.setText(null);
                    lastname.setText(null);
                    ID.setText(null);
                    user.setText(null);
                    pw.setText(null);
                    combobox.setValue(null);
                    homeroomfield.setText("");
                    updateAll();
                    connection.close();
                } catch (SQLException ex) {
                    System.out.print(ex.getStackTrace()[0].getLineNumber());
                    System.err.println("Error" + ex);
                }
            } else {
                adduserlabel.setText("User ID already exists");
            }
        } else {
            adduserlabel.setText("Username already exists");
        }
    }

    @FXML
    public void removeUser(ActionEvent event) {
        String SQLremove = "DELETE FROM login WHERE userID =?";
        try {
            Connection connection = dbConnect.getConnection();

            PreparedStatement statement = connection.prepareStatement(SQLremove);
            statement.setString(1, this.removeuserfield.getText());

            statement.execute();
            statement.close();
            removeuserfield.setText(null);
            updateAll();
            connection.close();
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }

    public SimpleUserData findUser(String id) {
        String SQLfind = "SELECT * FROM login WHERE userID =" + id;

        try {
            Connection connection = dbConnect.getConnection();

            PreparedStatement statement = connection.prepareStatement(SQLfind);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                SimpleUserData user = new SimpleUserData(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10));
                rs.close();
                connection.close();
                return user;
            } else {
                rs.close();
                connection.close();
                return null;
            }
        } catch (SQLException exc) {
            System.err.print("Error " + exc);
            return null;
        }
    }

    @FXML
    public void changeUser(ActionEvent event) {
        SimpleUserData user = findUser(userchangefield.getText());
        if (user != null) {
            visible1.setVisible(true);
            changefn.setText(user.getFirstName());
            changeln.setText(user.getLastName());
            changeun.setText(user.getUsername());
            changepw.setText(user.getPassword());
            changeut.setText(user.getUsertype());
            changehr.setText(user.getHomeroom());
            changeusercom.setText(user.getComments());
            changeuserbutton.setVisible(false);
        } else {
            userchangefield.setText("");
            userchangelabel.setText("ID not found!");
        }
    }

    @FXML
    public void finalizeChange(ActionEvent event) {
        if (changeut.getText().equals("Admin") || changeut.getText().equals("Student")) {
            String fn = changefn.getText();
            String ln = changeln.getText();
            String un = changeun.getText();
            String pw = changepw.getText();
            String ut = changeut.getText();
            String hr = changehr.getText();
            String id = userchangefield.getText();
            String comments = changeusercom.getText();

            try {
                String SQLchange = "UPDATE login SET firstname='" + fn + "'," +
                        "lastname='" + ln + "', username='" + un + "', password='" + pw + "'," +
                        "usertype='" + ut + "', homeroom='" + hr + "', comments ='" + comments + "' WHERE userID=" + id;
                Connection connection = dbConnect.getConnection();
                PreparedStatement statement = connection.prepareStatement(SQLchange);

                statement.execute();
                statement.close();
                connection.close();
                updateAll();
                visible1.setVisible(false);
                userchangefield.setText("");
                changeuserbutton.setVisible(true);
            } catch (SQLException exc) {
                System.err.print("Error " + exc);
            }
        } else {
            userchangelabel.setText("choose Admin or Student");
        }
    }

    @FXML
    public void stop(ActionEvent e) {
        visible1.setVisible(false);
    }


    // This is the start of the 2nd tab, the Library tab


    @FXML
    private TextField searchbox2;
    @FXML
    private TableColumn<BookData, String> titlecol;
    @FXML
    private TableColumn<BookData, String> authorcol;
    @FXML
    private TableColumn<BookData, String> serialcol;
    @FXML
    private TableColumn<BookData, String> availablecol;
    @FXML
    private TableColumn<BookData, String> commentcol;
    @FXML
    private TableColumn<BookData, String> ownercol;
    @FXML
    private TableColumn<BookData, String> holdcol;
    @FXML
    private TableColumn<BookData, String> outdatecol;

    @FXML
    private TableView<BookData> booktable;

    @FXML
    private Label addbooklabel;
    @FXML
    private TextField titlefield;
    @FXML
    private TextField serialfield;
    @FXML
    private TextField authorfield;
    @FXML
    private TextArea commentsarea;
    @FXML
    private TextField removebookfield;
    @FXML
    private TextField checkoutserial;
    @FXML
    private TextField checkoutid;
    @FXML
    private TextField checkinserial;
    @FXML
    private TextField commentchangeserial;
    @FXML
    private TextArea commentchangearea;
    @FXML
    private Label checkoutlabel;
    @FXML
    private Label checkinlabel;

    private ObservableList<BookData> data2;
    private String sql2 = "SELECT * FROM library";

    @FXML
    public void onBookSide(ActionEvent e) {
        control = 1 - 2;
    }

    private void loadBookData() {
        try {
            Connection connection = dbConnect.getConnection();
            this.data2 = FXCollections.observableArrayList();

            ResultSet rs = connection.createStatement().executeQuery(sql2);
            while (rs.next()) {
                this.data2.add(new BookData(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8)));
            }
            this.titlecol.setCellValueFactory(new PropertyValueFactory<BookData, String>("title"));
            this.authorcol.setCellValueFactory(new PropertyValueFactory<BookData, String>("author"));
            this.serialcol.setCellValueFactory(new PropertyValueFactory<BookData, String>("serial"));
            this.availablecol.setCellValueFactory(new PropertyValueFactory<BookData, String>("available"));
            this.commentcol.setCellValueFactory(new PropertyValueFactory<BookData, String>("comments"));
            this.ownercol.setCellValueFactory(new PropertyValueFactory<BookData, String>("ownerid"));
            this.holdcol.setCellValueFactory(new PropertyValueFactory<BookData, String>("hold"));
            this.outdatecol.setCellValueFactory(new PropertyValueFactory<BookData, String>("outdate"));
            titlecol.setCellFactory(TextFieldTableCell.forTableColumn());
            authorcol.setCellFactory(TextFieldTableCell.forTableColumn());
            serialcol.setCellFactory(TextFieldTableCell.forTableColumn());
            availablecol.setCellFactory(TextFieldTableCell.forTableColumn());
            commentcol.setCellFactory(TextFieldTableCell.forTableColumn());
            ownercol.setCellFactory(TextFieldTableCell.forTableColumn());
            holdcol.setCellFactory(TextFieldTableCell.forTableColumn());
            outdatecol.setCellFactory(TextFieldTableCell.forTableColumn());

            this.booktable.setItems(null);
            this.booktable.setItems(this.data2);
            rs.close();
            connection.close();
        } catch (SQLException ex) {
            System.err.print("Error " + ex);
        }
    }

    @SuppressWarnings("Duplicates")
    @FXML
    public void bookPrint(ActionEvent event) {
        print(booktable);
    }

    public void bookSearch() {
        try {
            Connection connection = dbConnect.getConnection();
            this.data2 = FXCollections.observableArrayList();
            String current = searchbox2.getText();
            String currentSQL = ("SELECT * FROM library WHERE title LIKE '%" + current + "%' OR author LIKE '%" + current + "%'" +
                    " OR serial LIKE '%" + current + "%' OR available LIKE '%" + current + "%' OR comments LIKE '%" + current + "%'");

            ResultSet rs = connection.createStatement().executeQuery(currentSQL);

            while (rs.next()) {
                this.data2.add(new BookData(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8)));
            }
            this.titlecol.setCellValueFactory(new PropertyValueFactory<BookData, String>("title"));
            this.authorcol.setCellValueFactory(new PropertyValueFactory<BookData, String>("author"));
            this.serialcol.setCellValueFactory(new PropertyValueFactory<BookData, String>("serial"));
            this.availablecol.setCellValueFactory(new PropertyValueFactory<BookData, String>("available"));
            this.commentcol.setCellValueFactory(new PropertyValueFactory<BookData, String>("comments"));
            this.ownercol.setCellValueFactory(new PropertyValueFactory<BookData, String>("ownerid"));
            this.holdcol.setCellValueFactory(new PropertyValueFactory<BookData, String>("hold"));
            this.outdatecol.setCellValueFactory(new PropertyValueFactory<BookData, String>("outdate"));
            titlecol.setCellFactory(TextFieldTableCell.forTableColumn());
            authorcol.setCellFactory(TextFieldTableCell.forTableColumn());
            serialcol.setCellFactory(TextFieldTableCell.forTableColumn());
            availablecol.setCellFactory(TextFieldTableCell.forTableColumn());
            commentcol.setCellFactory(TextFieldTableCell.forTableColumn());
            ownercol.setCellFactory(TextFieldTableCell.forTableColumn());
            holdcol.setCellFactory(TextFieldTableCell.forTableColumn());
            outdatecol.setCellFactory(TextFieldTableCell.forTableColumn());

            rs.close();
            connection.close();
            this.booktable.setItems(null);
            this.booktable.setItems(this.data2);
        } catch (SQLException ex) {
            System.err.print("Error" + ex);
        }
    }

    @FXML
    public void addBookToSearch() {
        timercounter = timercounter + 1;
        control = 1 - 2;
    }

    @FXML
    private void addBook(ActionEvent e) {
        String sqlInsert = "INSERT INTO library(title,author,serial, available, comments) VALUES (?,?,?,?,?)";

        if (bookExists(serialfield.getText()) == false) {
            try {
                Connection connection = dbConnect.getConnection();
                PreparedStatement statement = connection.prepareStatement(sqlInsert);
                statement.setString(1, this.titlefield.getText());
                statement.setString(2, this.authorfield.getText());
                statement.setString(3, this.serialfield.getText());
                statement.setString(4, "yes");
                statement.setString(5, this.commentsarea.getText());

                statement.execute();
                statement.close();

                titlefield.setText(null);
                authorfield.setText(null);
                serialfield.setText(null);
                commentsarea.setText(null);
                updateAll();
                connection.close();
            } catch (SQLException ex) {
                System.err.println("Error " + ex);
            }
        } else {
            addbooklabel.setText("Book serial already exists");
        }
    }

    @FXML
    public void removeBook(ActionEvent event) {
        String SQLremove = "DELETE FROM library WHERE serial =?";
        try {
            Connection connection = dbConnect.getConnection();

            PreparedStatement statement = connection.prepareStatement(SQLremove);
            statement.setString(1, this.removebookfield.getText());

            statement.execute();
            statement.close();
            removebookfield.setText(null);
            updateAll();
            connection.close();
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }

    @FXML
    private Button confirmbutton;
    @FXML
    private Button cancelbutton;

    @SuppressWarnings("Duplicates")
    @FXML
    public void checkout(ActionEvent event) {
        if (userExists(checkoutid.getText())) {
            if (bookExists(checkoutserial.getText())) {
                String SQLchecker = "SELECT * FROM login WHERE userID =" + checkoutid.getText();
                String SQLbookchecker = "SELECT * FROM library WHERE serial =" + checkoutserial.getText();
                try {
                    Connection connection = dbConnect.getConnection();
                    ResultSet userset = connection.createStatement().executeQuery(SQLchecker);
                    String usertype = userset.getString(6);
                    String userID = userset.getString(3);
                    int bookCount;
                    if (userset.getString(11) == null || (userset.getString(11).equals(""))) {
                        bookCount = 0;
                    } else {
                        bookCount = Integer.parseInt(userset.getString(11));
                    }
                    String books = userset.getString(10);
                    if (books == null) {
                        books = "";
                    }
                    userset.close();
                    final String booksout = books;
                    ResultSet bookset = connection.createStatement().executeQuery(SQLbookchecker);
                    String available = bookset.getString(4);
                    String title = bookset.getString(1);
                    String onhold = bookset.getString(7);
                    String currentD = "" + currentDate.toLocalDate().getYear() + "-" + currentDate.toLocalDate().getMonthOfYear() + "-" + currentDate.toLocalDate().getDayOfMonth();
                    bookset.close();
                    if (usertype.equals("Student")) {
                        if (bookCount < 2) {
                            if (available.equals("yes")) {
                                String SQLusercheckout = "UPDATE login SET bookcount='" + (bookCount + 1) + "', books='" + (books + title) + " / " + "' WHERE userID =" + checkoutid.getText();
                                String SQLbookcheckout = "UPDATE library SET available ='no', ownerid ='" + userID + "', outdate ='" + currentD + "' WHERE serial =" + checkoutserial.getText();
                                PreparedStatement usercheckout = connection.prepareStatement(SQLusercheckout);
                                usercheckout.execute();
                                usercheckout.close();

                                PreparedStatement bookcheckout = connection.prepareStatement(SQLbookcheckout);
                                bookcheckout.execute();
                                bookcheckout.close();
                                updateAll();
                                checkoutlabel.setText("");
                            } else if (onhold.length() > 2) {
                                if (resume == false) {
                                    checkoutlabel.setText("Book on hold, continue?");
                                    cancelbutton.setVisible(true);
                                    confirmbutton.setVisible(true);
                                } else if (resume == true) {
                                    String SQLusercheckout = "UPDATE login SET bookcount='" + (bookCount + 1) + "', books='" + (books + title) + " / " + "' WHERE userID =" + checkoutid.getText();
                                    String SQLbookcheckout = "UPDATE library SET available ='no', ownerid ='" + userID + "', outdate ='" + currentD + "'  hold ='no' WHERE serial =" + checkoutserial.getText();
                                    PreparedStatement usercheckout = connection.prepareStatement(SQLusercheckout);
                                    usercheckout.execute();
                                    usercheckout.close();

                                    PreparedStatement bookcheckout = connection.prepareStatement(SQLbookcheckout);
                                    bookcheckout.execute();
                                    bookcheckout.close();
                                    updateAll();
                                    resume = false;
                                    checkoutlabel.setText("");
                                }
                            } else {
                                checkoutlabel.setText("Book already out");
                            }
                        } else {
                            checkoutlabel.setText("Maximum books reached");
                        }
                    } else if (usertype.equals("Admin")) {
                        if (available.equals("yes")) {
                            String SQLusercheckout = "UPDATE login SET bookcount='" + (bookCount + 1) + "', books='" + (books + title) + " / " + "' WHERE userID =" + checkoutid.getText();
                            String SQLbookcheckout = "UPDATE library SET available ='no', ownerid ='" + userID + "', outdate ='" + currentD + "'  WHERE serial =" + checkoutserial.getText();
                            PreparedStatement usercheckout = connection.prepareStatement(SQLusercheckout);
                            usercheckout.execute();
                            usercheckout.close();

                            PreparedStatement bookcheckout = connection.prepareStatement(SQLbookcheckout);
                            bookcheckout.execute();
                            bookcheckout.close();
                            updateAll();
                            checkoutlabel.setText("");
                        } else if (onhold.length() > 2) {
                            dialog.show();
                            alertText.setText("Book on hold:" + onhold.substring(4));
                            continueBut.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent ev) {
                                    try {
                                        String SQLusercheckout = "UPDATE login SET bookcount='" + (bookCount + 1) + "', books='" + (booksout + title) + " / " + "' WHERE userID =" + checkoutid.getText();
                                        String SQLbookcheckout = "UPDATE library SET available ='no', ownerid ='" + userID + "', outdate ='" + currentD + "', hold='no'  WHERE serial =" + checkoutserial.getText();
                                        PreparedStatement usercheckout = connection.prepareStatement(SQLusercheckout);
                                        usercheckout.execute();
                                        usercheckout.close();

                                        PreparedStatement bookcheckout = connection.prepareStatement(SQLbookcheckout);
                                        bookcheckout.execute();
                                        bookcheckout.close();

                                        updateAll();
                                        checkoutlabel.setText("");
                                    } catch (SQLException excep) {
                                        System.err.print("Error " + excep);
                                    }
                                }
                            });
                            cancelBut.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    try {
                                        dialog.hide();
                                        connection.close();
                                    } catch (SQLException ex) {
                                        System.err.print("Error " + ex);
                                    }
                                }
                            });
                        }
                    } else {
                        checkoutlabel.setText("Book already out");
                    }
                    connection.close();
                }
                catch (SQLException exc) {
                    System.out.print(exc.getStackTrace()[0].getLineNumber());
                    System.err.print("Error " + exc);
                }
            } else {
                checkoutlabel.setText("Book doesn't exist");
            }
        }
        else {
            checkoutlabel.setText("User doesn't exist");
        }
    }

    @FXML
    public void chooseContinue(ActionEvent event) {
        resume = true;
        checkoutlabel.setText("Press checkout once more");
        confirmbutton.setVisible(false);
        cancelbutton.setVisible(false);
    }

    @FXML
    public void cancel(ActionEvent event) {
        checkoutlabel.setText("");
        resume = false;
        confirmbutton.setVisible(false);
        cancelbutton.setVisible(false);
    }

    @SuppressWarnings("Duplicates")
    @FXML
    public void checkin(ActionEvent e){
        if(bookExists(checkinserial.getText())){
            try{
                Connection connection = dbConnect.getConnection();
                String booksql = "SELECT * FROM library WHERE serial =" + checkinserial.getText();
                ResultSet bookset = connection.createStatement().executeQuery(booksql);
                String ownerID = bookset.getString(6);
                String available = bookset.getString(4);
                String title = bookset.getString(1);
                String hold = bookset.getString(7);
                bookset.close();
                if(available.equals("no")){
                    String userfinder = "SELECT * FROM login WHERE userID='" + ownerID + "'";
                    ResultSet userset = connection.createStatement().executeQuery(userfinder);
                    String userID = userset.getString(3);
                    double fineamount = Double.parseDouble(userset.getString(9).substring(1));
                    int bookcount = Integer.parseInt(userset.getString(11));
                    String books = userset.getString(10);
                    userset.close();
                    if(fineamount < .01){
                        String[] booknames = books.split(" / ");
                        books = "";
                        for(String x : booknames){
                            if(x.equals(title)) {
                                x = "";
                            }
                            else{
                                books = books + x + " / ";
                            }
                        }
                        String userUpdate = "UPDATE login SET bookcount='" + (bookcount - 1) + "', books='" + books + "' WHERE userID ='" + userID +"'";
                        String bookData = "UPDATE library SET available='yes', ownerid ='', outdate ='' WHERE serial =" + checkinserial.getText();

                        PreparedStatement updateUser = connection.prepareStatement(userUpdate);
                        updateUser.execute();
                        updateUser.close();

                        PreparedStatement updateBook = connection.prepareStatement(bookData);
                        updateBook.execute();
                        updateBook.close();

                        updateAll();
                        checkinlabel.setText("");
                        connection.close();
                    }
                    else{
                        dialog.show();
                        alertText.setText("User owes " + fineamount + ", clear book fine?");
                        continueBut.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent ev) {
                                try {
                                    String userID = bookset.getString(6);
                                    String title = bookset.getString(1);
                                    int bookCount = Integer.parseInt(userset.getString(11));
                                    String books = userset.getString(10);
                                    String[] booknames = books.split(" / ");
                                    books = "";
                                    for (String x : booknames) {
                                        if (x.equals(title)) {
                                            x = "";
                                        } else {
                                            books = books + x + " / ";
                                        }
                                    }
                                    String userUpdate = "UPDATE login SET bookcount ='" + (bookCount - 1) + "', books ='" + books + "' WHERE userID = " + userID;
                                    String bookUpdate = "UPDATE library SET available ='yes', ownerid ='', outdate =''  WHERE serial=" + checkinserial.getText();

                                    PreparedStatement updateUser = connection.prepareStatement(userUpdate);
                                    updateUser.execute();

                                    PreparedStatement updateBook = connection.prepareStatement(bookUpdate);
                                    updateBook.execute();

                                    updateAll();
                                    dialog.hide();
                                    checkinlabel.setText("");
                                    connection.close();
                                } catch (SQLException excep) {
                                    System.err.print("Error " + excep);
                                }
                            }
                        });
                        cancelBut.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                try {
                                    dialog.hide();
                                    connection.close();
                                } catch (SQLException ex) {
                                    System.err.print("Error " + ex);
                                }
                            }
                        });
                    }
                }
                else{
                    checkinlabel.setText("Book not checked out");
                }
            }
            catch(SQLException exc){
                System.err.print("Error " + exc);
            }
        }
        else {
            checkinlabel.setText("Book serial not found");
        }
    }

    @SuppressWarnings("Duplicates")
    public boolean userExists(String x) {
        try {
            Connection connection = dbConnect.getConnection();
            String finder = "SELECT * FROM login WHERE userID=" + x;
            ResultSet userset = connection.createStatement().executeQuery(finder);
            if (userset.next()) {
                userset.close();
                connection.close();
                return true;
            } else {
                userset.close();
                connection.close();
                return false;
            }
        } catch (SQLException e) {
            System.err.print("Error " + e);
        }
        return false;
    }

    /**
     * Method checks to see if a book with a serial of x exists in the SQL library table
     * @param x the serial of the book being searched for
     * @return returns true if book found
     */
    @SuppressWarnings("Duplicates")
    public boolean bookExists(String x) {
        try {
            Connection connection = dbConnect.getConnection();
            String finder = "SELECT * FROM library WHERE serial='" + x + "'";
            ResultSet bookset = connection.createStatement().executeQuery(finder);
            if(bookset.next()){
                bookset.close();
                connection.close();
                return true;
            }
            bookset.close();
            connection.close();
        } catch (SQLException e) {
            System.err.print("Error " + e);
        }
        System.out.print("Book not found");
        return false;
    }

    @SuppressWarnings("Duplicates")
    public boolean usernameExists(String x) {
        try {
            Connection connection = dbConnect.getConnection();
            String finder = "SELECT * FROM login WHERE username='" + x + "'";
            ResultSet userset = connection.createStatement().executeQuery(finder);
            if(userset.next()){
                userset.close();
                connection.close();
                return true;
            }
            userset.close();
            connection.close();
        } catch (SQLException e) {
            System.err.print("Error " + e);
        }
        return false;
    }


    public void changeComments(ActionEvent event) {
        try {
            Connection connection = dbConnect.getConnection();
            String serial = commentchangeserial.getText();
            String comments = commentchangearea.getText();
            String sqlcommentchange = "UPDATE library SET comments ='" + comments + "' WHERE serial='" + serial + "'";

            PreparedStatement stmt = connection.prepareStatement(sqlcommentchange);
            stmt.execute();
            stmt.close();
            connection.close();
            updateAll();
        } catch (SQLException e) {
            System.err.print("Error " + e);
        }
    }


    // Quick Reports Panel


    @FXML
    private Button reportprint;
    @FXML
    private Button reportall;
    @FXML
    private Button reportbyhomeroom;
    @FXML
    private Button reportsearchbutton;

    @FXML
    private TextField reportsearch;

    @FXML
    private Label reportlabel;

    @FXML
    private TableView reporttable;

    @FXML
    private TableColumn<QuickReportData, String> reportfn;
    @FXML
    private TableColumn<QuickReportData, String> reportln;
    @FXML
    private TableColumn<QuickReportData, String> reportid;
    @FXML
    private TableColumn<QuickReportData, String> reportfines;
    @FXML
    private TableColumn<QuickReportData, String> reporthomeroom;
    @FXML
    private TableColumn<QuickReportData, String> reportbooks;

    private ObservableList<QuickReportData> data3;

    private void loadReportData() {
        reportsearch.setVisible(false);
        reportsearchbutton.setVisible(false);
        try {

            String sql3 = "SELECT * FROM login";

            Connection connection = dbConnect.getConnection();
            this.data3 = FXCollections.observableArrayList();

            ResultSet rs = connection.createStatement().executeQuery(sql3);
            while (rs.next()) {
                this.data3.add(new QuickReportData(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(7),
                        rs.getString(9),
                        rs.getString(10)));
            }
            this.reportfn.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("firstname"));
            this.reportln.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("lastname"));
            this.reportid.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("userid"));
            this.reporthomeroom.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("homeroom"));
            this.reportfines.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("fines"));
            this.reportbooks.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("books"));
            reportfn.setCellFactory(TextFieldTableCell.forTableColumn());
            reportln.setCellFactory(TextFieldTableCell.forTableColumn());
            reportid.setCellFactory(TextFieldTableCell.forTableColumn());
            reporthomeroom.setCellFactory(TextFieldTableCell.forTableColumn());
            reportfines.setCellFactory(TextFieldTableCell.forTableColumn());
            reportbooks.setCellFactory(TextFieldTableCell.forTableColumn());


            this.reporttable.setItems(null);
            this.reporttable.setItems(this.data3);
            rs.close();
            connection.close();
        } catch (SQLException ex) {
            ex.getCause().printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    @FXML
    public void printQuick(ActionEvent event) {
        print(reporttable);
    }

    @FXML
    public void allOverdue(ActionEvent event) {
        reportsearch.setVisible(false);
        reportsearchbutton.setVisible(false);
        try {
            String sqlall = "SELECT * FROM login WHERE NOT fines ='$0.00'";

            Connection connection = dbConnect.getConnection();
            this.data3 = FXCollections.observableArrayList();

            ResultSet rs = connection.createStatement().executeQuery(sqlall);
            while (rs.next()) {
                this.data3.add(new QuickReportData(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(7),
                        rs.getString(9),
                        rs.getString(10)));
            }
            this.reportfn.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("firstname"));
            this.reportln.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("lastname"));
            this.reportid.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("userid"));
            this.reporthomeroom.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("homeroom"));
            this.reportfines.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("fines"));
            this.reportbooks.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("books"));
            reportfn.setCellFactory(TextFieldTableCell.forTableColumn());
            reportln.setCellFactory(TextFieldTableCell.forTableColumn());
            reportid.setCellFactory(TextFieldTableCell.forTableColumn());
            reporthomeroom.setCellFactory(TextFieldTableCell.forTableColumn());
            reportfines.setCellFactory(TextFieldTableCell.forTableColumn());
            reportbooks.setCellFactory(TextFieldTableCell.forTableColumn());

            rs.close();
            this.reporttable.setItems(null);
            this.reporttable.setItems(this.data3);
            connection.close();
        } catch (SQLException ex) {
            ex.getCause().printStackTrace();
        }
    }

    @FXML
    public void overdueByHomeroom(ActionEvent event) {
        try {
            String teacher = reportsearch.getText();
            String sql = "SELECT * FROM login WHERE NOT fines='0.00' AND homeroom='" + teacher + "'";

            Connection connection = dbConnect.getConnection();
            this.data3 = FXCollections.observableArrayList();

            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                this.data3.add(new QuickReportData(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(7),
                        rs.getString(9),
                        rs.getString(10)));
            }
            this.reportfn.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("firstname"));
            this.reportln.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("lastname"));
            this.reportid.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("userid"));
            this.reporthomeroom.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("homeroom"));
            this.reportfines.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("fines"));
            this.reportbooks.setCellValueFactory(new PropertyValueFactory<QuickReportData, String>("books"));
            reportfn.setCellFactory(TextFieldTableCell.forTableColumn());
            reportln.setCellFactory(TextFieldTableCell.forTableColumn());
            reportid.setCellFactory(TextFieldTableCell.forTableColumn());
            reporthomeroom.setCellFactory(TextFieldTableCell.forTableColumn());
            reportfines.setCellFactory(TextFieldTableCell.forTableColumn());
            reportbooks.setCellFactory(TextFieldTableCell.forTableColumn());

            rs.close();
            this.reporttable.setItems(null);
            this.reporttable.setItems(this.data3);
            connection.close();
        } catch (SQLException ex) {
            ex.getCause().printStackTrace();
        }
    }

    @FXML
    public void showTeachSearch(ActionEvent event) {
        reportsearch.setVisible(true);
        reportsearchbutton.setVisible(true);
    }


    //Customizable searches and reports pane

    @FXML
    private TextField userreportfield;
    @FXML
    private Button userreportsearch;
    @FXML
    private ComboBox userreportbox;

    @FXML
    private TableView userreporttable;

    @FXML
    private TableColumn<UserReportData, String> userreportfn;
    @FXML
    private TableColumn<UserReportData, String> userreportln;
    @FXML
    private TableColumn<UserReportData, String> userreportid;
    @FXML
    private TableColumn<UserReportData, String> userreportusername;
    @FXML
    private TableColumn<UserReportData, String> userreportpassword;
    @FXML
    private TableColumn<UserReportData, String> userreportusertype;
    @FXML
    private TableColumn<UserReportData, String> userreporthomeroom;
    @FXML
    private TableColumn<UserReportData, String> userreportcomments;
    @FXML
    private TableColumn<UserReportData, String> userreportfines;
    @FXML
    private TableColumn<UserReportData, String> userreportbooks;
    @FXML
    private TableColumn<UserReportData, String> userreportbookcount;


    private ObservableList<UserReportData> data4;

    private void loadUserReport() {
        try {

            String sql = "SELECT * FROM login";

            Connection connection = dbConnect.getConnection();
            this.data4 = FXCollections.observableArrayList();

            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                this.data4.add(new UserReportData(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getString(11)));
            }
            this.userreportfn.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("firstname"));
            this.userreportln.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("lastname"));
            this.userreportid.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("userid"));
            this.userreportusername.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("username"));
            this.userreportpassword.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("password"));
            this.userreportusertype.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("usertype"));
            this.userreporthomeroom.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("homeroom"));
            this.userreportcomments.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("comments"));
            this.userreportfines.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("fines"));
            this.userreportbooks.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("books"));
            this.userreportbookcount.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("bookcount"));
            userreportfn.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportln.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportid.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportusername.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportpassword.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportusertype.setCellFactory(TextFieldTableCell.forTableColumn());
            userreporthomeroom.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportcomments.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportfines.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportbooks.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportbookcount.setCellFactory(TextFieldTableCell.forTableColumn());

            rs.close();
            this.userreporttable.setItems(null);
            this.userreporttable.setItems(this.data4);
            connection.close();
        } catch (SQLException ex) {
            ex.getCause().printStackTrace();
        }
    }

    @FXML
    public void userReportPrint(ActionEvent event){
        print(userreporttable);
    }


    @FXML
    private void userReportSearch(ActionEvent event){
        try {
            String searched = userreportfield.getText();
            String option = null;
            String sql;
            if(userreportbox.getSelectionModel().getSelectedItem()!= null) {
                option = (userreportbox.getSelectionModel().getSelectedItem().toString()).toLowerCase();
            }
            if(option== null || option.equals("All")){
                sql = ("SELECT * FROM login WHERE firstname LIKE '%" + searched + "%' OR lastname LIKE '%" + searched + "%'" +
                        " OR userID LIKE '%" + searched + "%' OR username LIKE '%" + searched + "%' OR password LIKE '%" + searched + "%'" +
                        " OR usertype LIKE '%" + searched + "%' OR homeroom LIKE '%" + searched + "%' OR comments LIKE '%" + searched + "%'" +
                        "OR books LIKE '%" + searched + "%' OR fines LIKE '%" + searched + "%' OR bookcount LIKE '%" + searched + "%'");
            }
            else {
                sql = "SELECT * FROM login WHERE " + option + " LIKE'%" + searched + "%'";
            }

            Connection connection = dbConnect.getConnection();
            this.data4 = FXCollections.observableArrayList();

            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                this.data4.add(new UserReportData(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getString(11)));
            }
            this.userreportfn.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("firstname"));
            this.userreportln.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("lastname"));
            this.userreportid.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("userid"));
            this.userreportusername.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("username"));
            this.userreportpassword.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("password"));
            this.userreportusertype.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("usertype"));
            this.userreporthomeroom.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("homeroom"));
            this.userreportcomments.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("comments"));
            this.userreportfines.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("fines"));
            this.userreportbooks.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("books"));
            this.userreportbookcount.setCellValueFactory(new PropertyValueFactory<UserReportData, String>("bookcount"));
            userreportfn.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportln.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportid.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportusername.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportpassword.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportusertype.setCellFactory(TextFieldTableCell.forTableColumn());
            userreporthomeroom.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportcomments.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportfines.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportbooks.setCellFactory(TextFieldTableCell.forTableColumn());
            userreportbookcount.setCellFactory(TextFieldTableCell.forTableColumn());

            rs.close();
            this.userreporttable.setItems(null);
            this.userreporttable.setItems(this.data4);
            connection.close();
        } catch (SQLException ex) {
            System.err.print("Error " + ex);
        }
    }

    @FXML
    private TextField libraryreportfield;
    @FXML
    private Button libraryreportsearch;
    @FXML
    private ComboBox libraryreportbox;

    @FXML
    private TableView libraryreporttable;

    @FXML
    private TableColumn<BookData,String> libraryreporttitle;
    @FXML
    private TableColumn<BookData,String> libraryreportauthor;
    @FXML
    private TableColumn<BookData,String> libraryreportserial;
    @FXML
    private TableColumn<BookData,String> libraryreportavailable;
    @FXML
    private TableColumn<BookData,String> libraryreportcomments;
    @FXML
    private TableColumn<BookData,String> libraryreportownerid;
    @FXML
    private TableColumn<BookData, String> libraryreporthold;
    @FXML
    private TableColumn<BookData,String> libraryreportoutdate;

    private ObservableList<BookData> data5;


    public void loadLibraryReport(){
        {
            try {
                String sql = "SELECT * FROM library";

                Connection connection = dbConnect.getConnection();
                data5 = FXCollections.observableArrayList();

                ResultSet rs = connection.createStatement().executeQuery(sql);
                while (rs.next()) {
                    this.data5.add(new BookData(rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8)));
                }
                this.libraryreporttitle.setCellValueFactory(new PropertyValueFactory<BookData, String>("title"));
                this.libraryreportauthor.setCellValueFactory(new PropertyValueFactory<BookData, String>("author"));
                this.libraryreportserial.setCellValueFactory(new PropertyValueFactory<BookData, String>("serial"));
                this.libraryreportavailable.setCellValueFactory(new PropertyValueFactory<BookData, String>("available"));
                this.libraryreportcomments.setCellValueFactory(new PropertyValueFactory<BookData, String>("comments"));
                this.libraryreportownerid.setCellValueFactory(new PropertyValueFactory<BookData, String>("ownerid"));
                this.libraryreporthold.setCellValueFactory(new PropertyValueFactory<BookData, String>("hold"));
                this.libraryreportoutdate.setCellValueFactory(new PropertyValueFactory<BookData, String>("outdate"));
                libraryreporttitle.setCellFactory(TextFieldTableCell.forTableColumn());
                libraryreportauthor.setCellFactory(TextFieldTableCell.forTableColumn());
                libraryreportserial.setCellFactory(TextFieldTableCell.forTableColumn());
                libraryreportavailable.setCellFactory(TextFieldTableCell.forTableColumn());
                libraryreportcomments.setCellFactory(TextFieldTableCell.forTableColumn());
                libraryreportownerid.setCellFactory(TextFieldTableCell.forTableColumn());
                libraryreporthold.setCellFactory(TextFieldTableCell.forTableColumn());
                libraryreportoutdate.setCellFactory(TextFieldTableCell.forTableColumn());

                rs.close();
                this.libraryreporttable.setItems(null);
                this.libraryreporttable.setItems(this.data5);
                connection.close();
            } catch (SQLException ex) {
                System.err.print("Error " + ex);
            }
        }
    }

    @SuppressWarnings("Duplicates")
    @FXML
    public void libraryReportPrint(ActionEvent event){
        print(libraryreporttable);
    }

    @FXML
    public void libraryReportSearch(ActionEvent event) {
        try {
            String searched = libraryreportfield.getText();
            String option = null;
            String sql;
            if (libraryreportbox.getSelectionModel().getSelectedItem() != null) {
                option = (libraryreportbox.getSelectionModel().getSelectedItem().toString()).toLowerCase();
            }
            if (option == null || option.equals("all")) {
                sql = ("SELECT * FROM library WHERE title LIKE '%" + searched + "%' OR author LIKE '%" + searched + "%'" +
                        " OR serial LIKE '%" + searched + "%' OR available LIKE '%" + searched + "%' OR comments LIKE '%"
                        + searched + "%' OR comments LIKE '%" + searched + "%' OR hold LIKE '%" + searched + "%' OR outdate LIKE '%" + searched + "%'");
            } else {
                sql = "SELECT * FROM library WHERE " + option + " LIKE'%" + searched + "%'";
            }

            Connection connection = dbConnect.getConnection();
            this.data5 = FXCollections.observableArrayList();

            ResultSet rs = connection.createStatement().executeQuery(sql);

            while (rs.next()) {
                this.data5.add(new BookData(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8)));
            }
            this.libraryreporttitle.setCellValueFactory(new PropertyValueFactory<BookData, String>("title"));
            this.libraryreportauthor.setCellValueFactory(new PropertyValueFactory<BookData, String>("author"));
            this.libraryreportserial.setCellValueFactory(new PropertyValueFactory<BookData, String>("serial"));
            this.libraryreportavailable.setCellValueFactory(new PropertyValueFactory<BookData, String>("available"));
            this.libraryreportcomments.setCellValueFactory(new PropertyValueFactory<BookData, String>("comments"));
            this.libraryreportownerid.setCellValueFactory(new PropertyValueFactory<BookData, String>("ownerid"));
            this.libraryreporthold.setCellValueFactory(new PropertyValueFactory<BookData, String>("hold"));
            this.libraryreportoutdate.setCellValueFactory(new PropertyValueFactory<BookData, String>("outdate"));
            libraryreporttitle.setCellFactory(TextFieldTableCell.forTableColumn());
            libraryreportauthor.setCellFactory(TextFieldTableCell.forTableColumn());
            libraryreportserial.setCellFactory(TextFieldTableCell.forTableColumn());
            libraryreportavailable.setCellFactory(TextFieldTableCell.forTableColumn());
            libraryreportcomments.setCellFactory(TextFieldTableCell.forTableColumn());
            libraryreportownerid.setCellFactory(TextFieldTableCell.forTableColumn());
            libraryreporthold.setCellFactory(TextFieldTableCell.forTableColumn());
            libraryreportoutdate.setCellFactory(TextFieldTableCell.forTableColumn());

            rs.close();
            this.libraryreporttable.setItems(null);
            this.libraryreporttable.setItems(this.data5);
            connection.close();
        } catch (SQLException exc) {
            System.err.print("Error " + exc);
        }
    }

    /**
     * The method updates all TableViews in the project.
     */
    public void updateAll(){
        updateFines();
        loadLibraryReport();
        loadReportData();
        loadUserData();
        loadUserReport();
        loadReportData();
    }

    public void print(Node node){
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob printerJob = PrinterJob.createPrinterJob();

        PageLayout pageLayout = printer.createPageLayout(Paper.A4,
                PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);

        printerJob.getJobSettings().setPageLayout(pageLayout);

        System.out.println(pageLayout.getPrintableWidth());
        System.out.println(pageLayout.getPrintableHeight());

        final double scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        final double scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
        Scale scale = new Scale(scaleX, scaleY);
        node.getTransforms().add(scale);

        if(printerJob.showPrintDialog((Stage)ar.getScene().getWindow()) && printerJob.printPage(node)){
            node.getTransforms().remove(scale);
            printerJob.endJob();
        }
        else{
            node.getTransforms().remove(scale);
        }
    }
}
