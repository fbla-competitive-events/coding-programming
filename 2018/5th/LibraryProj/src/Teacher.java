import java.util.ArrayList;

public class Teacher extends User{ 		//teacher has all the methods and properties of User, since User is its superclass
	
	private static final long serialVersionUID = 1841602441921429621L; 		//this number allows saving to the file
	
	/**
	 * Constructor to create new teacher
	 * @param teacherName - name of teacher to be constructed
	 */
	public Teacher(String teacherName){	 	
		super(teacherName);
	}
	
}
