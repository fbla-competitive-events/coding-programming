
//CLICK THE GREEN RUN BUTTON NEAR THE TOP LEFT TO START THE PROGRAM




import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//this class is the User Interface which has the buttons and text, allowing the user to start the program.
//This is the class you have to run in order to use the program.


public class Window extends Application{
	TextField studentX;
	TextField teacherX;
	boolean wantsTeachers = false;
    boolean wantsStudents = false;
	int lastBookRow;
	int lastTeachRow;
	int lastStuRow;
	Text nameAnswer = new Text("");
    Button viewBooks = new Button();
    
    
	public static Database westhill = new Database(); //database for the program
	
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException{
		File stu = new File("Storage_Files\\students.txt");
		File tea = new File("Storage_Files\\teachers.txt");
		File boo = new File("Storage_Files\\books.txt");
		if (!stu.isFile() || !tea.isFile() || !boo.isFile()){
			westhill.uploadToFile();
		}
		westhill.readFromFile();
		
		launch(args);
	}
     
	//starts the program
	
	/**
	 * This is the main function which starts the UI
	 * Contains all graphical design and connections to the database class
	 */
	@Override
	public void start(Stage myStage) {
		
        myStage.setTitle("Westhill High Library Program");
        
        BorderPane border = new BorderPane();

	    HBox hbox = new HBox();
	    hbox.setPadding(new Insets(15, 12, 15, 12));
	    hbox.setSpacing(44);
	    hbox.setStyle("-fx-background-color: #32cd32;");

	    Button allBooks = new Button("All Books");
	    allBooks.setPrefSize(75, 30);
	    allBooks.setStyle("-fx-border-color: #ffffff; -fx-border-width: 3px;");
	    Button allStudents = new Button("All Students/Check-Out/Check-In");
	    allStudents.setPrefSize(200, 30);
	    allStudents.setStyle("-fx-border-color: #ffffff; -fx-border-width: 3px;");
	    Button allTeachers = new Button("All Teachers/Check-Out/Check-In");
	    allTeachers.setPrefSize(200, 30);
	    allTeachers.setStyle("-fx-border-color: #ffffff; -fx-border-width: 3px;");
	    
	    
	    Button issuedBooks = new Button("Issued Books Report");
	    issuedBooks.setPrefSize(130, 30);
	    issuedBooks.setStyle("-fx-border-color: #ffffff; -fx-border-width: 3px;");
	    Button fines = new Button("Fines Report");
	    fines.setPrefSize(90, 30);
	    fines.setStyle("-fx-border-color: #ffffff; -fx-border-width: 3px;");
	    hbox.getChildren().addAll(allBooks, allTeachers, allStudents, issuedBooks, fines);
	    
	    //this determines what happens when the all Teachers button is clicked
	    
	    allTeachers.setOnAction(e -> {
	    	GridPane teacherList = new GridPane();
	    	teacherList.setHgap(10);
	    	teacherList.setVgap(10);
	    	teacherList.setPadding(new Insets(0, 10, 0, 10));
	    	Text name = new Text("Names");
	    	name.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    	teacherList.add(name, 1, 1);
	    	lastTeachRow = 3;
	    	for (int i = 0; i < westhill.getAllTeachers().size(); ++i){
	    		Teacher j = westhill.getAllTeachers().get(i);
	    		Text nameValue = new Text(j.getName().toUpperCase());
	    		nameValue.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
	    		teacherList.add(nameValue, 1, ((i+1)*2) + 1);
	    		if (i == westhill.getAllTeachers().size()-1){
	    			lastTeachRow = ((i+1)*2)+1;
	    		}
	    	}
	    	Text info = new Text("Check Out:");
	    	Text enterName = new Text("Enter Teacher Name");
	    	Text enterBookName = new Text("Enter Book Name");
	    	TextField tname = new TextField();
	    	TextField bname = new TextField();
	    	Button checkOutBook = new Button("Check Out"); 
	    	
	    	Text checkIn = new Text("Check In Book");
	    	Text nameShow = new Text("Enter Book Name");
	    	Text teachNameShow = new Text("Enter Teacher Name");
	    	TextField teachNameValue = new TextField();
	    	TextField nameValue = new TextField();
	    	Button namePress = new Button("Check In");
	    	
	    	checkIn.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    	info.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    	teacherList.add(info, 1, (lastTeachRow + 4));
	    	teacherList.add(enterName, 1, (lastTeachRow + 6));
	    	teacherList.add(enterBookName, 1, lastTeachRow + 8);
	    	teacherList.add(tname, 4, (lastTeachRow + 6));
	    	teacherList.add(bname, 4, (lastTeachRow + 8));
	    	teacherList.add(checkOutBook, 4, (lastTeachRow + 10));
	    	teacherList.add(checkIn, 1, lastTeachRow+14);
	    	teacherList.add(teachNameShow, 1, lastTeachRow+16);
	    	teacherList.add(teachNameValue, 4, lastTeachRow+16);
	    	teacherList.add(nameShow, 1, lastTeachRow+18);
	    	teacherList.add(nameValue, 4, lastTeachRow+18);
	    	teacherList.add(namePress, 4, lastTeachRow+20);
	    	
	    	namePress.setOnAction( e34 -> {
	    		Teacher toCheckOut = westhill.getTeacher(teachNameValue.getText());
	    		Book current = null;
	    		for (Book b : westhill.getAllBooks()){
	    			if (b.getName().toLowerCase().equals(nameValue.getText().trim().toLowerCase())){
	    				current = b;
	    				break;
	    			}
	    		}
	    		if (current == null || toCheckOut == null){
	    			teacherList.add(new Text("Not Found"), 5, lastTeachRow+20);
	    			nameValue.clear();
	    			return;
	    		}
	    		else{
	    			toCheckOut.getBooksCheckedOut().remove(current);
	    			current.resetDateCheckedOut();
	    			current.resetDateDue();
	    			current.resetUserCheckedOut();
	    			teacherList.add(new Text("Done!"), 5, lastTeachRow+20);
	    			nameValue.clear();
	    			teachNameValue.clear();
	    		}
	            	   
	    	});
	    	
	       // this is the check out feature
	    	
	    	checkOutBook.setOnAction(e1 -> {
	    		Text complete;
	    		Book toBeChecked = westhill.findBookByName(bname.getText());
	    		Teacher whoWillCheck = null;
	    		for (Teacher t : westhill.getAllTeachers()){
	    			if (t.getName().equals(tname.getText().toLowerCase())){
	    				whoWillCheck = t;
	    				break;
	    			}
	    		}
	    		if (whoWillCheck == null){
	    			complete = new Text("Teacher Not Found");
	    			teacherList.add(complete, 5, (lastTeachRow + 10));
	    			return;
	    		}
	    		if (toBeChecked == null){
	    			complete = new Text("Book Not Available");
	    			teacherList.add(complete, 5, (lastTeachRow + 10));
	    			return;
	    		}
	    		else if(toBeChecked.getUserCheckedOut() != null){
	    			complete = new Text("Already Checked Out");
	    		}
	    		else if(whoWillCheck.getBooksCheckedOut().size() == User.numberOfBooksAllowedForTeachers){
	    			complete = new Text("No More Allowed");
	    		}
	    		else{
	    			complete = new Text("Done!");
	    			whoWillCheck.checkOutBook(toBeChecked);
	    		}
	    		teacherList.add(complete, 5, (lastTeachRow + 10));
	    		
	    		
	    	});
	    	
	    	Scene teachers = new Scene(teacherList, 500, 500);
	    	Stage teacherPage = new Stage();
	    	teacherPage.setScene(teachers); 
	    	teacherPage.setTitle("All Teachers");
	    	teacherPage.show();
	    });
	    
	    //this is what happens when you click on all students
	    
	    allStudents.setOnAction(e -> {
	    	GridPane studentList = new GridPane();
	    	studentList.setHgap(10);
	    	studentList.setVgap(10);
	    	studentList.setPadding(new Insets(0, 10, 0, 10));
	    	Text name = new Text("Names");
	    	name.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    	studentList.add(name, 1, 1);
	    	lastStuRow = 3;
	    	for (int i = 0; i < westhill.getAllStudents().size(); ++i){
	    		Student j = westhill.getAllStudents().get(i);
	    		Text nameValue = new Text(j.getName().toUpperCase());
	    		nameValue.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
	    		studentList.add(nameValue, 1, ((i+1)*2) + 1);
	    		if (i == westhill.getAllStudents().size()-1){
	    			lastStuRow = ((i+1)*2)+1;
	    		}
	    	}
	    	Text info = new Text("Check Out:");
	    	Text enterName = new Text("Enter Student Name");
	    	Text enterBookName = new Text("Enter Book Name");
	    	TextField tname = new TextField();
	    	TextField bname = new TextField();
	    	Button checkOutBook = new Button("Check Out");
	    	
	    	Text checkIn = new Text("Check In Book");
	    	Text stuNameShow = new Text("Enter Student Name");
	    	Text nameShow = new Text("Enter Book Name");
	    	TextField stuNameValue = new TextField();
	    	TextField nameValue = new TextField();
	    	Button namePress = new Button("Check In");
	    	checkIn.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    	
	    	
	    	info.setFont(Font.font("Arial", FontWeight.BOLD, 24));
	    	studentList.add(info, 1, (lastStuRow + 4));
	    	studentList.add(enterName, 1, (lastStuRow + 6));
	    	studentList.add(enterBookName, 1, lastStuRow + 8);
	    	studentList.add(tname, 4, (lastStuRow + 6));
	    	studentList.add(bname, 4, (lastStuRow + 8));
	    	studentList.add(checkOutBook, 4, (lastStuRow + 10));
	    	studentList.add(checkIn, 1, lastStuRow+14);
	    	studentList.add(stuNameShow, 1, lastStuRow+16);
	    	studentList.add(stuNameValue, 4, lastStuRow+16);
	    	studentList.add(nameShow, 1, lastStuRow+18);
	    	studentList.add(nameValue, 4, lastStuRow+18);
	    	studentList.add(namePress, 4, lastStuRow+20);
	    	
	    	namePress.setOnAction( e34 -> {
	    		Book current = null;
	    		Student toCheckOut = westhill.getStudent(stuNameValue.getText());
	    		for (Book b : westhill.getAllBooks()){
	    			if (b.getName().toLowerCase().equals(nameValue.getText().trim().toLowerCase())){
	    				current = b;
	    				break;
	    			}
	    		}
	    		if (current == null || toCheckOut == null){
	    			studentList.add(new Text("Not Found"), 5, lastStuRow+20);
	    			nameValue.clear();
	    			return;
	    		}
	    		else{
	    			toCheckOut.getBooksCheckedOut().remove(current);
	    			current.resetDateCheckedOut();
	    			current.resetDateDue();
	    			current.resetUserCheckedOut();
	    			studentList.add(new Text("Done!"), 5, lastStuRow+20);
	    			nameValue.clear();
	    			stuNameValue.clear();
	    		}
	            	   
	    	});
	    	
	    	
	    	checkOutBook.setOnAction(e1 -> {
	    		Text complete;
	    		Book toBeChecked = westhill.findBookByName(bname.getText());
	    		Student whoWillCheck = westhill.getStudent(tname.getText());
	    		if (whoWillCheck == null){
	    			complete = new Text("Student Not Found");
	    			studentList.add(complete, 5, (lastStuRow + 10));
	    			return;
	    		}
	    		if (toBeChecked == null){
	    			complete = new Text("Book Not Available");
	    			studentList.add(complete, 5, (lastStuRow + 10));
	    			return;
	    		}
	    		else if(whoWillCheck != null && whoWillCheck.getBooksCheckedOut().size() == User.numberOfBooksAllowedForStudents){
	    			complete = new Text("No More Allowed");
	    		}
	    		else{
	    			complete = new Text("Done!");
	    			whoWillCheck.checkOutBook(toBeChecked);
	    		}
	    		studentList.add(complete, 5, (lastStuRow + 10));
	    		
	    		
	    	});
	    	
	    	Scene students = new Scene(studentList, 500, 500);
	    	Stage studentPage = new Stage();
	    	studentPage.setScene(students); 
	    	studentPage.setTitle("All Students");
	    	studentPage.show();
	    });
	    
	    
	    //this is what happens when you click all books
	    
	    allBooks.setOnAction(e -> {
	    	GridPane bookList = new GridPane();
	    	bookList.setHgap(10);
	        bookList.setVgap(10);
	        bookList.setPadding(new Insets(0, 10, 0, 10));
	        Text name = new Text("Book Name");
	        Text author = new Text("Author Name");
	        Text id = new Text("Book ID");
	        Text status = new Text("Status of Book");
	        Text user = new Text("User");
	        
	        name.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	        author.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	        id.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	        status.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	        user.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	        
	        bookList.add(name, 1, 7);
	        bookList.add(author, 4, 7);
	        bookList.add(id, 7, 7);
	        bookList.add(status, 10, 7);
	        bookList.add(user, 13, 7);
	        for (int i = 0; i < westhill.getAllBooks().size(); ++i){
	        	Book currentBook = westhill.getAllBooks().get(i);
	        	Text bookName = new Text(currentBook.getName());
	        	Text authorName = new Text(currentBook.getAuthorName());
	        	Text bookID = new Text("" + currentBook.getID());
	        	Text bookStatus = new Text("Checked Out");
	        	Text userName;
	        	if (currentBook.getUserCheckedOut() == null){
	        		bookStatus = new Text("Not Checked Out");
	        		userName = new Text("N/A");
	        	}
	        	else{
	        		userName = new Text(currentBook.getUserCheckedOut().getName().toUpperCase());
	        	}
	        	bookName.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
	        	authorName.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
	        	bookID.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
	        	bookStatus.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
	        	userName.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
	        	
	        	
	        	bookList.add(bookName, 1, (i+1)*2+7);
	        	bookList.add(authorName, 4, (i+1)*2+7);
	        	bookList.add(bookID, 7, (i+1)*2+7);
	        	bookList.add(bookStatus, 10, (i+1)*2+7);
	        	bookList.add(userName, 13, (i+1)*2+7);
	        	if (i == westhill.getAllBooks().size()-1){
	        		lastBookRow = i;
	        	}
	        }
	        Text search = new Text("Search for Book ID");
	        search.setFont(Font.font("Arial", FontWeight.BOLD, 18));
	        bookList.add(search, 1, 1, 10, 1);
	        Text enterHere = new Text("Enter Name Here");
	        enterHere.setFont(Font.font("Arial", FontWeight.MEDIUM, 12));
	        bookList.add(enterHere, 1, 3);
	        TextField bName = new TextField();
	        bName.setPrefWidth(100);
	        bookList.add(bName, 2, 3, 3, 1);
	        Button enterSearch = new Button("Search");
	        bookList.add(enterSearch, 6, 3, 2, 1);
	        Text result = new Text("Result ID");
	        result.setFont(Font.font("Arial", FontWeight.MEDIUM, 12));
	        bookList.add(result, 8, 3);
	        TextField showIt = new TextField();
	        showIt.setPrefWidth(100);
	        bookList.add(showIt, 10, 3, 3, 1);
	        
	        enterSearch.setOnAction(e6 -> {
	        	int returnAnswer = 0;
	        	String bookSearchName = bName.getText().toLowerCase();
	        	for (Book b : westhill.getAllBooks()){
	        		if (b.getName().toLowerCase().equals(bookSearchName)){
	        			returnAnswer = b.getID();
	        			break;
	        		}
	        	}
	        	if (returnAnswer == 0){
	        		showIt.setText("Book Not Found");
	        		return;
	        	}
	        	showIt.setText(String.valueOf(returnAnswer));
	        });
	        
	    	Scene booksPage = new Scene(bookList, 800, 500);
	    	Stage bookPage = new Stage();
	    	bookPage.setScene(booksPage);
	    	bookPage.setTitle("All Books");
	    	bookPage.show();
	    });
	    
	    //this is the button which produces the issued books report
	    
	    issuedBooks.setOnAction(e -> {
	    	GridPane pageList = new GridPane();
	    	pageList.setHgap(10);
	        pageList.setVgap(10);
	        pageList.setPadding(new Insets(0, 10, 0, 10));
	        
	        
	        Text info = new Text("Include:");
	        info.setFont(Font.font("Arial", FontWeight.BOLD, 18));
	        pageList.add(info, 1, 2);
	        Button teachers = new Button("Teachers");
	        pageList.add(teachers, 1, 6);
	        teacherX = new TextField();
	        teacherX.setMinWidth(25);
	        teacherX.setMaxWidth(25);
	        pageList.add(teacherX, 2, 6);
	        Button students = new Button("Students");
	        pageList.add(students, 4, 6, 1, 1);
	        studentX = new TextField();
	        studentX.setMinWidth(25);
	        studentX.setMaxWidth(25);
	        pageList.add(studentX, 5, 6, 1, 1); 
	        Button generate = new Button("Generate Report");
	        pageList.add(generate, 5, 10, 1, 1);
	        
	        teachers.setOnAction(e10 -> {
	        	wantsTeachers = !wantsTeachers;
	        	if (wantsTeachers){
	        		teacherX.setText("X");
	        	}
	        	else {
	        		teacherX.clear();
	        	}
	        });
	        
	        students.setOnAction(e11 -> {
	        	wantsStudents = !wantsStudents;
	        	if (wantsStudents){
	        		studentX.setText("X");
	        	}
	        	else {
	        		studentX.clear();
	        	}
	        });
	        
	        generate.setOnAction(e7 -> {
	        	try {
					westhill.generateIssuedBooks(wantsStudents, wantsTeachers);
				} catch (FileNotFoundException e1) {	
					e1.printStackTrace();
				}
	        	pageList.add(new Text("Done!"), 6, 10);
	        	studentX.clear();
	        	teacherX.clear();
	        	try {
					Runtime.getRuntime().exec("explorer.exe /open," + "Reports\\issued_books_report.txt");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        });
	        
	        Scene westhillPage = new Scene(pageList, 400,200);
	    	Stage westhilledPage = new Stage();
	    	westhilledPage.setScene(westhillPage);
	    	westhilledPage.setTitle("Issued Books Report");
	    	westhilledPage.show();
        });
	    
	    //this is the button which produced the fines report
	    
	    fines.setOnAction(e -> {
	    	GridPane pageList = new GridPane();
	    	pageList.setHgap(10);
	        pageList.setVgap(10);
	        pageList.setPadding(new Insets(0, 10, 0, 10));
	        
	        
	        Text info = new Text("Include:");
	        info.setFont(Font.font("Arial", FontWeight.BOLD, 18));
	        pageList.add(info, 1, 2);
	        Button teachers = new Button("Teachers");
	        pageList.add(teachers, 1, 6);
	        teacherX = new TextField();
	        teacherX.setMinWidth(25);
	        teacherX.setMaxWidth(25);
	        pageList.add(teacherX, 2, 6);
	        Button students = new Button("Students");
	        pageList.add(students, 4, 6, 1, 1);
	        studentX = new TextField();
	        studentX.setMinWidth(25);
	        studentX.setMaxWidth(25);
	        pageList.add(studentX, 5, 6, 1, 1); 
	        Button generate = new Button("Generate Report");
	        pageList.add(generate, 5, 10, 1, 1);
	        
	        teachers.setOnAction(e10 -> {
	        	wantsTeachers = !wantsTeachers;
	        	if (wantsTeachers){
	        		teacherX.setText("X");
	        	}
	        	else {
	        		teacherX.clear();
	        	}
	        });
	        
	        students.setOnAction(e11 -> {
	        	wantsStudents = !wantsStudents;
	        	if (wantsStudents){
	        		studentX.setText("X");
	        	}
	        	else {
	        		studentX.clear();
	        	}
	        });
	        
	        generate.setOnAction(e7 -> {
	        	try {
					westhill.generateFinesReport(wantsStudents, wantsTeachers);
				} catch (FileNotFoundException e1) {	
					e1.printStackTrace();
				}
	        	pageList.add(new Text("Done!"), 6, 10);
	        	studentX.clear();
	        	teacherX.clear();
	        	try {
					Runtime.getRuntime().exec("explorer.exe /open," + "Reports\\fines_report.txt");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        });
	        
	        Scene westhillPage = new Scene(pageList, 400,200);
	    	Stage westhilledPage = new Stage();
	    	westhilledPage.setScene(westhillPage);
	    	westhilledPage.setTitle("Fines Report");
	    	westhilledPage.show();
        });
	
	

	    GridPane grid = new GridPane();
	    grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(0, 10, 0, 10));

	    //next few sections display the home page
	    
	    Text category = new Text("Add Book");
	    category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	    grid.add(category, 4, 1); 

	    Text students = new Text("Add Student");
	    students.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	    grid.add(students, 11, 1); 
	    
	    TextField studentName = new TextField();
	    grid.add(studentName, 11, 2);
	    
	    Text teachers = new Text("Add Teacher");
	    teachers.setFont(Font.font("Arial", FontWeight.BOLD, 20));
	    grid.add(teachers, 18, 1); 
	    
	    TextField teachersName = new TextField();
	    teachersName.setPrefWidth(220);
	    grid.add(teachersName, 18, 2);
	    

	    TextField bookName = new TextField();	   
	    grid.add(bookName, 4, 2);

	
	    Text chartSubtitle = new Text("Name");
	    chartSubtitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
	    grid.add(chartSubtitle, 1, 2);

	
	    TextField author = new TextField();
	    grid.add(author, 4, 4); 
	    Text a = new Text("Author Name");
	    a.setFont(Font.font("Arial", FontWeight.BOLD, 14));
	    grid.add(a, 1, 4);
	    
	    Button submitBook = new Button("Submit Info");
	    grid.add(submitBook, 4, 6);
	    
	    Button submitStudent = new Button("Submit Info");
	    grid.add(submitStudent, 11, 4);

	    Button submitTeacher = new Button("Submit Info");
	    grid.add(submitTeacher, 18, 4);
	    

	    Button exitNoSave = new Button("Exit Without Saving");
	    exitNoSave.setStyle("-fx-background-color: #ffb84d; " + "-fx-font-size: 16; " + "-fx-border-color: #000000;" + "-fx-border-width: 1.5px;");
	    exitNoSave.setPrefSize(180, 20);
	    grid.add(exitNoSave, 4, 10);
	    
	    Button saveAndExit = new Button("Save & Exit");
	    saveAndExit.setStyle("-fx-background-color: #ffb84d; " + "-fx-font-size: 16; "+"-fx-border-color: #000000;" + "-fx-border-width: 1.5px;");
	    saveAndExit.setPrefSize(180, 20);
	    grid.add(saveAndExit, 11, 10);
	    
	    Button helpSection = new Button("Help");
	    helpSection.setStyle("-fx-background-color: #00CC00; " + "-fx-text-fill: #FFFFFF;" + "-fx-border-color: #000000;" + "-fx-border-width: 1.5px;");
	    helpSection.setFont(Font.font("Arial", FontWeight.BOLD, 16));
	    helpSection.setPrefSize(90, 15);
	    grid.add(helpSection, 18, 10);
	    
	    helpSection.setOnAction(e -> {
	    	int current = 1;
	    	showHelpPic(current);
	    });
	    
	    
	    
	    exitNoSave.setOnAction(e -> {
            System.exit(0);
        });
	    
	    saveAndExit.setOnAction(e -> {
	    	try {
				westhill.uploadToFile();
			} 
	    	catch (ClassCastException | IOException e1) {
				e1.printStackTrace();
			}
            System.exit(0);
        });

	    submitBook.setOnAction(e -> {
	    	if (bookName.getText().equals("") || author.getText().equals("")){
	    		grid.add(new Text("Incomplete!"), 4, 8);
	    		bookName.clear();
	    		author.clear();
	    		return;
	    	}
            westhill.addBook(bookName.getText(), author.getText());
            grid.add(new Text("Done!"), 4, 8);
            bookName.clear();
            author.clear();
        });
	    
	    submitStudent.setOnAction(e -> {
	    	if (studentName.getText().equals("")){
	    		grid.add(new Text("Incomplete!"), 11, 6);
	    		return;
	    	}
            westhill.addStudent(studentName.getText());
            grid.add(new Text("Done!"), 11, 6);
            studentName.clear();
        });
	    
	    submitTeacher.setOnAction(e -> {
	    	if (teachersName.getText().equals("")){
	    		grid.add(new Text("Incomplete!"), 18, 6);
	    		return;
	    	}
            westhill.addTeacher(teachersName.getText());
            grid.add(new Text("Done!"), 18, 6);
            teachersName.clear();
        });
	    
	    border.setTop(hbox);
        border.setCenter(grid);
        
        //displays the window
        
        Scene myScene = new Scene(border, 900, 330);
		myStage.setScene(myScene);
        myStage.show();
    
        
	}
	
	
	//this method helps display the help window
	public void showHelpPic(int current){
		GridPane pageList = new GridPane();
    	pageList.setHgap(10);
        pageList.setVgap(10);
        pageList.setPadding(new Insets(0, 10, 0, 10));
        File file = new File("Image_Files\\Pic" + current + ".jpg");
        Image i = new Image(file.toURI().toString());
        ImageView pic = new ImageView();
        pic.setImage(i);
        pageList.add(pic, 0, 0, 50, 28);
        Button prev = new Button("Previous");
        prev.setStyle("-fx-font-size: 14; " + "-fx-border-color: #009900; -fx-border-width: 2px;");
        prev.setPrefSize(90, 10);
        Button next = new Button("Next");
        next.setStyle("-fx-font-size: 14; " + "-fx-border-color: #009900; -fx-border-width: 2px;");
        next.setPrefSize(90, 10);
        pageList.add(prev, 17, 28);
        pageList.add(next, 34, 28);
        Scene help1 = new Scene(pageList, 715, 330);
    	Stage helpPage1 = new Stage();
    	helpPage1.setScene(help1);
    	helpPage1.setTitle("Help");
    	helpPage1.show();
    	prev.setOnAction(e1 -> {
    		if (current == 1){
    			return;
    		}
    		helpPage1.close();
    		showHelpPic(current - 1);
    	});
    	next.setOnAction(e2 -> {
    		if (current == 4){
    			return;
    		}
    		helpPage1.close();
    		showHelpPic(current + 1);
    	});
    }
	
}
