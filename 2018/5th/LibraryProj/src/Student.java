import java.util.ArrayList;

public class Student extends User{ 			//student has all the methods and properties of a user, since User is its superclass

	private static final long serialVersionUID = -2301521390825164717L; 	//this number allows saving to the file

	/**
	 * Constructor creates new student with given name
	 * @param studentName - name of student to be created
	 */
	public Student(String studentName){	
		super(studentName);
	}	
}
