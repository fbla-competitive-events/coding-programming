using Syncfusion.Windows.Forms;
using System;
using System.Windows.Forms;

namespace fec {

    public partial class ConfirmPasswordForm : MetroForm {

        public ConfirmPasswordForm() {
            InitializeComponent();

            // Bind a key press event. 
            // If the return key is pressed when typing in the password box, execute submit button click.
            passwordTextBox.KeyDown += (sender, args) => {
                if (args.KeyCode == Keys.Return) {
                    submitPasswordButton.PerformClick();
                }
            };
        }

        private void SubmitPasswordButton_Click(object sender, EventArgs e) {

            // Check if the password field is empty.
            if (passwordTextBox.TextLength == 0) {
                MessageBoxAdv.Show(this, "Please enter the current password of the database.", "Error");
                return;
            }

            // If the entered password matches the database password.
            if (Crypto.Utilities.SecureStringToString(DatabaseProperties.password) == passwordTextBox.Text) {
                DialogResult = DialogResult.OK;
                Close();
            }
            else {
                MessageBoxAdv.Show(this, "Invalid password.", "Error");
            }
        }

        /// <summary>
        /// Show or hide the password characters depending on whether the 'show' checkbox is checked or unchecked respectively.
        /// </summary>
        private void ShowPasswordCheckBox_CheckedChanged(object sender, EventArgs e) {
            if (showPasswordCheckBox.Checked) {
                passwordTextBox.UseSystemPasswordChar = false;
            }
            else {
                passwordTextBox.UseSystemPasswordChar = true;
            }
        }
    }
}
