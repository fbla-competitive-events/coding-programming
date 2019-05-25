using Syncfusion.Windows.Forms;
using System;
using System.IO;
using System.Windows.Forms;

namespace fec {

    public partial class EnterPasswordForm : MetroForm {

        // Used to prevent the encrypted database file from being deleted while the application is running.
        private readonly FileStream ENCRYPTED_DB_LOCK;

        // Used to allow access to another thread to retrieve any decryption error messages.
        private string decryptionError;

        // Used to control form closing.
        private bool closed = false;

        public EnterPasswordForm() {
            InitializeComponent();

            // Open a stream to the encrypted database file to prevent it from being deleted.
            ENCRYPTED_DB_LOCK = File.Open(DatabaseProperties.ENCRYPTED_DATABASE_NAME, FileMode.Open, FileAccess.Read, FileShare.ReadWrite);

            // Set message box colors to a darker blue color.
            MessageBoxAdv.MetroColorTable.BorderColor = System.Drawing.Color.RoyalBlue;
            MessageBoxAdv.MetroColorTable.YesButtonBackColor = System.Drawing.Color.RoyalBlue;
            MessageBoxAdv.MetroColorTable.NoButtonBackColor = System.Drawing.Color.RoyalBlue;
            MessageBoxAdv.MetroColorTable.OKButtonBackColor = System.Drawing.Color.RoyalBlue;

            // Set global message box style.
            MessageBoxAdv.MessageBoxStyle = MessageBoxAdv.Style.Metro;
            // Bind a key press event. 
            // If the return key is pressed when typing in the password box, execute submit button click.
            passwordTextBox.KeyDown += (sender, args) => {
                if (args.KeyCode == Keys.Return) {
                    submitPasswordButton.PerformClick();
                }
            };
        }

        /// <summary>
        /// Attempt to decrypt database using the password entered.
        /// </summary>
        private void SubmitPasswordButton_Click(object sender, EventArgs e) {

            // Check if the password field is empty.
            if (passwordTextBox.TextLength == 0) {
                MessageBoxAdv.Show(this, "Please enter a password to unlock the database.", "Error");
                return;
            }

            ShowProgressBar();

            // Run background worker.
            decryptionWorker.RunWorkerAsync();
        }

        /// <summary>
        /// Decrypt database on a separate thread.
        /// </summary>
        #region Background Worker
        private void DecryptionWorker_DoWork(object sender, System.ComponentModel.DoWorkEventArgs e) {
            try {
                // Decrypt the encrypted database.
                Crypto.Symmetric.DecryptDB(DatabaseProperties.ENCRYPTED_DATABASE_NAME,
                                           DatabaseProperties.DATABASE_NAME,
                                           passwordTextBox.Text,
                                           DatabaseProperties.PBKDF2_ITERATIONS);
            }
            catch (Exception ex) {
                decryptionError = ex.Message;
                e.Cancel = true;
            }
        }

        /// <summary>
        /// If the background worker completed without errors, proceed with displaying the main UI.
        /// </summary>
        private void DecryptionWorker_WorkCompleted(object sender, System.ComponentModel.RunWorkerCompletedEventArgs e) {

            // If the execution of the background worker was cancelled, hide the progress bar and show an error.
            if (e.Cancelled) {
                HideProgressBar();
                MessageBoxAdv.Show(this, "Failed to decrypt database.\n\n" + decryptionError, "Error");
                return;
            }

            SplashScreenForm splashForm = new SplashScreenForm();

            // Hide this form so that only the splash screen is visible.
            this.Hide();

            splashForm.Show();

            // Execute startup events so that splash screen images are loaded.
            Application.DoEvents();

            // Release the encrypted database lock.
            ENCRYPTED_DB_LOCK.Close();

            // Delete the encrypted database file.
            File.Delete(DatabaseProperties.ENCRYPTED_DATABASE_NAME);

            // Create a secure string from the entered password.
            foreach (char c in passwordTextBox.Text) {
                DatabaseProperties.password.AppendChar(c);
            }

            // Create and display the main form.
            fec_Main mainForm = new fec_Main(splashForm);
            mainForm.Show();

            closed = true;
            this.Close();
        }
        #endregion

        /// <summary>
        /// Makes the progress bar visible, and the 'submit' button invisible.
        /// </summary>
        private void ShowProgressBar() {
            submitPasswordButton.Visible = false;
            progressBar.Visible = true;
        }

        /// <summary>
        /// Makes the progress bar invisible, and the 'submit' button visible.
        /// </summary>
        private void HideProgressBar() {
            progressBar.Visible = false;
            submitPasswordButton.Visible = true;
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

        /// <summary>
        /// If the form is closed by the user, exit the application. If not, set message box colors to match main UI.
        /// </summary>
        private void EnterPasswordForm_FormClosing(object sender, FormClosingEventArgs e) {

            if (!closed) {
                Application.Exit();
            }

            // Set message box colors to a blue color.
            MessageBoxAdv.MetroColorTable.BorderColor = System.Drawing.Color.DeepSkyBlue;
            MessageBoxAdv.MetroColorTable.YesButtonBackColor = System.Drawing.Color.DeepSkyBlue;
            MessageBoxAdv.MetroColorTable.NoButtonBackColor = System.Drawing.Color.DeepSkyBlue;
            MessageBoxAdv.MetroColorTable.OKButtonBackColor = System.Drawing.Color.DeepSkyBlue;
        }
    }
}
