package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** 
 * Class LibraryFile 
 * This class contains methods to create and maintain all the library data: 
 * Students, Teachers, Books and Admin constants
 */
public class LibraryFile {

	ObservableList<Student> students = FXCollections.observableArrayList();
	ObservableList<Teacher> teachers = FXCollections.observableArrayList();
	ObservableList<LibraryBook> books = FXCollections.observableArrayList();
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM-dd-yyyy");

	/**
	 * readAdminData(fileName)
	 * Set the following class variables from the admin file name:
	 * 1. Student.BOOK_LIMIT  - number of books that a student is allowed to borrow
	 * 2. Student.DAY_LIMIT   - number of days a student is allowed to checkout books
	 * 3. Student.FINE_PER_DAY- fine amount per day charged for a student for overdue books
	 * 4. Teacher.BOOK_LIMIT  - number of books that a teacher is allowed to borrow
	 * 5. Teacher.DAY_LIMIT   - number of days a student is allowed to checkout books
	 * 6. Teacher.FINE_PER_DAY- fine amount per day charged for a teacher for overdue books
	 * 
	 * These constants are stored in a single line in the file Admin.txt
	 * 
	 */
	public void readAdminData(String fileName) {
		Scanner scan;
		try {
			scan = new Scanner(new File(fileName));
			while (scan.hasNextLine()) {
				String s = scan.nextLine();
				String[] data = s.split("\\|", 6);
				Student.bookLimit = Integer.parseInt(data[0]);
				Student.dayLimit = Integer.parseInt(data[1]);
				Student.finePerDay = Double.parseDouble(data[2]);
				Teacher.bookLimit = Integer.parseInt(data[3]);
				Teacher.dayLimit = Integer.parseInt(data[4]);
				Teacher.finePerDay = Double.parseDouble(data[5]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
	}

	public ObservableList<LibraryBook> getBookData(String fileName) {
		try {
			Scanner scan = new Scanner(new File(fileName));
			while (scan.hasNextLine()) {
				String s = scan.nextLine();
				LocalDate borDate = null;
				LocalDate retDate = null;
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM-dd-yyyy");
				String[] bookArray = s.split("\\|", 9);
				boolean ckdout = bookArray[3].equals("true") ? true : false;
				if (bookArray[7].equals(null) || bookArray[7] == null || bookArray[7].equals("null"))
					borDate = null;
				else 
					borDate = LocalDate.parse(bookArray[7], dtf);
				if (bookArray[8].equals(null) || bookArray[8] == null || bookArray[8].equals("null"))
					retDate = null;
				else 
					retDate = LocalDate.parse(bookArray[8], dtf);
				LibraryBook book = new LibraryBook(bookArray[0], bookArray[1], bookArray[2], ckdout, bookArray[4],
						bookArray[5], bookArray[6], borDate ,
						retDate );
				books.add(book);

			}

		} catch (FileNotFoundException e) {
			System.out.println("File " + fileName + " not found.");
		}
		return books;

	}

	public ObservableList<Student> getStudentData(String fileName) {
		try {
			Scanner scan = new Scanner(new File(fileName));
			String currId = null;
			ArrayList<SimpleStringProperty> stuBooks = new ArrayList<>();
			ArrayList<LocalDate> borrDates = new ArrayList<>();
			ArrayList<LocalDate> retDates = new ArrayList<>();
			int counter = -1;
			while (scan.hasNextLine()) {
				String s = scan.nextLine();
				String[] studentArray = s.split("\\|", 6);
				if (counter < 0) {
					stuBooks.add(new SimpleStringProperty(studentArray[3]));
					borrDates.add(studentArray[4].equals("null") ? null : LocalDate.parse(studentArray[4], dtf));
					retDates.add(studentArray[5].equals("null") ? null : LocalDate.parse(studentArray[5], dtf));
					Student stu = new Student(studentArray[0], studentArray[1], studentArray[2], stuBooks, borrDates,
							retDates);
					students.add(stu);
					counter++;
					currId = studentArray[0];
					continue;
				}

				if (currId.equals(studentArray[0])) {
					stuBooks.add(new SimpleStringProperty(studentArray[3]));
					students.get(counter).getBorrowedDates()
							.add(studentArray[4].equals("null") ? null : LocalDate.parse(studentArray[4], dtf));
					students.get(counter).getReturnDates()
							.add(studentArray[5].equals("null") ? null : LocalDate.parse(studentArray[5], dtf));
				} else {
					stuBooks = new ArrayList<>();
					borrDates = new ArrayList<>();
					retDates = new ArrayList<>();
					stuBooks.add(new SimpleStringProperty(studentArray[3]));
					borrDates.add(studentArray[4].equals("null") ? null : LocalDate.parse(studentArray[4], dtf));
					retDates.add(studentArray[5].equals("null") ? null : LocalDate.parse(studentArray[5], dtf));
					Student stu = new Student(studentArray[0], studentArray[1], studentArray[2], stuBooks, borrDates,
							retDates);
					students.add(stu);
					currId = studentArray[0];
					counter++;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File " + fileName + " not found.");
		}

		return students;

	}

	public ObservableList<Teacher> getTeacherData(String fileName) {
		try {
			Scanner scan = new Scanner(new File(fileName));
			String currId = null;
			ArrayList<SimpleStringProperty> teachBooks = new ArrayList<>();
			ArrayList<LocalDate> borrDates = new ArrayList<>();
			ArrayList<LocalDate> retDates = new ArrayList<>();
			int counter = -1;
			while (scan.hasNextLine()) {
				String s = scan.nextLine();
				String[] teacherArray = s.split("\\|", 6);
				if (counter < 0) {
					teachBooks.add(new SimpleStringProperty(teacherArray[3]));
					borrDates.add(teacherArray[4].equals("null") ? null : LocalDate.parse(teacherArray[4], dtf));
					retDates.add(teacherArray[5].equals("null") ? null : LocalDate.parse(teacherArray[5], dtf));
					Teacher teach = new Teacher(teacherArray[0], teacherArray[1], teacherArray[2], teachBooks,
							borrDates, retDates);
					teachers.add(teach);
					counter++;
					currId = teacherArray[0];
					continue;
				}

				if (currId.equals(teacherArray[0])) {
					teachBooks.add(new SimpleStringProperty(teacherArray[3]));
					teachers.get(counter).getBorrowedDates()
							.add(teacherArray[4].equals("null") ? null : LocalDate.parse(teacherArray[4], dtf));
					teachers.get(counter).getReturnDates()
							.add(teacherArray[5].equals("null") ? null : LocalDate.parse(teacherArray[5], dtf));
				} else {
					teachBooks = new ArrayList<>();
					borrDates = new ArrayList<>();
					retDates = new ArrayList<>();
					teachBooks.add(new SimpleStringProperty(teacherArray[3]));
					borrDates.add(teacherArray[4].equals("null") ? null : LocalDate.parse(teacherArray[4], dtf));
					retDates.add(teacherArray[5].equals("null") ? null : LocalDate.parse(teacherArray[5], dtf));
					Teacher teach = new Teacher(teacherArray[0], teacherArray[1], teacherArray[2], teachBooks,
							borrDates, retDates);
					teachers.add(teach);
					currId = teacherArray[0];
					counter++;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File " + fileName + " not found.");
		}

		return teachers;

	}

	public void writeBookData(String fileName) {

		try {
			FileWriter fw = new FileWriter(fileName);
			PrintWriter pw = new PrintWriter(fw);

			for (int i = 0; i < books.size(); i++) {
				pw.write(books.get(i).getBookId() + "|");
				pw.write(books.get(i).getBookName() + "|");
				pw.write(books.get(i).getAuthorName() + "|");
				if(books.get(i).getIsCheckedOut().equals("Checked Out"))
					pw.write("true" + "|");
				else
					pw.write("false" + "|");
				try {
					pw.write(books.get(i).getCheckedOutBy() + "|");
				}
				catch(NullPointerException ne) {
					pw.write("null" + "|");
				}
				try {
					pw.write(books.get(i).getCheckedOutId() + "|");
				}
				catch(NullPointerException ne) {
					pw.write("null" + "|");
				}
				try {
					pw.write(books.get(i).getBorrowerName() + "|");
				}
				catch(NullPointerException ne) {
					pw.write("null" + "|");
				}
				try {
					pw.write(books.get(i).getCheckedOutDate().format(dtf) + "|");
				} catch (NullPointerException ne) {
					pw.write("null" + "|");
				}
				try {
					pw.write(books.get(i).getReturnDate().format(dtf) + "\n");
				} catch (NullPointerException ne) {
					pw.write("null" + "\n");
				}
			}

			pw.close();
			fw.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void writeStudentData(String fileName) {

		try {
			FileWriter fw = new FileWriter(fileName);
			PrintWriter pw = new PrintWriter(fw);

			for (int i = 0; i < students.size(); i++) {
				if (students.get(i).getBookIds().size() == 0) {
					pw.write(students.get(i).getId() + "|");
					pw.write(students.get(i).getLastName() + "|");
					pw.write(students.get(i).getFirstName() + "|");
					pw.write("null|null|null\n");
				} else {
					for (int j = 0; j < students.get(i).getBookIds().size(); j++) {
						pw.write(students.get(i).getId() + "|");
						pw.write(students.get(i).getLastName() + "|");
						pw.write(students.get(i).getFirstName() + "|");
						pw.write(students.get(i).getBookIds().get(j) + "|");

						try {
							if (students.get(i).getBorrowedDates() == null
									|| students.get(i).getBorrowedDates().equals(null)
									|| students.get(i).getBorrowedDates().equals("null"))
								pw.write("null" + "|");
							else
								pw.write(students.get(i).getBorrowedDates().get(j).format(dtf) + "|");
						} catch (NullPointerException ne) {
							pw.write("null" + "|");
						}
						try {
							if (students.get(i).getReturnDates() == null
									|| students.get(i).getReturnDates().equals(null)
									|| students.get(i).getReturnDates().equals("null"))
								pw.write("null" + "\n");
							else
								pw.write(students.get(i).getReturnDates().get(j).format(dtf) + "\n");
						} catch (NullPointerException ne) {
							pw.write("null" + "\n");
						}

					}

				}
			}

			pw.close();
			fw.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void writeTeacherData(String fileName) {

		try {
			FileWriter fw = new FileWriter(fileName);
			PrintWriter pw = new PrintWriter(fw);

			for (int i = 0; i < teachers.size(); i++) {
				if (teachers.get(i).getBookIds().size() == 0) {
					pw.write(teachers.get(i).getId() + "|");
					pw.write(teachers.get(i).getLastName() + "|");
					pw.write(teachers.get(i).getFirstName() + "|");
					pw.write("null|null|null\n");
				} else {
					for (int j = 0; j < teachers.get(i).getBookIds().size(); j++) {
						pw.write(teachers.get(i).getId() + "|");
						pw.write(teachers.get(i).getLastName() + "|");
						pw.write(teachers.get(i).getFirstName() + "|");
						pw.write(teachers.get(i).getBookIds().get(j) + "|");
						try {
							if (teachers.get(i).getBorrowedDates() == null
									|| teachers.get(i).getBorrowedDates().equals(null)
									|| teachers.get(i).getBorrowedDates().equals("null"))
								pw.write("null" + "|");
							else
								pw.write(teachers.get(i).getBorrowedDates().get(j).format(dtf) + "|");
						} catch (NullPointerException ne) {
							pw.write("null" + "|");
						}
						try {
							if (teachers.get(i).getReturnDates() == null
									|| teachers.get(i).getReturnDates().equals(null)
									|| teachers.get(i).getReturnDates().equals("null"))
								pw.write("null" + "\n");
							else
								pw.write(teachers.get(i).getReturnDates().get(j).format(dtf) + "\n");
						} catch (NullPointerException ne) {
							pw.write("null" + "\n");
						}
					}
				}
			}

			pw.close();
			fw.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
