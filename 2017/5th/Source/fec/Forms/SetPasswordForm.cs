using Syncfusion.Windows.Forms;
using System;
using System.Windows.Forms;

namespace fec {

    public partial class SetPasswordForm : MetroForm {

        // Getter for the password
        public string Password {
            get {
                return passwordTextBox.Text;
            }
        }

        public SetPasswordForm() {
            InitializeComponent();
        }

        /// <summary>
        /// Show/hide password according to whether the 'show' checkbox is checked.
        /// </summary>
        private void ShowPasswordCheckBox_CheckedChanged(object sender, EventArgs e) {
            if (showPasswordCheckBox.Checked) {
                passwordTextBox.UseSystemPasswordChar = false;
            }
            else {
                passwordTextBox.UseSystemPasswordChar = true;
            }
        }

        /// <summary>
        /// When the set button is clicked, verify that there are no errors and close the dialog.
        /// </summary>
        private void SetPasswordButton_Click(object sender, EventArgs e) {

            // If the password text box is blank show an error
            if (passwordTextBox.TextLength == 0) {
                MessageBoxAdv.Show(this, "Please enter a password.", "Error");
                return;
            }

            // If the password text box is not blank, but the confirm password text box is, show an error.
            if (passwordTextBox.TextLength > 0 && confirmPasswordTextBox.TextLength == 0) {
                MessageBoxAdv.Show(this, "Please confirm entered password.", "Error");
                return;
            }

            // If the values of the password text box and the confirm password text box do not match, show an error.
            if (passwordTextBox.Text != confirmPasswordTextBox.Text) {
                MessageBoxAdv.Show(this, "Passwords do not match.", "Error");
                return;
            }

            this.DialogResult = DialogResult.OK;

            Close();
        }
    }
}
