using Syncfusion.Windows.Forms;

namespace fec {

    public partial class AddEmployeeForm : MetroForm {

        public AddEmployeeForm() {
            InitializeComponent();
        }

        private void addButton_Click(object sender, System.EventArgs e) {

            DatabaseWorker.AddEmployee(nameTextBox.Text, jobTextBox.Text, addressTextBox.Text, phoneTextBox.Text);

            DialogResult = System.Windows.Forms.DialogResult.OK;
            Close();
        }
    }
}
