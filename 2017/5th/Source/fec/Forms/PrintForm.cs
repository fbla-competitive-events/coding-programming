using Syncfusion.Windows.Forms;
using System.Windows.Forms;

namespace fec {

    public partial class PrintForm : MetroForm {

        // Used to control when form can be closed.
        bool allowClose = true;

        // Property that points to the tableComboBox text.
        // Enables other classes to access this data.
        public string SelectedTable { get { return tableComboBox.Text; } }

        public PrintForm() {
            InitializeComponent();

            // Set the form icon.
            this.Icon = Properties.Resources.fecIcon;

            tableComboBox.SelectedIndex = 0;
        }

        /// <summary>
        /// If the print button is clicked and there are no errors, close the form.
        /// </summary>
        private void PrintButton_Click(object sender, System.EventArgs e) {

            if (tableComboBox.SelectedIndex != 0) {

                DialogResult = DialogResult.OK;
                Close();
            } 
            else {
                MessageBoxAdv.Show(this, "Please select a table to print", "Error");

                // Do not close the form when the message box is dismissed.
                allowClose = false;
            }
        }

        /// <summary>
        /// Limits form closing.
        /// </summary>
        private void PrintForm_FormClosing(object sender, FormClosingEventArgs e) {
            if (!allowClose) {
                // Abort form closing
                e.Cancel = true;
            }

            allowClose = true;
        }
    }
}
