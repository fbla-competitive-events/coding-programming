using Syncfusion.Windows.Forms;
using System.Windows.Forms;
using System;


namespace fec {

    public partial class SettingsForm : MetroForm {

        // Used to control form closing.
        bool allowClose = true;

        public SettingsForm() {
            InitializeComponent();

            // Set message box colors to an orange color.
            MessageBoxAdv.MetroColorTable.BorderColor = System.Drawing.Color.Coral;
            MessageBoxAdv.MetroColorTable.YesButtonBackColor = System.Drawing.Color.Coral;
            MessageBoxAdv.MetroColorTable.NoButtonBackColor = System.Drawing.Color.Coral;
            MessageBoxAdv.MetroColorTable.OKButtonBackColor = System.Drawing.Color.Coral;
        }

        /// <summary>
        /// Compact the database.
        /// </summary>
        private void CompactDatabaseButton_Click(object sender, EventArgs e) {

            if (GetConfirmation("This process will restructure the database and could take some time \n"
                              + " if the database is large.\n" + " Proceed?")) {
                try {
                    // Do not allow the form to be closed.
                    allowClose = false;

                    // Show progress bar.
                    progressBar.Visible = true;

                    DatabaseWorker.CompactDatabase();

                    // Hide progress bar.
                    progressBar.Visible = false;

                    // Allow the form to be closed.
                    allowClose = true;

                    MessageBoxAdv.Show(this, "Successfully compacted the database.", "Success");
                }
                catch (Exception) {
                    MessageBoxAdv.Show(this, "Failed to compact the database.", "Failed");

                    // Hide progress bar.
                    progressBar.Visible = false;

                    // Allow the form to be closed.
                    allowClose = true;
                }
            }
        }

        /// <summary>
        /// Prevents closing the form if an operation is in progress.
        /// Reset message box colors if the form is allowed to close.
        /// </summary>
        private void SettingsForm_FormClosing(object sender, FormClosingEventArgs e) {
            if (!allowClose) {
                // Abort closing the form.
                e.Cancel = true;
            }

            // Revert message box colors to blue.
            MessageBoxAdv.MetroColorTable.BorderColor = System.Drawing.Color.DeepSkyBlue;
            MessageBoxAdv.MetroColorTable.YesButtonBackColor = System.Drawing.Color.DeepSkyBlue;
            MessageBoxAdv.MetroColorTable.NoButtonBackColor = System.Drawing.Color.DeepSkyBlue;
            MessageBoxAdv.MetroColorTable.OKButtonBackColor = System.Drawing.Color.DeepSkyBlue;
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
        /// Open the database security form.
        /// </summary>
        private void OpenDatabaseSecurityButton_Click(object sender, EventArgs e) {
            DatabaseSecurityForm databaseSecurityForm = new DatabaseSecurityForm();
            databaseSecurityForm.ShowDialog(this);
        }
    }
}
