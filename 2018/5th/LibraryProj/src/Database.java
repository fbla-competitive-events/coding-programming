import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.io.*;

//this database class stores data and has all the methods that store in a file, read from file, save the data, etc
//data is stored in the project files

public class Database {
	private ArrayList<Student> allStudents; 	//stores all students
	private ArrayList<Teacher> allTeachers; 	//stores all teachers
	private ArrayList<Book> allBooks;   		//stores all books
	
	/**
	 * Constructor creating a new database
	 */
	public Database(){
		this.allStudents = new ArrayList<Student>();
		this.allTeachers = new ArrayList<Teacher>();
		this.allBooks = new ArrayList<Book>();
	}
	
	/**
	 * 
	 * @return all books in the database
	 */
	public ArrayList<Book> getAllBooks(){		
		return this.allBooks;
	}
	
	/**
	 * 
	 * @return all students in the system
	 */
	public ArrayList<Student> getAllStudents(){ 	
		return this.allStudents;
	}
	
	/**
	 * 
	 * @return all teachers in the system
	 */
	public ArrayList<Teacher> getAllTeachers(){		
		return this.allTeachers;
	}
	
	/**
	 * 
	 * @param name - name of student
	 * @return student with name (name)
	 */
	public Student getStudent(String name){
		name = name.trim().toLowerCase();
		for (int j = 0; j < allStudents.size(); ++j){
			if (allStudents.get(j).getName().equals(name)){
				return allStudents.get(j);
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param name - name of teacher
	 * @return teacher with name (name)
	 */
	public Teacher getTeacher(String name){		
		name = name.trim().toLowerCase();
		for (int j = 0; j < allTeachers.size(); ++j){
			if (allTeachers.get(j).getName().equals(name)){
				return allTeachers.get(j);
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param ID - book ID
	 * @return book with ID (ID)
	 */
	public Book getBook(int ID){			
		for (int j = 0; j < allBooks.size(); ++j){
			if (allBooks.get(j).getID() == ID){
				return allBooks.get(j);
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param bName - name of book
	 * @return book with given name
	 */
	public Book findBookByName(String bName){		
		Book answer = null;
		for (Book b : this.allBooks){
			if (b.getName().toLowerCase().equals(bName.toLowerCase()) && b.getUserCheckedOut() == null){
				answer = b;
				break;
			}
		}
		return answer;
	}
	
	/**
	 * 
	 * @param bookName - name of book to be added
	 * @param authorName - name of author of book to be added
	 */
	public void addBook(String bookName, String authorName){	
		Book book = new Book(bookName, authorName);		
		allBooks.add(book);
	}
	
	/**
	 * 
	 * @param bookID - ID of book to be removed
	 */
	public void removeBook(int bookID){			
		allBooks.remove(getBook(bookID));
	}
	
	/**
	 * 
	 * @param name - name of student to be added
	 */
	public void addStudent(String name){	
		Student s = new Student(name);
		allStudents.add(s);
	}
	
	/**
	 * 
	 * @param name - name of student to be removed
	 */
	public void removeStudent(String name){		
		allStudents.remove(getStudent(name));
	}
	
	/**
	 * 
	 * @param name - name of teacher to be added
	 */
	public void addTeacher(String name){		
		Teacher t = new Teacher(name);
		allTeachers.add(t);
	}
	
	/**
	 * 
	 * @param name - name of teacher to be removed
	 */
	public void removeTeacher(String name){		
		allTeachers.remove(getTeacher(name));
	}

	
	/**
	 * generates issued books report and saves it in the Reports folder
	 * @param stud - whether students should be included in report
	 * @param teach - whether teachers should be included in report
	 * @throws FileNotFoundException
	 */
	public void generateIssuedBooks(boolean stud, boolean teach) throws FileNotFoundException{           
		PrintWriter outputFile = new PrintWriter("Reports\\issued_books_report.txt");  													
		Date d = new Date();
		if (stud){
			outputFile.println("STUDENTS");
			outputFile.println();
			for (int i = 0; i < allStudents.size(); ++i){
				Student current = allStudents.get(i);
				if (current.getBooksCheckedOut().size() == 0){
					continue;
				}
				outputFile.println(current.getName().toUpperCase() + "'s BOOKS DUE");
				for (int k = 0; k < current.getBooksCheckedOut().size(); ++k){
					Book currentBook = current.getBooksCheckedOut().get(k);
					//this calculates how far the due date is and prints it to the file
					int daysDueIn = (int) (Math.round(((currentBook.getDateDue().getTime() - d.getTime()) / 86400000)));
					if (daysDueIn < 0){
						outputFile.println(currentBook + "    OVERDUE BY " + (0 - daysDueIn) + " DAYS");
					}
					else{
						outputFile.println(currentBook + "    DUE IN " + daysDueIn + " DAYS");
					}	
				}
				outputFile.println();
			}
		}
		
		if (teach){
			outputFile.println();
			outputFile.println("TEACHERS");
			outputFile.println();
			for (int i = 0; i < allTeachers.size(); ++i){
				Teacher current = allTeachers.get(i);
				if (current.getBooksCheckedOut().size() == 0){
					continue;
				}
				outputFile.println(current.getName().toUpperCase() + "'s BOOKS DUE");
				for (int k = 0; k < current.getBooksCheckedOut().size(); ++k){
					Book currentBook = current.getBooksCheckedOut().get(k);
					int daysDueIn = (int) (Math.round(((currentBook.getDateDue().getTime() - d.getTime()) / 86400000)));
					if (daysDueIn < 0){
						outputFile.println(currentBook + "    OVERDUE BY " + (0 - daysDueIn) + " DAYS");
					}
					else{
						outputFile.println(currentBook + "    DUE IN " + daysDueIn + " DAYS");
					}
				}
				outputFile.println();
			}
		}
		outputFile.close();
	}
	
	/**
	 * generates fines report and saves it in the Reports folder
	 * @param stud - whether students should be included in report
	 * @param teach - whether teachers should be included in report
	 * @throws FileNotFoundException
	 */
	public void generateFinesReport(boolean stud, boolean teach) throws FileNotFoundException{ 
		PrintWriter outputFile = new PrintWriter("Reports\\fines_report.txt");  
																		
		Date d = new Date();
		outputFile.println("FINES");
		outputFile.println();
		for (int j = 0; j < allBooks.size(); ++j){
			Book currentBook = allBooks.get(j);
			if (currentBook.getDateDue() != null && currentBook.getDateDue().getTime() < d.getTime()){
				if (currentBook.getUserCheckedOut() instanceof Student && stud || currentBook.getUserCheckedOut() instanceof Teacher && teach){
					outputFile.println(currentBook.getUserCheckedOut().getName().toUpperCase() + " HAS NOT RETURNED " + currentBook.toString() + " ON TIME");
					int daysOverDue = (int) (Math.round(((d.getTime() - currentBook.getDateDue().getTime()) / 86400000)));
					outputFile.println(currentBook.getName() + " IS LATE BY " + daysOverDue + " DAYS");
					outputFile.println();
				}
			}
		}
		outputFile.close();
	}
	
	/**
	 * reads data from Storage_Files folder to use during program run-time
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 */
	public void readFromFile() throws IOException, FileNotFoundException, ClassNotFoundException{
		FileInputStream fis = new FileInputStream("Storage_Files\\students.txt");
	    ObjectInputStream input = new ObjectInputStream(fis);   //reads students.txt file
	    Student[] readStudents = (Student[]) input.readObject();
	    this.allStudents = new ArrayList<Student>(Arrays.asList(readStudents));
	    input.close();
	    
	    FileInputStream fis1 = new FileInputStream("Storage_Files\\teachers.txt");
	    ObjectInputStream input1 = new ObjectInputStream(fis1);   //reads teachers.txt file
	    Teacher[] readTeachers = (Teacher[])input1.readObject();
	    this.allTeachers = new ArrayList<Teacher>(Arrays.asList(readTeachers));
	    input1.close();
	    
	    FileInputStream fis2 = new FileInputStream("Storage_Files\\books.txt");
	    ObjectInputStream input2 = new ObjectInputStream(fis2);		//reads books.txt file
	    Book[] readBooks = (Book[])input2.readObject();
	    this.allBooks = new ArrayList<Book>(Arrays.asList(readBooks));
	    input2.close();
	    if (readBooks.length != 0){
	    	Book.IDNumber = readBooks[readBooks.length-1].getID();  
	    	//this gets the last id used, which helps give every book a different id
	    }
	}
	
	
	/**
	 * uploads and saves all data to the Storage_Files folder
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ClassCastException
	 */
	public void uploadToFile() throws IOException, FileNotFoundException, ClassCastException{
		Student[] uploadStudents = new Student[allStudents.size()];
		uploadStudents = this.allStudents.toArray(uploadStudents);
		FileOutputStream fos = new FileOutputStream("Storage_Files\\students.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(uploadStudents);   //converts the list of students to an array and saves to the file
        oos.close();
		
		Teacher[] uploadTeachers = new Teacher[allTeachers.size()];
		uploadTeachers = this.allTeachers.toArray(uploadTeachers);
		FileOutputStream fos1 = new FileOutputStream("Storage_Files\\teachers.txt");
        ObjectOutputStream oos1 = new ObjectOutputStream(fos1);
        oos1.writeObject(uploadTeachers);  //converts the list of teachers to an array and saves to the file
        oos1.close();
		
		Book[] uploadBooks = new Book[allBooks.size()];
		uploadBooks = this.allBooks.toArray(uploadBooks);
		FileOutputStream fos2 = new FileOutputStream("Storage_Files\\books.txt");
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(uploadBooks);     //converts the list of books to an array and saves to the file
        oos2.close();		
	}
}
