using Syncfusion.Windows.Forms;
using Syncfusion.Windows.Forms.Grid.Grouping;
using System;
using System.Data;
using System.Data.SQLite;
using System.Windows.Forms;

namespace fec {

    public partial class TableResetForm : MetroForm {

        // Holds a reference to the database grid.
        private GridGroupingControl databaseGrid;

        // Holds a reference to the DataSet underlying the database grid.
        private DataSet database;

        // Used to refill tables after reset.
        private readonly SQLiteDataAdapter employeeScheduleTableAdapter = new SQLiteDataAdapter("SELECT * FROM [Employee Schedule]", Queries.connection);
        private readonly SQLiteDataAdapter customerAttendanceTableAdapter = new SQLiteDataAdapter("SELECT * FROM [Customer Attendance]", Queries.connection);

        // Used to control form closing.
        bool allowClose = true;

        public TableResetForm(ref GridGroupingControl dbGrid, ref DataSet db) {
            InitializeComponent();

            databaseGrid = dbGrid;
            database = db;

            // Set a default value for the tableComboBox
            tableComboBox.SelectedIndex = 0;

            // Set message box colors to purple.
            MessageBoxAdv.MetroColorTable.BorderColor = System.Drawing.Color.Purple;
            MessageBoxAdv.MetroColorTable.YesButtonBackColor = System.Drawing.Color.Purple;
            MessageBoxAdv.MetroColorTable.NoButtonBackColor = System.Drawing.Color.Purple;
            MessageBoxAdv.MetroColorTable.OKButtonBackColor = System.Drawing.Color.Purple;
        }

        /// <summary>
        /// Reset the values of the selected database table.
        /// </summary>
        private void ResetButton_Click(object sender, System.EventArgs e) {

            if (tableComboBox.SelectedIndex != 0) {

                // Do not allow the form to be closed.
                allowClose = false;

                string confirmMessage = "";

                // Set a confirmation message according to the selected table.
                switch (tableComboBox.Text) {
                    case "Employee Schedule":
                        confirmMessage = "This will reset the values of all employee schedules to \"OFF\".\nProceed?";
                        break;
                    case "Customer Attendance":
                        confirmMessage = "This will reset the values of all customer attendances to \"N/A\".\nProceed?";
                        break;
                }

                try {
                    if (GetConfirmation(confirmMessage)) {
                        switch (tableComboBox.Text) {
                            case "Employee Schedule":
                                DatabaseWorker.ResetEmployeeSchedule();
                                break;
                            case "Customer Attendance":
                                DatabaseWorker.ResetCustomerAttendance();
                                break;
                        }

                        // Refresh the reset table.
                        ReloadTable();
                        MessageBoxAdv.Show(this, "Table was successfully reset.", "Success.");
                    }
                }
                catch (Exception) {
                    MessageBoxAdv.Show(this, "Table reset failed.", "Failed.");
                }             
            }
            else {
                MessageBoxAdv.Show(this, "Please select a table.", "Error.");

                allowClose = false;
            }
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
        /// Prevents closing the form if an operation is in progress.
        /// Reset message box colors if the form is allowed to close.
        /// </summary>
        private void TableResetForm_FormClosing(object sender, FormClosingEventArgs e) {
            if (!allowClose) {
                // Abort closing the form.
                e.Cancel = true;            
            }

            if (allowClose) {
                // Revert message box colors to blue.
                MessageBoxAdv.MetroColorTable.BorderColor = System.Drawing.Color.DeepSkyBlue;
                MessageBoxAdv.MetroColorTable.YesButtonBackColor = System.Drawing.Color.DeepSkyBlue;
                MessageBoxAdv.MetroColorTable.NoButtonBackColor = System.Drawing.Color.DeepSkyBlue;
                MessageBoxAdv.MetroColorTable.OKButtonBackColor = System.Drawing.Color.DeepSkyBlue;

                // Dispose of the adapters so that any streams to the database are released.
                employeeScheduleTableAdapter.Dispose();
                customerAttendanceTableAdapter.Dispose();
            }

            allowClose = true;
        }
        
        /// <summary>
        /// Reload the table with the reset values.
        /// </summary>
        private void ReloadTable() {
            // Clear the table's current values.
            database.Tables[tableComboBox.Text].Clear();
            
            // Fill the table.
            if (tableComboBox.Text == "Employee Schedule") {
                employeeScheduleTableAdapter.Fill(database, "Employee Schedule");
            }
            else {
                customerAttendanceTableAdapter.Fill(database, "Customer Attendance");
            }

            // If the table's values were not already cleared, accept the changes and reload.
            if (database.HasChanges()) {
                database.Tables[tableComboBox.Text].AcceptChanges();
            }
        }
    }
}
