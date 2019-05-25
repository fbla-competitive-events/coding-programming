using Syncfusion.Windows.Forms;
using System.Windows.Forms;
using System;

namespace fec {

    public partial class ChangePasswordForm : MetroForm {

        public ChangePasswordForm() {
            InitializeComponent();
        }

        private void ChangePasswordButton_Click(object sender, EventArgs e) {
            // Check if the current password field is empty.
            if (currentPasswordTextBox.TextLength == 0) {
                MessageBoxAdv.Show(this, "Please enter the current password of the database.", "Error");
                return;
            }
            // Check if the new password field is empty.
            if (newPasswordTextBox.TextLength == 0) {
                MessageBoxAdv.Show(this, "Please enter a new password for the database.", "Error");
                return;
            }
            // If the new password text box is not blank, but the confirm password text box is, show an error.
            if (newPasswordTextBox.TextLength > 0 && confirmNewPasswordTextBox.TextLength == 0) {
                MessageBoxAdv.Show(this, "Please confirm new password.", "Error");
                return;
            }

            // If the entered new password does not match the current database password show an error.
            if (Crypto.Utilities.SecureStringToString(DatabaseProperties.password) != currentPasswordTextBox.Text) {
                MessageBoxAdv.Show(this, "Invalid current password.", "Error");
                return;
            }

            // If the values of the new password text box and the confirm new password text box do not match, show an error.
            if (newPasswordTextBox.Text != confirmNewPasswordTextBox.Text) {
                MessageBoxAdv.Show(this, "New passwords do not match.", "Error");
                return;
            }

            // Clear the current password.
            DatabaseProperties.password.Clear();

            // Change the database password.
            foreach (char c in newPasswordTextBox.Text) {
                DatabaseProperties.password.AppendChar(c);
            }

            DialogResult = DialogResult.OK;

            Close();
        }

        /// <summary>
        /// Show or hide the password characters depending on whether the 'show' checkbox is checked or unchecked respectively.
        /// </summary>
        private void ShowCurrentPasswordCheckBox_CheckedChanged(object sender, EventArgs e) {
            if (showCurrentPasswordCheckBox.Checked) {
                currentPasswordTextBox.UseSystemPasswordChar = false;
            }
            else {
                currentPasswordTextBox.UseSystemPasswordChar = true;
            }
        }

        /// <summary>
        /// Show or hide the password characters depending on whether the 'show' checkbox is checked or unchecked respectively.
        /// </summary>
        private void ShowNewPasswordCheckBox_CheckedChanged(object sender, EventArgs e) {
            if (showNewPasswordCheckBox.Checked) {
                newPasswordTextBox.UseSystemPasswordChar = false;
            }
            else {
                newPasswordTextBox.UseSystemPasswordChar = true;
            }
        }
    }
}
