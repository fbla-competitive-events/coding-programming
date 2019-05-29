package application;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

public class LibraryUtils {
	
	int getNextBookIds(ObservableList<LibraryBook> books) {
		int maxId = 0;
		int temp = 0;
		for (LibraryBook book : books) {
			temp = Integer.parseInt(book.getBookId());
			if (maxId < temp)
				maxId = temp;
		}
		return maxId;
	}

	int getNextIds(ObservableList<? extends Borrower> borrowers) {
		int maxId = 0;
		int temp = 0;
		for (Borrower borrower : borrowers) {
			temp = Integer.parseInt(borrower.getId());
			if (maxId < temp)
				maxId = temp;
		}
		return maxId;
	}

	boolean isValidStudent(String studentId, ObservableList<Student> students) {
		boolean isValidId = false;

		for (int i = 0; i < students.size(); i++) {
			if (students.get(i).getId().equals(studentId)) {
				isValidId = true;
			}
		}

		return isValidId;
	}

	boolean isValidTeacher(String teacherId, ObservableList<Teacher> teachers) {
		boolean isValidId = false;

		for (int i = 0; i < teachers.size(); i++) {
			if (teachers.get(i).getId().equals(teacherId)) {
				isValidId = true;
			}
		}
		return isValidId;
	}

	/**
	 * populateBooksArray
	 */
	public void populateBooksArray(ObservableList<LibraryBook> books, ObservableList<Student> students, 
			ObservableList<Teacher> teachers) {
		for (LibraryBook book : books) {
			for (Student student : students) {
				if (book.getCheckedOutId().equals(student.getId())) {
					student.bookIds.add(new SimpleStringProperty(book.getBookId()));
					student.getBorrowedDates().add(book.getCheckedOutDate());
					student.getReturnDates().add(book.getReturnDate());
				}
			}
			for (Teacher teacher : teachers) {
				if (book.getCheckedOutId().equals(teacher.getId())) {
					teacher.bookIds.add(new SimpleStringProperty(book.getBookId()));
					teacher.getBorrowedDates().add(book.getCheckedOutDate());
					teacher.getReturnDates().add(book.getReturnDate());
				}
			}
		}
	}

	/**
	 * 
	 * @param bookIdField
	 * @param borrowerIdField
	 * @param borrowerType
	 * @return
	 */
	public String bookBorrow(TextField bookIdField, TextField borrowerIdField, String borrowerType,
			ObservableList<LibraryBook> books, ObservableList<Student> students, 
			ObservableList<Teacher> teachers) {
		String msg = "";
		for (LibraryBook book : books) {
			if (book.bookId.get().equals(bookIdField.getText())) {
				if (!book.isCheckedOut) {
					if (borrowerType.equals("Student")) {
						for(Student student : students) {
							if (student.id.get().equals(borrowerIdField.getText())) {
								if (student.bookIds.size() <= Student.bookLimit) {
									int index = student.bookIds.size();
									student.bookIds.add(index, new SimpleStringProperty(bookIdField.getText()));
									book.isCheckedOut = true;
									book.checkedOutBy = new SimpleStringProperty("Student");
									book.borrowerName = new SimpleStringProperty(
											student.getFirstName() + " " + student.getLastName());
									book.checkedOutId = new SimpleStringProperty(borrowerIdField.getText());
									book.checkedOutDate = LocalDate.now();
									book.returnDate = LocalDate.now().plusDays(Student.dayLimit);
									student.borrowedDates.add(index, LocalDate.now());
									student.returnDates.add(index, LocalDate.now().plusDays(Student.dayLimit));
									msg = ( "Transaction Successful: Book Checked Out \n" + bookIdField.getText() + ": " + book.getBookName()  + "\n Checked Out to: \n" 
										      + borrowerIdField.getText() + ": " + student.getFirstName() + " " + student.getLastName());
								} else {
									msg = "Book limit reached. Please return books before borrowing more books.";
								}
							}
						}
					}

					else if (borrowerType.equals("Teacher")) {
						for (Teacher teacher : teachers) {
							if (teacher.id.get().equals(borrowerIdField.getText())) {
								if (teacher.bookIds.size() <= Teacher.bookLimit) {
									int index = teacher.bookIds.size();
									teacher.bookIds.add(index, new SimpleStringProperty(bookIdField.getText()));
									book.isCheckedOut = true;
									book.checkedOutBy = new SimpleStringProperty("Teacher");
									book.borrowerName = new SimpleStringProperty(
											teacher.getFirstName() + " " + teacher.getLastName());
									book.checkedOutId = new SimpleStringProperty(borrowerIdField.getText());
									book.checkedOutDate = LocalDate.now();
									book.returnDate = LocalDate.now().plusDays(Teacher.dayLimit);
									teacher.borrowedDates.add(index, LocalDate.now());
									teacher.returnDates.add(index, LocalDate.now().plusDays(Teacher.dayLimit));
									msg = ( "Transaction Successful: Book Checked Out \n" + bookIdField.getText() + ": " + book.getBookName()  + "\n Checked Out to: \n" 
									      + borrowerIdField.getText() + ": " + teacher.getFirstName() + " " + teacher.getLastName());
								} else {
									msg = "Book limit reached. Please return books before borrowing more books.";
								}
							}
						}

					}
				} else {
					msg = "Book Checked Out by Another Member. Please Choose Another Book";
				}
			}
		}
		return msg;
	}

	public String bookReturn(TextField bookIdField, ObservableList<LibraryBook> books, 
			ObservableList<Student> students, ObservableList<Teacher> teachers) {
		String message = "";
		boolean bookReturned = false;
		ArrayList<SimpleStringProperty> temp = new ArrayList<>();
		for (LibraryBook book : books) {
			if (bookReturned)
				break;
			if (book.bookId.get().equals(bookIdField.getText())) {
				System.out.println("Book to be deleted: " + bookIdField.getText());
				if (book.isCheckedOut) {
					System.out.println("Book to be checked out ");
					for (Student student : students) {
						if (bookReturned)
							break;
						temp = new ArrayList<>();
						//if (student.bookIds.size() <= Student.bookLimit && student.bookIds.size() > 0) {
							temp.addAll(student.bookIds);
							System.out.println("Temp array has:" + temp.toString());
							for (int i = 0; i < temp.size(); i++) {
								if (temp.get(i).get().equals(bookIdField.getText())) {
									System.out.println("Remove: " + temp.get(i).get());
									student.bookIds.remove(i);
								}
								else {
									continue;
								}
								book.isCheckedOut = false;
								book.checkedOutBy = null;
								book.borrowerName = null;
								book.checkedOutId = null;
								book.checkedOutDate = null;
								book.returnDate = null;
								message = "Book " + bookIdField.getText() + " Returned Successfully.";
								bookReturned = true;
								break;
							}
						//}
					}

					for (Teacher teacher : teachers) {
						if (bookReturned)
							break;
						temp = new ArrayList<>();
						//if (teacher.bookIds.size() <= Teacher.bookLimit && teacher.bookIds.size() > 0) {
							temp.addAll(teacher.bookIds);
							for (int i = 0; i < temp.size(); i++) {
								if (temp.get(i).get().equals(bookIdField.getText())) {
									teacher.bookIds.remove(i);
								}
								else {
									continue;
								}
								book.isCheckedOut = false;
								book.checkedOutBy = null;
								book.borrowerName = null;
								book.checkedOutId = null;
								book.checkedOutDate = null;
								book.returnDate = null;
								message = "Book " + bookIdField.getText() + " Returned Successfully.";
								bookReturned = true;
								break;
							}
						//}
					}
				} else {
					message = "Book " + bookIdField.getText() + " is not Checkedout.";
				}
			} else {
				message = "Not a Valid Book ID. Please Enter A Valid ID";
			}
		}
		return message;
	}

	public static String calcFine(LocalDate borrowedDate, LocalDate returnDate, double finePerDay) {

		int overLimit = 0;

		if (LocalDate.now().getYear() == returnDate.getYear()
				&& LocalDate.now().getDayOfYear() > returnDate.getDayOfYear()) {
			overLimit = LocalDate.now().getDayOfYear() - returnDate.getDayOfYear();
		}

		else if (LocalDate.now().getYear() != returnDate.getYear()) {
			overLimit = (LocalDate.now().getDayOfYear() + 365) - returnDate.getDayOfYear();
		}
		DecimalFormat df = new DecimalFormat("#0.00");
		if (overLimit == 0)
			return "$" + df.format(0.0);
		else
			return "$" + df.format(overLimit * finePerDay);
	}

	public void printBorrowers(ObservableList<Student> students, ObservableList<Teacher> teachers) {

		System.out.println("Students:");
		for (Borrower b : students) {
			System.out.println(b);
		}
		System.out.println();
		System.out.println("Teachers:");
		for (Borrower b : teachers) {
			System.out.println(b);
		}
	}

	public boolean isDuplicate(String firstName, String lastName, String borrowerType, 
			ObservableList<Student> students, ObservableList<Teacher> teachers) {
		boolean isDuplicate = false;

		if (borrowerType.equals("Student")) {
			for (int i = 0; i < students.size(); i++) {
				if (students.get(i).getFirstName().equals(firstName)
						&& students.get(i).getLastName().equals(lastName)) {
					isDuplicate = true;
				}
			}
		}

		else if (borrowerType.equals("Teacher")) {
			for (int i = 0; i < teachers.size(); i++) {
				if (teachers.get(i).getFirstName().equals(firstName)
						&& teachers.get(i).getLastName().equals(lastName)) {
					isDuplicate = true;
				}
			}

		}

		return isDuplicate;
	}

}
