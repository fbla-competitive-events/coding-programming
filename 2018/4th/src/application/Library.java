package application;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** 
 * The aim of this code is to:
 * 1. Accept login from administrator
 * 2. Choose between 'borrow' and 'return' for 'student' or 'teacher'
 * 3. Upon selecting any of these combinations, the outcome should be an entry for teacher and student id and book id respectively.
 * 4. After the login, the methods for borrow and return should run and the message 'Transaction successful' should be output, 
 * while showing the student/teacher's name, id, book they borrowed and the book id, date borrowed, and return date.
 * 
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Library extends Application {

	private TableView<Student> studentTable = new TableView<Student>();
	private TableView<Teacher> teacherTable = new TableView<Teacher>();
	private TableView<LibraryBook> bookTable = new TableView<LibraryBook>();

	LibraryFile libraryFileReader = new LibraryFile();
	LibraryUtils libraryUtils = new LibraryUtils();
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM-dd-yyyy");
	Label errMsg = new Label("");

	public ObservableList<LibraryBook> books;
	public ObservableList<Student> students;
	public ObservableList<Teacher> teachers;

	String bookfileName;
	String studentsfileName;
	String teachersfileName;
	String adminfileName;
	String css;

	private static int studentCount;
	private static int teacherCount;
	private static int bookCount;
	Label err = new Label();

	public static void main(String[] args) {
		launch(args);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * The code below is the code for the login screen, as well as the code for the main screen 
	 * that appears after the username and password have been correctly entered.
	 * The 'Register' buttons allow the librarian to view library books in addition to students and teachers enrolled in the school.
	 * The student and teacher buttons, when clicked, display a table with a list of students and teachers. 
	 * New students and teachers may be added by entering a first and last name and clicking the 'Add' button.
	 * To delete a student or teacher record, select a row of data in the table and click the 'Delete' button.
	 * A pop-up box asking for confirmation to delete the record will appear. Select 'Delete' to permanently delete the data, or
	 * 'Cancel' to keep the data.
	 * The 'Add/Edit Book' button functions in a similar manner, except the librarian must enter the book title and the author name, 
	 * instead of a first and last name.
	 */
	public void start(Stage primaryStage) throws FileNotFoundException {

		final Parameters params = getParameters();
		final List<String> parameters = params.getRaw();
		bookfileName = !parameters.isEmpty() ? parameters.get(0) : "src/application/Book.txt";
		studentsfileName = !parameters.isEmpty() ? parameters.get(1) : "src/application/Student.txt";
		teachersfileName = !parameters.isEmpty() ? parameters.get(2) : "src/application/Teacher.txt";
		adminfileName = !parameters.isEmpty() ? parameters.get(3) : "src/application/Admin.txt";

		books = libraryFileReader.getBookData(bookfileName);
		students = libraryFileReader.getStudentData(studentsfileName);
		teachers = libraryFileReader.getTeacherData(teachersfileName);
		libraryFileReader.readAdminData(adminfileName);

		bookCount = libraryUtils.getNextBookIds(books);
		studentCount = libraryUtils.getNextIds(students);
		teacherCount = libraryUtils.getNextIds(teachers);

		Class<?> clazz = Library.class;
		InputStream input = clazz.getResourceAsStream("book-image.png");
		Image image = new Image(input);
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(100);
		imageView.setFitWidth(100);
		primaryStage.setTitle("Anderson High School Library");
		primaryStage.setHeight(700);
		primaryStage.setWidth(1000);
		primaryStage.show();

		GridPane grid = new GridPane();
		grid.getStyleClass().addAll("pane");
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(20);
		grid.setVgap(20);
		grid.setPadding(new Insets(300, 300, 300, 300));
		grid.add(imageView, 0, 0);

		Scene scene = new Scene(grid, 300, 300);
		URL url = this.getClass().getResource("application.css");
		if (url == null) {
			System.out.println("Resource not found. Aborting.");
			System.exit(-1);
		}
		css = url.toExternalForm();
		scene.getStylesheets().add(css);
		primaryStage.setScene(scene);

		Text title = new Text("Anderson High School Library");
		title.setFont(Font.font("Britannic Bold", FontWeight.NORMAL, 35));
		grid.add(title, 0, 1, 7, 1);

		VBox adminBox = new VBox();
		adminBox.setPadding(new Insets(25, 25, 25, 25));
		Text adminScreenText = new Text("Admin Login");
		adminScreenText.setFont(Font.font("Lucida Sans", FontWeight.NORMAL, 20));
		Label userName = new Label("Username: ");
		userName.getStyleClass().add("labelText");
		TextField userTextField = new TextField();
		Label pw = new Label("Password: ");
		pw.getStyleClass().add("labelText");
		PasswordField pwField = new PasswordField();
		Text blank = new Text("");

		HBox userNameBox = new HBox();
		userNameBox.setPrefWidth(500);
		userNameBox.getChildren().addAll(userName, userTextField);
		HBox pwBox = new HBox();
		pwBox.getChildren().addAll(pw, pwField);

		adminBox.getChildren().addAll(adminScreenText, userNameBox, pwBox, blank);
		adminBox.setSpacing(20);
		adminBox.setBackground(
				new Background(new BackgroundFill(Color.LIGHTSTEELBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		grid.add(adminBox, 0, 2);

		Button ebtn = new Button("Exit");
		ebtn.getStyleClass().addAll("buttonLarge");
		Button btn = new Button("Submit");
		btn.getStyleClass().addAll("buttonLarge");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().addAll(btn, ebtn);
		grid.add(hbBtn, 0, 5);

		ebtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.close();
			}
		});

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (userTextField.getText() == null || pwField.getText() == null) {
					Text errorMsg = new Text("Error: Please enter your username and password.");
					grid.add(errorMsg, 1, 6);
				}

				if (!(userTextField.getText().equals("admin")) || (!(pwField.getText().equals("admin")))) {
					Text errorMsg = new Text("Error: Invalid username or password.");
					grid.add(errorMsg, 1, 6);
				}

				else {

					grid.getChildren().clear();
					grid.setPadding(new Insets(100, 100, 200, 100));

					Text registerLabel = new Text("Register");
					registerLabel.getStyleClass().addAll("heading");
					Text chkoutReturnLabel = new Text("Checkout/Return");
					chkoutReturnLabel.getStyleClass().addAll("heading");
					Text reportsLabel = new Text("Reports");
					reportsLabel.getStyleClass().addAll("heading");

					Button editStudentBtn = new Button("Add/Edit Student");
					editStudentBtn.getStyleClass().addAll("buttonLarge");
					Button editTeacherBtn = new Button("Add/Edit Teacher");
					editTeacherBtn.getStyleClass().addAll("buttonLarge");
					Button editBookBtn = new Button("Add/Edit Book");
					editBookBtn.getStyleClass().addAll("buttonLarge");
					Button borrowBookBtn = new Button("Checkout Book");
					borrowBookBtn.getStyleClass().addAll("buttonLarge");
					Button returnBookBtn = new Button("Return Book");
					returnBookBtn.getStyleClass().addAll("buttonLarge");
					Button overDueBooksBtn = new Button("Overdue Books");
					overDueBooksBtn.getStyleClass().addAll("buttonLarge");
					Button checkedOutBooksBtn = new Button("Borrowed Books");
					checkedOutBooksBtn.getStyleClass().addAll("buttonLarge");
					Button exitBtn = new Button("Exit");
					exitBtn.getStyleClass().addAll("buttonLarge");

					grid.add(imageView, 0, 0);

					grid.add(registerLabel, 0, 1);
					grid.add(editStudentBtn, 0, 2);
					grid.add(editTeacherBtn, 0, 3);
					grid.add(editBookBtn, 0, 4);

					grid.add(chkoutReturnLabel, 1, 1);
					grid.add(borrowBookBtn, 1, 2);
					grid.add(returnBookBtn, 1, 3);

					grid.add(reportsLabel, 2, 1);
					grid.add(overDueBooksBtn, 2, 2);
					grid.add(checkedOutBooksBtn, 2, 3);

					grid.add(exitBtn, 2, 8);

					editStudentButtonAction(editStudentBtn, primaryStage);
					editTeacherButtonAction(editTeacherBtn, primaryStage);
					editBookButtonAction(editBookBtn, primaryStage);
					returnBookButtonAction(returnBookBtn);
					borrowedBookButtonAction(borrowBookBtn);
					overdueBooksButtonAction(overDueBooksBtn, primaryStage);
					checkedoutBooksButtonAction(checkedOutBooksBtn, primaryStage);

					exitBtn.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							primaryStage.close();
						}

					});

				}
			}
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void editStudentButtonAction(Button editStudentBtn, Stage primaryStage) {
		editStudentBtn.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			public void handle(ActionEvent args) {
				err.setVisible(false);
				Scene scene = new Scene(new Group());
				scene.getStylesheets().add(css);
				Stage stage = new Stage();
				stage.setTitle("Student Members");
				stage.setWidth(700);
				stage.setHeight(700);
				stage.setScene(scene);

				GridPane editStudGrid = new GridPane();
				editStudGrid.setPrefWidth(700);
				editStudGrid.setPrefHeight(700);
				editStudGrid.getStyleClass().addAll("pane");

				Label label = new Label("Student Members");
				label.getStyleClass().addAll("heading");

				studentTable.setEditable(true);
				studentTable.setPrefHeight(300);
				studentTable.setPrefWidth(500);

				Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFactory = new Callback<TableColumn<Student, String>, TableCell<Student, String>>() {
					public TableCell call(TableColumn p) {
						return new EditingCell();
					}
				};

				TableColumn<Student, String> idCol = new TableColumn<Student, String>("ID number");
				idCol.setPrefWidth(80);
				TableColumn<Student, String> firstNameCol = new TableColumn<Student, String>("First Name");
				firstNameCol.setPrefWidth(210);
				TableColumn<Student, String> lastNameCol = new TableColumn<Student, String>("Last Name");
				lastNameCol.setPrefWidth(210);

				idCol.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));

				firstNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
				firstNameCol.setCellFactory(cellFactory);

				firstNameCol.setOnEditCommit(new EventHandler<CellEditEvent<Student, String>>() {
					public void handle(CellEditEvent<Student, String> t) {

						if (t.getNewValue().equals(null) || t.getNewValue().trim().equals("")) {
							System.out.println("Entered if for null firstName");
							err.setText("Error: Empty First Name. Please Enter Valid Value.");
							err.setVisible(true);
						}
						// Write method for the duplicate value entry.
						else {

							((Student) t.getTableView().getItems().get(t.getTablePosition().getRow()))
									.setFirstName(new SimpleStringProperty(t.getNewValue()));
							err.setVisible(false);
						}

					}
				});

				lastNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
				lastNameCol.setCellFactory(cellFactory);
				lastNameCol.setOnEditCommit(new EventHandler<CellEditEvent<Student, String>>() {
					public void handle(CellEditEvent<Student, String> t) {

						if (t.getNewValue().equals(null) || t.getNewValue().trim().equals("")) {
							err.setText("Error: Empty Last Name. Please Enter Valid Value.");
							err.setVisible(true);
						}

						else {
							((Student) t.getTableView().getItems().get(t.getTablePosition().getRow()))
									.setLastName(new SimpleStringProperty(t.getNewValue()));
							err.setVisible(false);
						}
					}
				});

				studentTable.setItems(students);
				studentTable.getColumns().addAll(idCol, firstNameCol, lastNameCol);

				TextField firstNameField = new TextField();
				firstNameField.setPromptText("First Name");
				firstNameField.setMaxWidth(100);

				TextField lastNameField = new TextField();
				lastNameField.setPromptText("Last Name");
				lastNameField.setMaxWidth(100);

				Button addButton = new Button("Add");
				addButton.getStyleClass().addAll("buttonSmall");
				Button exitButton = new Button("Back");
				exitButton.getStyleClass().addAll("buttonSmall");
				Button deleteButton = new Button("Delete Selected Student");
				deleteButton.getStyleClass().addAll("buttonXLarge");

				VBox vbox = new VBox();
				vbox.setSpacing(5);
				vbox.setPadding(new Insets(90, 0, 0, 90));
				HBox hb = new HBox();
				hb.setSpacing(3);
				HBox hb1 = new HBox();
				hb1.setSpacing(3);

				exitButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						stage.close();
					}
				});

				addButton.setOnAction(new EventHandler<ActionEvent>() {

					boolean isDuplicate = false;

					public void handle(ActionEvent e) {
						
						if ((firstNameField.getText() != null && firstNameField.getText().trim().length() > 0)
								&& (lastNameField.getText() != null && lastNameField.getText().trim().length() > 0)) {
							
							isDuplicate = libraryUtils.isDuplicate(firstNameField.getText(), lastNameField.getText(), "Student"
									, students, teachers);

							if (isDuplicate) {
								Alert alert = new Alert(AlertType.CONFIRMATION);
								alert.setTitle("Duplicate Entry");
								alert.setHeaderText("Student(s) with the same first and last name exist(s). Are you sure you want to add this entry?");

								ButtonType buttonTypeDelete = new ButtonType("Add");
								ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

								alert.getButtonTypes().setAll(buttonTypeDelete, buttonTypeCancel);

								Optional<ButtonType> result = alert.showAndWait();

								boolean cancelled = false;
								System.out.println(result.get().getText());
								if (result.get().getText().equals("Add")){
									System.out.println("Adding...");
									studentCount += 1;
									students.add(new Student(Integer.toString(studentCount), lastNameField.getText(),
											firstNameField.getText()));
									lastNameField.clear();
									firstNameField.clear();
								}else {
									lastNameField.clear();
									firstNameField.clear();
								    cancelled = true;// ... user chose CANCEL or closed the dialog
								}
								
							}

							else {
								studentCount += 1;
								students.add(new Student(Integer.toString(studentCount), lastNameField.getText(),
										firstNameField.getText()));
								lastNameField.clear();
								firstNameField.clear();
							}

						} else {
							err.setText("Please Enter Valid First and Last Names.");
							err.setVisible(true);
						}
					}

				});

				deleteButton.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent e) {
						Student delStudent = (Student) studentTable.getSelectionModel().getSelectedItem();
					
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Confirm Delete");
						alert.setHeaderText("Are you sure you want to delete this student?");

						ButtonType buttonTypeDelete = new ButtonType("Delete");
						ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

						alert.getButtonTypes().setAll(buttonTypeDelete, buttonTypeCancel);

						Optional<ButtonType> result = alert.showAndWait();

						boolean hasCheckedOut = false;
						boolean cancelled = false;
						System.out.println(result.get().getText());
						if (result.get().getText().equals("Delete")){
							System.out.println("Deleting...");
							for (int i = 0; i < books.size(); i++) {
								try {
									if (books.get(i).getCheckedOutId().equals(delStudent.getId())) {
										hasCheckedOut = true;
										Alert alert1 = new Alert(AlertType.INFORMATION);
										alert1.setTitle("Delete Student Failed");
										alert1.setHeaderText(null);
										alert1.setContentText("Error: The student with id \n" + delStudent.getId()
										+ " has a book checked out.\n Please return the book \n"
										+ "before deleting this student.\n");
										alert1.showAndWait();
										break;
									}
								}
								catch(NullPointerException ne) {
									System.out.println("next");
								}
								
							}
						}else {
						    cancelled = true;// ... user chose CANCEL or closed the dialog
						}
						
						if (!hasCheckedOut && !cancelled) {
							studentTable.getItems().remove(delStudent);
							for (int i = 0; i < students.size(); i++) {
								if (students.get(i).getId().equals(delStudent.getId())) {
									students.remove(i);
								}
							}
						}
					}

				});

				firstNameField.clear();
				lastNameField.clear();

				hb.getChildren().addAll(firstNameField, lastNameField, addButton);
				hb1.getChildren().addAll(deleteButton, exitButton);
				vbox.getChildren().addAll(label, studentTable, err, hb, hb1);
				editStudGrid.getChildren().addAll(vbox);
				((Group) scene.getRoot()).getChildren().addAll(editStudGrid);

				stage.setScene(scene);
				stage.show();
			}

		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void editTeacherButtonAction(Button editTeacherBtn, Stage primaryStage) {
		editTeacherBtn.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent event) {

				Scene scene = new Scene(new Group());
				scene.getStylesheets().add(css);
				Stage stage = new Stage();
				stage.setTitle("Teacher Members");
				stage.setWidth(700);
				stage.setHeight(700);
				stage.setScene(scene);

				GridPane editTeachGrid = new GridPane();
				editTeachGrid.setPrefWidth(700);
				editTeachGrid.setPrefHeight(700);
				editTeachGrid.getStyleClass().addAll("pane");

				Label label = new Label("Teacher Members");
				label.getStyleClass().addAll("heading");

				teacherTable.setEditable(true);
				teacherTable.setPrefHeight(300);
				teacherTable.setPrefWidth(500);

				Callback<TableColumn<Teacher, String>, TableCell<Teacher, String>> cellFactory = new Callback<TableColumn<Teacher, String>, TableCell<Teacher, String>>() {
					public TableCell call(TableColumn p) {
						return new EditingCell();
					}
				};

				TableColumn<Teacher, String> idCol = new TableColumn<Teacher, String>("ID number");
				idCol.setPrefWidth(80);
				TableColumn<Teacher, String> firstNameCol = new TableColumn<Teacher, String>("First Name");
				firstNameCol.setPrefWidth(210);
				TableColumn<Teacher, String> lastNameCol = new TableColumn<Teacher, String>("Last Name");
				lastNameCol.setPrefWidth(210);

				idCol.setCellValueFactory(new PropertyValueFactory<Teacher, String>("id"));

				firstNameCol.setCellValueFactory(new PropertyValueFactory<Teacher, String>("firstName"));
				firstNameCol.setCellFactory(cellFactory);
				firstNameCol.setOnEditCommit(new EventHandler<CellEditEvent<Teacher, String>>() {
					public void handle(CellEditEvent<Teacher, String> t) {
						((Borrower) t.getTableView().getItems().get(t.getTablePosition().getRow()))
								.setFirstName(new SimpleStringProperty(t.getNewValue()));
					}
				});

				lastNameCol.setCellValueFactory(new PropertyValueFactory<Teacher, String>("lastName"));
				lastNameCol.setCellFactory(cellFactory);
				lastNameCol.setOnEditCommit(new EventHandler<CellEditEvent<Teacher, String>>() {
					public void handle(CellEditEvent<Teacher, String> t) {
						((Teacher) t.getTableView().getItems().get(t.getTablePosition().getRow()))
								.setLastName(new SimpleStringProperty(t.getNewValue()));
					}
				});

				teacherTable.setItems(teachers);
				teacherTable.getColumns().addAll(idCol, firstNameCol, lastNameCol);

				TextField firstNameField = new TextField();
				firstNameField.setPromptText("First Name");
				firstNameField.setMaxWidth(100);

				TextField lastNameField = new TextField();
				lastNameField.setPromptText("Last Name");
				lastNameField.setMaxWidth(100);

				Button addButton = new Button("Add");
				addButton.getStyleClass().addAll("buttonSmall");
				Button exitButton = new Button("Back");
				exitButton.getStyleClass().addAll("buttonSmall");
				Button deleteButton = new Button("Delete Selected Teacher");
				deleteButton.getStyleClass().addAll("buttonXLarge");

				Label err = new Label();
				err.setVisible(false);

				VBox vbox = new VBox();
				vbox.setSpacing(5);
				vbox.setPadding(new Insets(90, 0, 0, 90));
				HBox hb = new HBox();
				hb.setSpacing(3);

				exitButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						stage.close();
					}
				});
				addButton.setOnAction(new EventHandler<ActionEvent>() {

					boolean isDuplicate = false;

					public void handle(ActionEvent e) {
						if ((firstNameField.getText() != null && firstNameField.getText().trim().length() > 0)
								&& (lastNameField.getText() != null && lastNameField.getText().trim().length() > 0)) {
							
							isDuplicate = libraryUtils.isDuplicate(firstNameField.getText(), lastNameField.getText(), "Teacher"
									, students, teachers);

							if (isDuplicate) {
								Alert alert = new Alert(AlertType.CONFIRMATION);
								alert.setTitle("Duplicate Entry");
								alert.setHeaderText("Teacher(s) with the same first and last name exist(s). Are you sure you want to add this entry?");

								ButtonType buttonTypeDelete = new ButtonType("Add");
								ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

								alert.getButtonTypes().setAll(buttonTypeDelete, buttonTypeCancel);

								Optional<ButtonType> result = alert.showAndWait();

								boolean cancelled = false;
								System.out.println(result.get().getText());
								if (result.get().getText().equals("Add")){
									System.out.println("Adding...");
									teacherCount += 1;
									teachers.add(new Teacher(Integer.toString(teacherCount), lastNameField.getText(),
											firstNameField.getText()));
									lastNameField.clear();
									firstNameField.clear();
								}else {
									lastNameField.clear();
									firstNameField.clear();
								    cancelled = true;// ... user chose CANCEL or closed the dialog
								}
								
							}
							else {
								teacherCount += 1;
								teachers.add(new Teacher(Integer.toString(teacherCount), lastNameField.getText(),
										firstNameField.getText()));
								err.setVisible(false);
							}

						} else {
							err.setText("Please Enter Valid First and Last Names.");
							err.setVisible(true);
						}

						firstNameField.clear();
						lastNameField.clear();
					}
				});

				deleteButton.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent e) {
						Teacher delTeacher = (Teacher) teacherTable.getSelectionModel().getSelectedItem();

						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Confirm Delete");
						alert.setHeaderText("Are you sure you want to delete this teacher?");

						ButtonType buttonTypeDelete = new ButtonType("Delete");
						ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

						alert.getButtonTypes().setAll(buttonTypeDelete, buttonTypeCancel);

						Optional<ButtonType> result = alert.showAndWait();

						boolean hasCheckedOut = false;
						boolean cancelled = false;
						System.out.println(result.get().getText());
						if (result.get().getText().equals("Delete")){
							for (int i = 0; i < books.size(); i++) {
								try {
									if (books.get(i).getCheckedOutId().equals(delTeacher.getId())) {
										hasCheckedOut = true;
										Alert alert1 = new Alert(AlertType.INFORMATION);
										alert1.setTitle("Delete Teacher Failed");
										alert1.setHeaderText(null);
										alert1.setContentText("Error: The teacher with id \n" + delTeacher.getId()
										+ " has a book checked out.\n Please return the book \n"
										+ "before deleting this teacher.\n");
										alert1.showAndWait();
										break;
									}
								}
								catch(NullPointerException ne) {
									System.out.println("Deleting next");
								}								
							}
						}else {
						    cancelled = true;// ... user chose CANCEL or closed the dialog
						}
						
						if (!hasCheckedOut && !cancelled) {
							teacherTable.getItems().remove(delTeacher);
							for (int i = 0; i < teachers.size(); i++) {
								if (teachers.get(i).getId().equals(delTeacher.getId())) {
									teachers.remove(i);
								}
							}
						}
					}
		

				});

				hb.getChildren().addAll(firstNameField, lastNameField, addButton);
				HBox hb1 = new HBox();
				hb1.getChildren().addAll(deleteButton, exitButton);
				hb1.setSpacing(3);
				vbox.getChildren().addAll(label, teacherTable, err, hb, hb1);
				editTeachGrid.getChildren().addAll(vbox);
				((Group) scene.getRoot()).getChildren().addAll(editTeachGrid);

				stage.setScene(scene);
				stage.show();
			}

		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void editBookButtonAction(Button editBookBtn, Stage primaryStage) {
		editBookBtn.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("unchecked")
			public void handle(ActionEvent e) {
				Scene scene = new Scene(new Group());
				scene.getStylesheets().add(css);
				Stage stage = new Stage();
				stage.setTitle("Library Books");
				stage.setWidth(700);
				stage.setHeight(700);
				stage.setScene(scene);

				GridPane viewGrid = new GridPane();
				viewGrid.setPrefWidth(700);
				viewGrid.setPrefHeight(700);
				viewGrid.getStyleClass().addAll("pane");

				Label err = new Label();
				err.setVisible(false);

				Label label = new Label("Library Books");
				label.getStyleClass().addAll("heading");

				bookTable.setPrefHeight(300);
				bookTable.setPrefWidth(500);

				TableColumn<LibraryBook, String> bookIdCol = new TableColumn<LibraryBook, String>("Book ID");
				bookIdCol.setPrefWidth(50);
				TableColumn<LibraryBook, String> titleCol = new TableColumn<LibraryBook, String>("Title");
				titleCol.setPrefWidth(250);
				TableColumn<LibraryBook, String> authorNameCol = new TableColumn<LibraryBook, String>("Author");
				authorNameCol.setPrefWidth(100);
				TableColumn<LibraryBook, String> isCheckedOutCol = new TableColumn<LibraryBook, String>("Status");
				isCheckedOutCol.setPrefWidth(100);

				bookIdCol.setCellValueFactory(new PropertyValueFactory<LibraryBook, String>("bookId"));

				titleCol.setCellValueFactory(new PropertyValueFactory<LibraryBook, String>("bookName"));

				authorNameCol.setCellValueFactory(new PropertyValueFactory<LibraryBook, String>("authorName"));

				isCheckedOutCol.setCellValueFactory(new PropertyValueFactory<LibraryBook, String>("isCheckedOut"));

				bookTable.setItems(books);

				bookTable.getColumns().addAll(bookIdCol, titleCol, authorNameCol, isCheckedOutCol);

				TextField titleField = new TextField();
				titleField.setPromptText("Title");
				titleField.setMaxWidth(100);

				TextField authorField = new TextField();
				authorField.setPromptText("Author");
				authorField.setMaxWidth(100);

				Label addBookLabel = new Label("Add Book");
				Label deleteBookLabel = new Label("Delete Book");
				Button addButton = new Button("Add");
				addButton.getStyleClass().addAll("buttonSmall");
				Button cancelButton = new Button("Back");
				cancelButton.getStyleClass().addAll("buttonSmall");
				Button deleteButton = new Button("Delete Selected Book");
				deleteButton.getStyleClass().addAll("buttonXLarge");
				HBox hb1 = new HBox();
				hb1.getChildren().addAll(deleteButton, cancelButton);
				VBox vb = new VBox();
				vb.getChildren().addAll(hb1);

				deleteButton.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {

						LibraryBook delBook = (LibraryBook) bookTable.getSelectionModel().getSelectedItem();
						String deleteId = delBook.getBookId();

						ArrayList<String> temp;
						
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Confirm Delete");
						alert.setHeaderText("Are you sure you want to delete this book?");

						ButtonType buttonTypeDelete = new ButtonType("Delete");
						ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

						alert.getButtonTypes().setAll(buttonTypeDelete, buttonTypeCancel);

						Optional<ButtonType> result = alert.showAndWait();

			
						if (result.get().getText().equals("Delete")){
							for (int i = 0; i < teachers.size(); i++) {
								temp = teachers.get(i).getBookIds();
								if (temp.contains(deleteId)) {
									teachers.get(i).getBookIds().remove(deleteId);
								}
							}

							for (int i = 0; i < students.size(); i++) {
								temp = students.get(i).getBookIds();
								if (temp.contains(deleteId)) {
									students.get(i).getBookIds().remove(deleteId);
								}
							}

							for (int i = 0; i < books.size(); i++) {
								bookTable.getItems().remove(delBook);
								if (books.get(i).getBookId().equals(deleteId)) {
									books.remove(i);
								}
							}
						}
					}
				});
				cancelButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						stage.close();
					}
				});
				addButton.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						if ((titleField.getText() != null && titleField.getText().trim().length() > 0)
								&& (authorField.getText() != null && authorField.getText().trim().length() > 0)) {

							bookCount += 1;
							boolean isCheckedOut = false;
							books.add(new LibraryBook(Integer.toString(bookCount), titleField.getText(),
									authorField.getText(), isCheckedOut, null, null, null, null, null));
						} else {
							err.setText("Please enter valid Book author and title");
							err.setVisible(true);
						}

						titleField.clear();
						authorField.clear();
					}
				});

				VBox vbox = new VBox();
				vbox.setSpacing(5);
				vbox.setPadding(new Insets(90, 0, 0, 90));
				HBox hb2 = new HBox();
				hb2.getChildren().addAll(titleField, authorField, addButton);
				hb2.setSpacing(3);
				hb1.setSpacing(3);
				vbox.getChildren().addAll(label, bookTable, err, addBookLabel, hb2, deleteBookLabel, hb1);
				viewGrid.getChildren().addAll(vbox);
				((Group) scene.getRoot()).getChildren().addAll(viewGrid);

				stage.setScene(scene);
				stage.show();

			}
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void returnBookButtonAction(Button returnBookBtn) {
		returnBookBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				Scene scene = new Scene(new Group());
				scene.getStylesheets().add(css);
				Stage stage = new Stage();
				stage.setTitle("Return Books");
				stage.setWidth(300);
				stage.setHeight(300);
				stage.setScene(scene);
				VBox vbox = new VBox();
				vbox.setPrefWidth(300);
				vbox.setPrefHeight(300);
				vbox.setSpacing(20);
				vbox.setAlignment(Pos.CENTER);
				GridPane checkGrid = new GridPane();
				checkGrid.getStyleClass().addAll("pane");
				checkGrid.setPrefWidth(300);
				checkGrid.setPrefHeight(300);
				checkGrid.setAlignment(Pos.CENTER);

				Label bookIdLabel = new Label("Book ID: ");
				TextField bookIdField = new TextField();
				Label msg = new Label();
				msg.setVisible(false);

				Button submitBtn = new Button("Submit");
				submitBtn.getStyleClass().addAll("buttonSmall");
				Button exitButton = new Button("Cancel");
				exitButton.getStyleClass().addAll("buttonSmall");

				HBox hb1 = new HBox();
				hb1.getChildren().addAll(bookIdLabel, bookIdField);
				hb1.setSpacing(3);
				hb1.setPrefWidth(300);
				hb1.setAlignment(Pos.CENTER);

				HBox hb2 = new HBox();
				hb2.getChildren().addAll(submitBtn, exitButton);
				hb2.setSpacing(3);
				hb2.setPrefWidth(300);
				hb2.setAlignment(Pos.CENTER);

				vbox.getChildren().addAll(hb1, hb2, msg);
				checkGrid.getChildren().addAll(vbox);
				((Group) scene.getRoot()).getChildren().addAll(checkGrid);

				stage.setScene(scene);
				stage.show();
				exitButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						stage.close();
					}
				});

				submitBtn.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent e) {
						msg.setText(libraryUtils.bookReturn(bookIdField, books, students, teachers));
						msg.setVisible(true);
					}
				});

			}
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void borrowedBookButtonAction(Button borrowBookBtn) {
		borrowBookBtn.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("unchecked")
			public void handle(ActionEvent e) {

				Scene scene = new Scene(new Group());
				scene.getStylesheets().add(css);
				Stage stage = new Stage();
				stage.setTitle("Borrow Books");
				stage.setWidth(700);
				stage.setHeight(700);
				stage.setScene(scene);

				GridPane resultsGrid = new GridPane();
				resultsGrid.setPrefWidth(700);
				resultsGrid.setPrefHeight(700);
				resultsGrid.getStyleClass().addAll("pane");
				resultsGrid.setAlignment(Pos.CENTER);

				TableView<LibraryBook> availableBooks = new TableView<LibraryBook>();
				Label label = new Label("Borrow Books");
				label.setFont(new Font("Arial", 20));
				availableBooks.setPrefHeight(300);
				availableBooks.setPrefWidth(500);
				
				TableColumn<LibraryBook, String> bookIdCol = new TableColumn<LibraryBook, String>("Book ID");
				bookIdCol.setPrefWidth(50);
				TableColumn<LibraryBook, String> titleCol = new TableColumn<LibraryBook, String>("Title");
				titleCol.setPrefWidth(250);
				TableColumn<LibraryBook, String> authorNameCol = new TableColumn<LibraryBook, String>("Author");
				authorNameCol.setPrefWidth(100);
				TableColumn<LibraryBook, String> isCheckedOutCol = new TableColumn<LibraryBook, String>("Status");
				isCheckedOutCol.setPrefWidth(100);
				bookIdCol.setCellValueFactory(new PropertyValueFactory<LibraryBook, String>("bookId"));
				titleCol.setCellValueFactory(new PropertyValueFactory<LibraryBook, String>("bookName"));
				authorNameCol.setCellValueFactory(new PropertyValueFactory<LibraryBook, String>("authorName"));
				isCheckedOutCol.setCellValueFactory(new PropertyValueFactory<LibraryBook, String>("isCheckedOut"));
				availableBooks.setItems(books);
				availableBooks.getColumns().addAll(bookIdCol, titleCol, authorNameCol, isCheckedOutCol);

				Label bookIdLabel = new Label("Book ID:        ");
				TextField bookIdField = new TextField();
				
				Label borrowerIdLabel = new Label("Borrower ID: ");
				TextField borrowerIdField = new TextField();

				ToggleGroup tg = new ToggleGroup();
				RadioButton isStudent = new RadioButton("Student");
				isStudent.setToggleGroup(tg);
				isStudent.setSelected(true);
				RadioButton isTeacher = new RadioButton("Teacher");
				isTeacher.setToggleGroup(tg);

				Button submitBtn = new Button("Check Out");
				submitBtn.getStyleClass().addAll("buttonLarge");
				Button exitButton = new Button("Cancel");
				exitButton.getStyleClass().addAll("buttonSmall");

				HBox hbox2 = new HBox();
				hbox2.setSpacing(3);
				hbox2.getChildren().addAll(borrowerIdLabel, borrowerIdField);

				HBox hbox3 = new HBox();
				hbox3.setSpacing(3);
				hbox3.getChildren().addAll(submitBtn, exitButton);

				HBox hbox4 = new HBox();
				hbox4.setSpacing(3);
				hbox4.getChildren().addAll(isStudent, isTeacher);

				Label msg = new Label("");
				msg.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
				msg.setVisible(false);

				VBox vbox = new VBox();
				vbox.setSpacing(20);
				vbox.setAlignment(Pos.CENTER);
				vbox.getChildren().addAll(label, availableBooks, hbox2, hbox4, hbox3, msg); //hbox1
				resultsGrid.getChildren().addAll(vbox);
				((Group) scene.getRoot()).getChildren().addAll(resultsGrid);

				stage.setScene(scene);
				stage.show();
	
				exitButton.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent e) {
						stage.close();
					}
				});

				submitBtn.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent e) {	
						LibraryBook libBook = (LibraryBook) availableBooks.getSelectionModel().getSelectedItem();
						if(libBook == null) {
							Alert alert1 = new Alert(AlertType.INFORMATION);
							alert1.setTitle("Borrow Book Failed");
							alert1.setHeaderText(null);
							alert1.setContentText("Error: Please select a row in \n" +
							" the table to borrow the book. \n");
							alert1.showAndWait();
						}
						else {
							bookIdField.setText(libBook.getBookId());
						}
						if (isStudent.isSelected()) {
							if (libraryUtils.isValidStudent(borrowerIdField.getText(), students))
								msg.setText(libraryUtils.bookBorrow(bookIdField, borrowerIdField, "Student", books,
										students, teachers));
							else
								msg.setText("Not a Valid Student ID");
						}

						else if (isTeacher.isSelected()) {
							if (libraryUtils.isValidTeacher(borrowerIdField.getText(), teachers))
								msg.setText(libraryUtils.bookBorrow(bookIdField, borrowerIdField, "Teacher", books,
										students, teachers));
							else {
								msg.setText("Not a Valid Teacher ID");
							}
						}
						borrowerIdField.clear();
						msg.setVisible(true);

					}
				});

			}
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void overdueBooksButtonAction(Button overDueBooksBtn, Stage primaryStage) {
		overDueBooksBtn.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("unchecked")
			public void handle(ActionEvent e) {
				Scene scene = new Scene(new Group());
				scene.getStylesheets().add(css);
				Stage stage = new Stage();
				stage.setTitle("Overdue Book Report");
				stage.setWidth(700);
				stage.setHeight(700);
				stage.setScene(scene);

				Text to = new Text("To");
				to.getStyleClass().add("alert");
				Text startDate = new Text();
				LocalDate odStart = LocalDate.now().minusDays(7);
				startDate.setText(LocalDate.now().minusDays(7).format(dtf));
				startDate.getStyleClass().add("alert");
				Text endDate = new Text();
				LocalDate odEnd = LocalDate.now();
				endDate.setText(LocalDate.now().format(dtf));
				endDate.getStyleClass().add("alert");

				Text overdueBookReportLabel = new Text("Overdue Book Report");
				overdueBookReportLabel.getStyleClass().addAll("heading");
				Text label = new Text("");

				TableView<OverdueBook> table = new TableView<OverdueBook>();
				table.setPrefSize(550, 300);
				TableColumn<OverdueBook, String> bookIdCol = new TableColumn<OverdueBook, String>("Book ID");
				bookIdCol.setPrefWidth(75);
				bookIdCol.setCellValueFactory(new PropertyValueFactory<OverdueBook, String>("bookId"));
				TableColumn<OverdueBook, String> bookNameCol = new TableColumn<OverdueBook, String>("Title");
				bookNameCol.setPrefWidth(250);
				bookNameCol.setCellValueFactory(new PropertyValueFactory<OverdueBook, String>("bookName"));
				TableColumn<OverdueBook, String> borrowerIdCol = new TableColumn<OverdueBook, String>("Borrower Id");
				borrowerIdCol.setPrefWidth(75);
				borrowerIdCol.setCellValueFactory(new PropertyValueFactory<OverdueBook, String>("borrowerId"));
				TableColumn<OverdueBook, String> returnDateCol = new TableColumn<OverdueBook, String>("Return Date");
				returnDateCol.setPrefWidth(100);
				returnDateCol.setCellValueFactory(new PropertyValueFactory<OverdueBook, String>("returnDate"));
				TableColumn<OverdueBook, String> fineValuesCol = new TableColumn<OverdueBook, String>("Fine");
				fineValuesCol.setPrefWidth(50);
				fineValuesCol.setCellValueFactory(new PropertyValueFactory<OverdueBook, String>("fine"));

				List<OverdueBook> bookList = new ArrayList<>();
				GridPane finesGrid = new GridPane();
				finesGrid.getStyleClass().addAll("pane");
				finesGrid.setPrefWidth(700);
				finesGrid.setPrefHeight(700);
				finesGrid.setAlignment(Pos.CENTER);

				String fine = "$0.00";
				OverdueBook ob;
				for (LibraryBook book : books) {
					fine = "$0.00";
					if (book.isCheckedOut && book.getCheckedOutBy().equals("Student")
							&& (book.getReturnDate().getDayOfYear() > odStart.getDayOfYear())
							&& (book.getReturnDate().getDayOfYear() < odEnd.getDayOfYear())) {

						fine = LibraryUtils.calcFine(book.getCheckedOutDate(), book.getReturnDate(),
								Student.finePerDay);

					} else if (book.isCheckedOut && book.getCheckedOutBy().equals("Teacher")
							&& (book.getReturnDate().getDayOfYear() > odStart.getDayOfYear())
							&& (book.getReturnDate().getDayOfYear() < odEnd.getDayOfYear())) {

						fine = LibraryUtils.calcFine(book.getCheckedOutDate(), book.getReturnDate(),
								Teacher.finePerDay);

					}
					if (!(book.getReturnDate() == null) && !fine.equals("$0.00")) {
						String formattedDate = book.getReturnDate().format(dtf);
						ob = new OverdueBook(new SimpleStringProperty(book.getBookId()),
								new SimpleStringProperty(book.getBookName()),
								new SimpleStringProperty(book.getCheckedOutId()),
								new SimpleStringProperty(formattedDate), new SimpleStringProperty(fine));
						bookList.add(ob);
					}
				}
				table.setItems(FXCollections.observableArrayList(bookList));
				table.getColumns().addAll(bookIdCol, bookNameCol, borrowerIdCol, returnDateCol, fineValuesCol);

				Text dummy = new Text("");
				Text dummy1 = new Text("");

				HBox hbox1 = new HBox();
				hbox1.setSpacing(10);
				hbox1.getChildren().addAll(startDate, to, endDate);

				finesGrid.add(overdueBookReportLabel, 0, 0);
				finesGrid.add(dummy, 0, 1);
				finesGrid.add(hbox1, 0, 2);
				finesGrid.add(label, 0, 3);
				finesGrid.add(table, 0, 4);
				Button printButton = new Button("Print");
				printButton.getStyleClass().add("buttonSmall");
				Button cancelButton = new Button("Cancel");
				cancelButton.getStyleClass().add("buttonSmall");

				HBox hbox2 = new HBox();
				hbox2.setSpacing(10);
				hbox2.getChildren().addAll(printButton, cancelButton);

				finesGrid.add(dummy1, 0, 5);
				finesGrid.add(hbox2, 0, 6);
				((Group) scene.getRoot()).getChildren().addAll(finesGrid);
				stage.setScene(scene);
				stage.show();

				cancelButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						stage.close();
					}
				});
				printButton.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						PrinterJob printerJob = PrinterJob.createPrinterJob();
						if (printerJob.showPrintDialog(primaryStage.getOwner()) && printerJob.printPage(table))
							printerJob.endJob();

					}

				});
			}
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void checkedoutBooksButtonAction(Button checkedOutBooksBtn, Stage primaryStage) {
		checkedOutBooksBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// find books that are due as of date.now or later
				Scene scene = new Scene(new Group());
				scene.getStylesheets().add(css);
				Stage stage = new Stage();
				stage.setTitle("Borrowed Books Report");
				stage.setWidth(700);
				stage.setHeight(700);
				stage.setScene(scene);

				Text to = new Text("To");
				to.getStyleClass().add("alert");
				Text startDate = new Text();
				startDate.setText(LocalDate.now().minusDays(7).format(dtf));
				startDate.getStyleClass().add("alert");
				Text endDate = new Text();
				endDate.setText(LocalDate.now().format(dtf));
				endDate.getStyleClass().add("alert");

				Text BorrowedBooksReportlabel = new Text("Borrowed Books Report");
				Text label = new Text("");
				BorrowedBooksReportlabel.getStyleClass().addAll("heading");

				TableView<BorrowedBook> table = new TableView<BorrowedBook>();
				table.setPrefSize(600, 300);

				TableColumn<BorrowedBook, String> bookIdCol = new TableColumn<BorrowedBook, String>("Book ID");
				bookIdCol.setPrefWidth(50);
				bookIdCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("bookId"));

				TableColumn<BorrowedBook, String> bookNameCol = new TableColumn<BorrowedBook, String>("Title");
				bookNameCol.setPrefWidth(250);
				bookNameCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("bookName"));

				TableColumn<BorrowedBook, String> borrowerIdCol = new TableColumn<BorrowedBook, String>("Borrower ID");
				borrowerIdCol.setPrefWidth(50);
				borrowerIdCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("borrowerId"));

				TableColumn<BorrowedBook, String> borrowerNameCol = new TableColumn<BorrowedBook, String>(
						"Borrower Name");
				borrowerNameCol.setPrefWidth(100);
				borrowerNameCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("borrowerName"));

				TableColumn<BorrowedBook, String> returnDateCol = new TableColumn<BorrowedBook, String>(
						"Checkout Date");
				returnDateCol.setPrefWidth(75);
				returnDateCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("borrowDate"));

				TableColumn<BorrowedBook, String> dueInCol = new TableColumn<BorrowedBook, String>("Due in");
				dueInCol.setPrefWidth(75);
				dueInCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("leadingDays"));

				List<BorrowedBook> bookList = new ArrayList<>();
				GridPane dueDateGrid = new GridPane();
				dueDateGrid.getStyleClass().addAll("pane");
				dueDateGrid.setPrefWidth(700);
				dueDateGrid.setPrefHeight(700);
				dueDateGrid.setAlignment(Pos.CENTER);

				BorrowedBook bb;
				int leadingDays;
				SimpleStringProperty ld;
				for (int i = 0; i < books.size(); i++) {
					if (!(books.get(i).getReturnDate() == null)) {

						leadingDays = (books.get(i).getReturnDate().getDayOfYear() - LocalDate.now().getDayOfYear());
						System.out.println("Leading days: " + leadingDays);
						if (leadingDays == 0)
							ld = new SimpleStringProperty("Due Today");
						else if (leadingDays == -1)
							ld = new SimpleStringProperty(Math.abs(leadingDays) + " Day late");
						else if (leadingDays < -1)
							ld = new SimpleStringProperty(Math.abs(leadingDays) + " Days late");
						else if (leadingDays == 1)
							ld = new SimpleStringProperty(Math.abs(leadingDays) + " Day");
						else
							ld = new SimpleStringProperty(Math.abs(leadingDays) + " Days");
						System.out.println("Leading days: " + ld);
						if (books.get(i).isCheckedOut) {
							bb = new BorrowedBook(new SimpleStringProperty(books.get(i).getBookId()),
									new SimpleStringProperty(books.get(i).getBookName()),
									new SimpleStringProperty(books.get(i).getCheckedOutId()),
									new SimpleStringProperty(books.get(i).getBorrowerName()), ld,
									new SimpleStringProperty(books.get(i).getCheckedOutDate().format(dtf)));
							bookList.add(bb);
						}

					}
				}
				table.setItems(FXCollections.observableArrayList(bookList));
				table.getColumns().addAll(bookIdCol, bookNameCol, borrowerIdCol, borrowerNameCol, returnDateCol,
						dueInCol);

				Text dummy = new Text("");
				Text dummy1 = new Text("");

				HBox hbox1 = new HBox();
				hbox1.setSpacing(10);
				hbox1.getChildren().addAll(startDate, to, endDate);

				dueDateGrid.add(BorrowedBooksReportlabel, 0, 0);
				dueDateGrid.add(dummy, 0, 1);
				dueDateGrid.add(hbox1, 0, 2);
				dueDateGrid.add(label, 0, 3);
				dueDateGrid.add(table, 0, 4);
				Button printButton = new Button("Print");
				printButton.getStyleClass().add("buttonSmall");
				Button cancelButton = new Button("Cancel");
				cancelButton.getStyleClass().add("buttonSmall");

				HBox hbox2 = new HBox();
				hbox2.setSpacing(10);
				hbox2.getChildren().addAll(printButton, cancelButton);

				dueDateGrid.add(dummy1, 0, 5);
				dueDateGrid.add(hbox2, 0, 6);
				((Group) scene.getRoot()).getChildren().addAll(dueDateGrid);
				stage.setScene(scene);
				stage.show();

				cancelButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						stage.close();
					}
				});
				printButton.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						PrinterJob printerJob = PrinterJob.createPrinterJob();
						if (printerJob.showPrintDialog(primaryStage.getOwner()) && printerJob.printPage(table))
							printerJob.endJob();

					}

				});

			}
		});
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void stop() {

		libraryFileReader.writeBookData(bookfileName);
		libraryFileReader.writeStudentData(studentsfileName);
		libraryFileReader.writeTeacherData(teachersfileName);

		System.out.println("Stage is closing");
	}
}
