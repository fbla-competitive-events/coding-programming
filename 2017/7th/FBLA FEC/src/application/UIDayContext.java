package application;

//import statements
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class UIDayContext {
	
	//Given the date, this class helps facilitate setting labels and list views in the employee scheduler
	public Label labelObj;
	public ListView<String> listViewObj;
	
	public UIDayContext(Label labelObj, ListView<String> listViewObj) {
		this.labelObj = labelObj;
		this.listViewObj = listViewObj;
	}
}
