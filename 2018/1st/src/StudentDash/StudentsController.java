package StudentDash;

import AdminDash.BookData;
import Database.dbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import Login.LoginApp;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class StudentsController implements Initializable {

    @FXML
    private AnchorPane ap;
    @FXML
    private TableView<BookData> booktable;
    @FXML
    private TableColumn<BookData, String> titlecol;
    @FXML
    private TableColumn<BookData, String> authorcol;
    @FXML
    private TableColumn<BookData, String> serialcol;
    @FXML
    private TableColumn<BookData, String> availablecol;
    @FXML
    private TableColumn<BookData, String> commentscol;
    @FXML
    private TableColumn<BookData, String> ownercol;
    @FXML
    private TableColumn<BookData, String> holdcol;
    @FXML
    private TableColumn<BookData, String> outdatecol;
    private TextField searchbox;
    @FXML
    private TextField serialfield;
    @FXML
    private Label holdlabel;

    private ObservableList<BookData> data;
    private String sql = "SELECT * FROM library";
    private int timercounter = 5;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadBookData();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (timercounter > 5) {
                    bookSearch();
                    timercounter--;
                }
            }
        },0,200);
    }

        private void loadBookData(){{
            try {
                Connection connection = dbConnect.getConnection();
                this.data = FXCollections.observableArrayList();

                ResultSet rs = connection.createStatement().executeQuery(sql);
                while (rs.next()) {
                    this.data.add(new BookData(rs.getString(1),
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
                this.commentscol.setCellValueFactory(new PropertyValueFactory<BookData, String>("comments"));
                this.ownercol.setCellValueFactory(new PropertyValueFactory<BookData, String>("ownerid"));
                this.holdcol.setCellValueFactory(new PropertyValueFactory<BookData, String>("hold"));
                this.outdatecol.setCellValueFactory(new PropertyValueFactory<BookData, String>("outdate"));
                titlecol.setCellFactory(TextFieldTableCell.forTableColumn());
                authorcol.setCellFactory(TextFieldTableCell.forTableColumn());
                serialcol.setCellFactory(TextFieldTableCell.forTableColumn());
                availablecol.setCellFactory(TextFieldTableCell.forTableColumn());
                commentscol.setCellFactory(TextFieldTableCell.forTableColumn());
                ownercol.setCellFactory(TextFieldTableCell.forTableColumn());
                holdcol.setCellFactory(TextFieldTableCell.forTableColumn());
                outdatecol.setCellFactory(TextFieldTableCell.forTableColumn());


                this.booktable.setItems(null);
                this.booktable.setItems(this.data);
                connection.close();
            } catch (SQLException ex) {
                ex.getCause().printStackTrace();
            }
        }
    }

    public void bookSearch(){
        try {
            Connection connection = dbConnect.getConnection();
            this.data = FXCollections.observableArrayList();
            String current = searchbox.getText();
            String currentSQL = ("SELECT * FROM library WHERE title LIKE '%" + current + "%' OR author LIKE '%" + current + "%'" +
                    " OR serial LIKE '%" + current + "%' OR available LIKE '%" + current + "%' OR comments LIKE '%" + current + "%'");

            ResultSet rs = connection.createStatement().executeQuery(currentSQL);

            while (rs.next()) {
                this.data.add(new BookData(rs.getString(1),
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
            this.commentscol.setCellValueFactory(new PropertyValueFactory<BookData, String>("comments"));
            this.ownercol.setCellValueFactory(new PropertyValueFactory<BookData, String>("ownerid"));
            this.holdcol.setCellValueFactory(new PropertyValueFactory<BookData, String>("hold"));
            this.outdatecol.setCellValueFactory(new PropertyValueFactory<BookData, String>("outdate"));
            titlecol.setCellFactory(TextFieldTableCell.forTableColumn());
            authorcol.setCellFactory(TextFieldTableCell.forTableColumn());
            serialcol.setCellFactory(TextFieldTableCell.forTableColumn());
            availablecol.setCellFactory(TextFieldTableCell.forTableColumn());
            commentscol.setCellFactory(TextFieldTableCell.forTableColumn());
            ownercol.setCellFactory(TextFieldTableCell.forTableColumn());
            holdcol.setCellFactory(TextFieldTableCell.forTableColumn());
            outdatecol.setCellFactory(TextFieldTableCell.forTableColumn());


            this.booktable.setItems(null);
            this.booktable.setItems(this.data);
            connection.close();
        } catch (SQLException ex) {
            System.err.print("Error" + ex);
        }
    }

    @FXML
    public void addToSearch(){
        timercounter = timercounter + 1;
    }

    @FXML
    public void putOnHold(){
        if(bookExists(serialfield.getText())) {
            try {
                Connection connection = new dbConnect().getConnection();
                String serial = serialfield.getText();
                String sqlHold = "UPDATE library SET available = 'no', hold = 'yes, " + ((Stage)(ap.getScene().getWindow())).getTitle() + "' WHERE serial ='" + serial + "'";
                PreparedStatement statement = connection.prepareStatement(sqlHold);
                statement.execute();
                loadBookData();
                connection.close();
            } catch (SQLException exc) {
                System.err.print("Error " + exc);
            }
        }
        else{
            holdlabel.setText("Book doesn't exist");
        }
    }

    @SuppressWarnings("Duplicates")
    public boolean bookExists(String x) {
        try {
            Connection connection = dbConnect.getConnection();
            String finder = "SELECT * FROM library WHERE serial=" + x;
            ResultSet bookset = connection.createStatement().executeQuery(finder);
            if (bookset.next()) {
                connection.close();
                return true;
            } else {
                connection.close();
                return false;
            }
        } catch (SQLException e) {
            System.err.print("Error " + e);
        }
        return false;
    }
}
