using Syncfusion.Windows.Forms;
using System.Windows.Forms;

namespace fec {
    public partial class PrintForm : MetroForm {

        // Used to control when form can be closed
        bool allowClose = true;

        public string SelectedTable { get { return tableComboBox.Text; } }

        public PrintForm() {
            InitializeComponent();
            this.Icon = Properties.Resources.fecIcon;

            tableComboBox.SelectedIndex = 0;
        }

        private void printButton_Click(object sender, System.EventArgs e) {

            if (tableComboBox.SelectedIndex != 0) {
                
                DialogResult = DialogResult.OK;
                Close();
            } 
            else {
                MessageBoxAdv.MessageBoxStyle = MessageBoxAdv.Style.Metro;
                MessageBoxAdv.Show(this, "Please select a table to print", "Error");

                // Do not close form when the message box is dismissed
                allowClose = false;
            }
        }

        private void PrintForm_FormClosing(object sender, FormClosingEventArgs e) {
            if (!allowClose) {
                e.Cancel = true;
            }

            allowClose = true;
        }
    }
}
