using Syncfusion.Windows.Forms;
using System;
using System.Drawing;
using System.Windows.Forms;

namespace fec {

    public partial class DatabaseSecurityForm : MetroForm {

        // Holds a value representing whether the database is encrypted.
        private bool databaseEncrypted = false;

        // A red color used in the encryption label when the database is unencrypted.
        private readonly Color redColor = ColorTranslator.FromHtml("#c51616");

        public DatabaseSecurityForm() {
            InitializeComponent();

            // Set message box colors to a darker blue color.
            MessageBoxAdv.MetroColorTable.BorderColor = System.Drawing.Color.RoyalBlue;
            MessageBoxAdv.MetroColorTable.YesButtonBackColor = System.Drawing.Color.RoyalBlue;
            MessageBoxAdv.MetroColorTable.NoButtonBackColor = System.Drawing.Color.RoyalBlue;
            MessageBoxAdv.MetroColorTable.OKButtonBackColor = System.Drawing.Color.RoyalBlue;

            // If the database password is not empty then the database is encrypted, else, it is unencrypted.
            if (DatabaseProperties.password.Length > 0) {

                // Setup UI to indicate that the database is encrypted.
                databaseEncrypted = true;
                SetupEncrypted();
            }
            else {

                // Setup UI to indicate that the database is unencrypted.
                databaseEncrypted = false;
                SetupUnencrypted();
            }
        }

        /// <summary>
        /// Setup UI to indicate that the datbase is encrypted.
        /// </summary>
        private void SetupEncrypted() {
            enableDisableEncLabel.Text = "Disable Encryption";
            enableDisableEncButton.Image = Properties.Resources.DecryptDatabase;
            changePasswordLabel.Enabled = true;
            changePasswordButton.Enabled = true;
            encryptionLabel.Text = "ENABLED";
            encryptionLabel.ForeColor = Color.RoyalBlue;
        }

        /// <summary>
        /// Setup UI to indicate that the database is unencrypted.
        /// </summary>
        private void SetupUnencrypted() {
            enableDisableEncLabel.Text = "Enable Encryption";
            enableDisableEncButton.Image = Properties.Resources.EncryptDatabase;
            changePasswordLabel.Enabled = false;
            changePasswordButton.Enabled = false;
            encryptionLabel.Text = "DISABLED";
            encryptionLabel.ForeColor = redColor;
        }

        /// <summary>
        /// Displays a confirmation message.
        /// </summary>
        /// <param name="message"> The message to display. </param>
        /// <returns> Whether the user clicked the 'Yes' button. </returns>
        private bool GetConfirmation(string message) {
            if (MessageBoxAdv.Show(this, message, "Confirm", MessageBoxButtons.YesNo) == DialogResult.Yes) {
                return true;
            }
            else {
                return false;
            }
        }

        /// <summary>
        /// Enable/Disable encryption based on whether the database is unencrypted or encrypted respectively.
        /// </summary>
        private void EnableDisableEncButton_Click(object sender, EventArgs e) {
            if (databaseEncrypted) {
                if (GetConfirmation("Are you sure you wish to disable database encryption?\n" 
                    + "This will make the data stored in the database vulnerable to hackers." )) 
                {
                    ConfirmPasswordForm confirmPasswordForm = new ConfirmPasswordForm();
                    confirmPasswordForm.ShowDialog();

                    if (confirmPasswordForm.DialogResult == DialogResult.OK) {

                        // Clear the value in the password SecureString.
                        DatabaseProperties.password.Clear();

                        // Setup UI to indicate that the database is unencrypted.
                        SetupUnencrypted();
                        databaseEncrypted = false;

                        MessageBoxAdv.Show(this, "Encryption disabled.", "Success.");
                    }                 
                }
            }
            else {
                if (GetConfirmation("Are you sure you wish to enable database encryption?\n"
                    + "Enabling database encryption will protect database data while not being used.\n"
                    + "If database encryption is enabled, a password will be required to open the encrypted database.\n"
                    + "Keep in mind that if the password is lost, then the database cannot be decrypted.\n\n"
                    + "Be sure to backup the database.")) 
                {
                    SetPasswordForm setPasswordForm = new SetPasswordForm();
                    setPasswordForm.ShowDialog(this);

                    if (setPasswordForm.DialogResult == DialogResult.OK) {

                        // Set new database password.
                        foreach (char c in setPasswordForm.Password) {
                            DatabaseProperties.password.AppendChar(c);
                        }

                        // Setup UI to indicate that the database is encrypted.
                        SetupEncrypted();
                        databaseEncrypted = true;

                        MessageBoxAdv.Show(this, "Encryption enabled.\n The database will be encrypted when the application is exitted.", "Success.");
                    }
                }              
            }
        }

        /// <summary>
        /// Open a form to change the database password.
        /// </summary>
        private void ChangePasswordButton_Click(object sender, EventArgs e) {
            ChangePasswordForm changePasswordForm = new ChangePasswordForm();
            changePasswordForm.ShowDialog(this);

            if (changePasswordForm.DialogResult == DialogResult.OK) {
                MessageBoxAdv.Show(this, "The database password was changed.\n The database will be encrypted using the new password when the application is exitted.", "Success.");
            }
        }

        /// <summary>
        /// When the form is closing, change message box colors to an orange color.
        /// </summary>
        private void DatabaseSecurityForm_FormClosing(object sender, FormClosingEventArgs e) {
            // Set message box colors to an orange color.
            MessageBoxAdv.MetroColorTable.BorderColor = Color.Coral;
            MessageBoxAdv.MetroColorTable.YesButtonBackColor = Color.Coral;
            MessageBoxAdv.MetroColorTable.NoButtonBackColor = Color.Coral;
            MessageBoxAdv.MetroColorTable.OKButtonBackColor = Color.Coral;
        }
    }
}
