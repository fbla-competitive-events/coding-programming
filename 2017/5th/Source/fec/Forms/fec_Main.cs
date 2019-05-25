using Syncfusion.Grouping;
using Syncfusion.Windows.Forms;
using Syncfusion.Windows.Forms.Grid;
using Syncfusion.Windows.Forms.Grid.Grouping;
using System;
using System.Collections.Specialized;
using System.Data;
using System.Data.SQLite;
using System.Drawing.Printing;
using System.Drawing;
using System.IO;
using System.Threading;
using System.Windows.Forms;

namespace fec {

    public partial class fec_Main : MetroForm {

        // The object containing the database values, which acts as the foundation of the grid.
        private static DataSet database = new DataSet();

        // Used to prevent the database file from being deleted while the application is running.
        private readonly FileStream DB_LOCK;

        // Searching Variables. Used to add and remove table filters.
        private RecordFilterDescriptor filterEmployees = new RecordFilterDescriptor();
        private RecordFilterDescriptor filterEmployeeSchedule = new RecordFilterDescriptor();
        private RecordFilterDescriptor filterCustomers = new RecordFilterDescriptor();
        private RecordFilterDescriptor filterCustomerAttendance = new RecordFilterDescriptor();

        // Used for searching. These are the values displayed in the search category combo box.
        private readonly string[] allValues = { "Id", "Name" };
        private readonly string[] employeeValues = { "Id", "Name", "Job", "Address", "Phone" };
        private readonly string[] customerValues = { "Id", "Name", "Phone" };

        // Used for printing.
        private GridPrinter printer;
        private string printingTableName;
        private bool firstPage = true;

        // Used for multi-threading. Holds the number of database tasks running.
        private int jobCount = 0;

        // Used for UI resizing.
        private Size oldSize;
        private bool maximized = false;

        // Used to control application exitting.
        private bool exitting = false;

        /// <summary>
        /// The main form constructor. Called when the application is opened.
        /// Creates a new database if one does not already exist, and setups the
        /// database grid and other UI components.
        /// </summary>
        public fec_Main(SplashScreenForm splashForm) {
            InitializeComponent();

            // Set the form icon.
            this.Icon = Properties.Resources.fecIcon;

            // Set the minimum size of the form.
            this.MinimumSize = new Size(820, 561);
            oldSize = this.MinimumSize;

            // Set global message box style.
            MessageBoxAdv.MessageBoxStyle = MessageBoxAdv.Style.Metro;

            // Open a stream to the database file to prevent it from being deleted.
            DB_LOCK = File.Open(DatabaseProperties.DATABASE_NAME, FileMode.Open, FileAccess.Read, FileShare.ReadWrite);

            // Verify that the database is not corrupted.
            try {
                if (DB_LOCK.Length == 0) {
                    throw new Exception();
                }

                DatabaseWorker.CheckIntegrity();
            }
            catch (Exception) {            
                // Display error message and force-exit application when message is dismissed.
                if (MessageBoxAdv.Show(this, "Unable to open database file.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error) == DialogResult.OK) {
                    DB_LOCK.Close(); 
                    System.Diagnostics.Process.GetCurrentProcess().Kill();
                }
            }

            // Setup and format the grid.
            SetupDatabaseGrid();

            // Setup some UI components and bind some events.
            SetupComponents();

            splashForm.Close();
        }


        /// <summary>
        /// Setup the database DataSet, populate the grid with database data, and format it.
        /// </summary>
        private void SetupDatabaseGrid() {

            #region Data Source Setup
            // Used to retrieve data from the database and to populate the grid.
            SQLiteDataAdapter employeeTableAdapter = new SQLiteDataAdapter("SELECT * FROM Employees", Queries.connection);
            SQLiteDataAdapter employeeScheduleTableAdapter = new SQLiteDataAdapter("SELECT * FROM [Employee Schedule]", Queries.connection);
            SQLiteDataAdapter customersTableAdapter = new SQLiteDataAdapter("SELECT * FROM Customers", Queries.connection);
            SQLiteDataAdapter customerAttendanceTableAdapter = new SQLiteDataAdapter("SELECT * FROM [Customer Attendance]", Queries.connection);

            // Setup the DataSet and add tables to it in order to hold corresponding database values.
            database.DataSetName = "Database";
            database.Tables.Add(new DataTable("Employees"));
            database.Tables.Add(new DataTable("Employee Schedule"));
            database.Tables.Add(new DataTable("Customers"));
            database.Tables.Add(new DataTable("Customer Attendance"));

            try {
                // Populate the DataSet using the SQL adapters.
                employeeTableAdapter.Fill(database, "Employees");
                employeeScheduleTableAdapter.Fill(database, "Employee Schedule");
                customersTableAdapter.Fill(database, "Customers");
                customerAttendanceTableAdapter.Fill(database, "Customer Attendance");
            }
            catch (Exception) {
                // Display error message and force-exit application when message is dismissed.
                if (MessageBoxAdv.Show(this, "Unable to open database file.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error) == DialogResult.OK) {
                    DB_LOCK.Close();
                    System.Diagnostics.Process.GetCurrentProcess().Kill();
                }
            }

            // Set the data source of the grid to the DataSet.
            databaseGrid.DataSource = database;
            #endregion

            #region Grid Combo Box Formatting
            // Holds the combo box values of the employee schedule table.
            StringCollection employeeScheduleValues = new StringCollection()
                                                    { "AM/PM", "AM", "PM", "OFF" };

            // Holds the combo box values of the customer attendance table.
            StringCollection customerAttendanceValues = new StringCollection()
                                                      { "AM", "PM", "AM/PM", "N/A" };

            // Holds the combo box values of the customer membership entry in the Customer table.
            StringCollection customerMembershipValues = new StringCollection()
                                                        { "N/A", "SMILE PLUS" };


            // Format the employee schedule table and add combo boxes.
            for (int i = 2; i < databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns.Count; i++) {
                GridTableCellStyleInfo styleEmployeeSchedule = databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell;

                styleEmployeeSchedule.DropDownStyle = GridDropDownStyle.Exclusive;
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell.CellType = GridCellTypeName.ComboBox;
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell.ChoiceList = employeeScheduleValues;
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell.CellValue = "CHOICES";
            }

            // Format the customer attendance table and add combo boxes.
            for (int i = 2; i < databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns.Count; i++) {
                GridTableCellStyleInfo customerAttendance = databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell;

                customerAttendance.DropDownStyle = GridDropDownStyle.Exclusive;
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell.CellType = GridCellTypeName.ComboBox;
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell.ChoiceList = customerAttendanceValues;
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell.CellValue = "CHOICES";
            }

            // Format the Customer table and add membership combo boxes.
            GridTableCellStyleInfo customers = databaseGrid.GetTable("Customers").TableDescriptor.Columns["Membership"].Appearance.AnyRecordFieldCell;
            customers.DropDownStyle = GridDropDownStyle.Exclusive;
            databaseGrid.GetTable("Customers").TableDescriptor.Columns["Membership"].Appearance.AnyRecordFieldCell.CellType = GridCellTypeName.ComboBox;
            databaseGrid.GetTable("Customers").TableDescriptor.Columns["Membership"].Appearance.AnyRecordFieldCell.ChoiceList = customerMembershipValues;
            databaseGrid.GetTable("Customers").TableDescriptor.Columns["Membership"].Appearance.AnyRecordFieldCell.CellValue = "CHOICES";
            #endregion

            #region Alignment
            // Set alignment of id fields.
            databaseGrid.GetTable("Employees").TableDescriptor.Columns["Id"].Appearance.AnyRecordFieldCell.HorizontalAlignment = GridHorizontalAlignment.Center;
            databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns["Id"].Appearance.AnyRecordFieldCell.HorizontalAlignment = GridHorizontalAlignment.Center;
            databaseGrid.GetTable("Customers").TableDescriptor.Columns["Id"].Appearance.AnyRecordFieldCell.HorizontalAlignment = GridHorizontalAlignment.Center;
            databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns["Id"].Appearance.AnyRecordFieldCell.HorizontalAlignment = GridHorizontalAlignment.Center;

            // Set alignment of phone fields.
            databaseGrid.GetTable("Employees").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.HorizontalAlignment = GridHorizontalAlignment.Left;
            databaseGrid.GetTable("Customers").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.HorizontalAlignment = GridHorizontalAlignment.Left;
            #endregion

            #region Readonly & Input Limitation Settings
            // Make the 'Id' and 'Name' cells readonly so as not to allow editing outside of appropriate tables.
            // This way, employee and customer names can only be edited from the 'Employees' and 'Customers' table respectively, 
            // while ids cannot be edited at all.
            databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns["Id"].Appearance.AnyRecordFieldCell.ReadOnly = true;
            databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns["Name"].Appearance.AnyRecordFieldCell.ReadOnly = true;
            databaseGrid.GetTable("Employees").TableDescriptor.Columns["Id"].Appearance.AnyRecordFieldCell.ReadOnly = true;

            databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns["Id"].Appearance.AnyRecordFieldCell.ReadOnly = true;
            databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns["Name"].Appearance.AnyRecordFieldCell.ReadOnly = true;
            databaseGrid.GetTable("Customers").TableDescriptor.Columns["Id"].Appearance.AnyRecordFieldCell.ReadOnly = true;

            // Limit the input length of 'Phone' column cells.
            databaseGrid.GetTable("Employees").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.MaxLength = 12;
            databaseGrid.GetTable("Customers").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.MaxLength = 12;
            #endregion

            #region Phone Mask
            // Set employee phone field mask.
            databaseGrid.GetTable("Employees").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.CellType = GridCellTypeName.MaskEdit;
            databaseGrid.GetTable("Employees").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.MaskEdit.Mask = "(999)-999-9999";
            databaseGrid.GetTable("Employees").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.MaskEdit.ClipMode = Syncfusion.Windows.Forms.Tools.ClipModes.ExcludeLiterals;

            // Set customer phone field mask.
            databaseGrid.GetTable("Customers").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.CellType = GridCellTypeName.MaskEdit;
            databaseGrid.GetTable("Customers").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.MaskEdit.Mask = "(999)-999-9999";
            databaseGrid.GetTable("Customers").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.MaskEdit.ClipMode = Syncfusion.Windows.Forms.Tools.ClipModes.ExcludeLiterals;
            #endregion

            #region Masking Value Type Overriding
            // Override employee phone field value type to allow masking.
            databaseGrid.GetTable("Employees").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.CellValueType = typeof(String);

            // Override customer phone field value type to allow masking.
            databaseGrid.GetTable("Customers").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.CellValueType = typeof(String);
            #endregion

            // Format the column header text to be bold.
            databaseGrid.Appearance.ColumnHeaderCell.Font.Bold = true;

            ResizeGridColumns();
        }

        /// <summary>
        /// Adjust column width in proportion to the grid width.
        /// Should be called whenever the grid is resized.
        /// </summary>
        private void ResizeGridColumns() {
            int gridWidth = databaseGrid.Width;
            int columnCount;

            // Resize the columns of the 'Employees' table
            columnCount = databaseGrid.GetTable("Employees").TableDescriptor.Columns.Count;
            for (int i = 1; i < columnCount; i++) {
                databaseGrid.GetTable("Employees").TableDescriptor.Columns[i].Width = gridWidth / columnCount;
            }

            // Resize the columns of the 'Employee Schedule' table
            columnCount = databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns.Count;
            for (int i = 1; i < columnCount; i++) {
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns[i].Width = gridWidth / columnCount;
            }

            // Resize the columns of the 'Customers' table
            columnCount = databaseGrid.GetTable("Customers").TableDescriptor.Columns.Count;
            for (int i = 1; i < columnCount; i++) {
                databaseGrid.GetTable("Customers").TableDescriptor.Columns[i].Width = gridWidth / columnCount;
            }

            // Resize the columns of the 'Customer Attendance' table
            columnCount = databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns.Count;
            for (int i = 1; i < columnCount; i++) {
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns[i].Width = gridWidth / columnCount;
            }

        }

        /// <summary>
        /// When the "Add Employee" button is clicked, prompt the user to enter the information of the new
        /// employee, and then add the values to the database. Update the grid after the employee was added.
        /// </summary>
        private void AddEmployeeButton_Click(object sender, EventArgs e) {

            // Create and display an "Add Employee" form for the user to enter employee information.
            // This form will also insert the new entries in the database.
            AddEmployeeForm prompt = new AddEmployeeForm();
            prompt.ShowDialog();

            // If the form was fully filled in and the entry was successfully inserted in the database, 
            // create new rows and update the grid.
            if (prompt.DialogResult == DialogResult.OK) {

                // Hold the employee's basic information and schedule values that are retrieved from the database.
                string[] employeeData = DatabaseWorker.GetEmployeeEntry(DatabaseWorker.tempId);
                string[] scheduleData = DatabaseWorker.GetEmployeeScheduleEntry(DatabaseWorker.tempId);

                // Create new rows to hold the information and schedule of the new employee.
                DataRow employeeEntry = database.Tables["Employees"].NewRow();
                DataRow employeeScheduleEntry = database.Tables["Employee Schedule"].NewRow();

                // Populate each column of the new Employee and Employe Schedule rows with data from the database.
                for (int i = 0; i < database.Tables["Employees"].Columns.Count; i++) {
                    employeeEntry[i] = employeeData[i];
                }

                for (int i = 0; i < database.Tables["Employee Schedule"].Columns.Count; i++) {
                    employeeScheduleEntry[i] = scheduleData[i];
                }

                // Add the newly created Employee and Employee Schedule rows to the DataSet.
                database.Tables["Employees"].Rows.Add(employeeEntry);
                database.Tables["Employee Schedule"].Rows.Add(employeeScheduleEntry);

            }
        }

        /// <summary>
        /// When the "Add Customer" button is clicked, prompt the user to enter the information of the new
        /// customer, and then add the values to the database. Update the grid after the customer was added.
        /// </summary>
        private void AddCustomerButton_Click(object sender, EventArgs e) {

            // Create and display an "Add Customer" form for the user to enter customer information.
            // This form will also insert the new entries to the database.
            AddCustomerForm prompt = new AddCustomerForm();
            prompt.ShowDialog();

            // If the form was fully filled in and the entry was successfully inserted in the database, 
            // create new rows and update the grid.
            if (prompt.DialogResult == DialogResult.OK) {

                // Hold the customer's basic information and attendance values that are retrieved from the database.
                string[] customerData = DatabaseWorker.GetCustomerEntry(DatabaseWorker.tempId);
                string[] customerAttendanceData = DatabaseWorker.GetCustomerAttendanceEntry(DatabaseWorker.tempId);

                // Create new rows to hold the information and attendance of the new customer.
                DataRow customerEntry = database.Tables["Customers"].NewRow();
                DataRow customerAttendanceEntry = database.Tables["Customer Attendance"].NewRow();

                // Populate each column of the new Customer and Customer Attendance rows with data from the database.
                for (int i = 0; i < database.Tables["Customers"].Columns.Count; i++) {
                    customerEntry[i] = customerData[i];
                }

                for (int i = 0; i < database.Tables["Customer Attendance"].Columns.Count; i++) {
                    customerAttendanceEntry[i] = customerAttendanceData[i];
                }

                // Add the newly created Customer and Customer Attendance rows to the DataSet.
                database.Tables["Customers"].Rows.Add(customerEntry);
                database.Tables["Customer Attendance"].Rows.Add(customerAttendanceEntry);

            }
        }

        /// <summary>
        /// When the "Remove Entry" button is clicked, remove the selected employee and customer entries,
        /// along with the related schedule and attendance entries respectively, from the database and from the
        /// DataSet. Update the grid to reflect these changes.
        /// </summary>
        private void RemoveButton_Click(object sender, EventArgs e) {
            try {
                // Get selected row.
                GridRangeInfo info = databaseGrid.TableModel.Selections.GetSelectedRows(true, true)[0];

                // Get an element fromt the row.
                Element element = databaseGrid.TableModel.GetDisplayElementAt(info.Top);

                // Get the table the element belongs to.
                GridNestedTable gnt = element as GridNestedTable;

                // Get the data of the row.
                DataRowView drv = gnt.ChildTable.ParentTable.CurrentElement.GetData() as DataRowView;

                // Get the name of the table the row belongs to.
                string table = gnt.ChildTable.Name;


                if (table == "Employees") {

                    // Remove the employee and corresponding employee schedule entry from the database.
                    DatabaseWorker.RemoveEmployee(Convert.ToInt32(drv[0]));

                    // Get the grid employee entry.
                    Record record = gnt.ChildTable.ParentTable.CurrentElement.GetRecord();

                    // Get the index of the employee entry.
                    // This index is the same as the index of the corresponding employee schedule entry.
                    int index = databaseGrid.GetTable("Employees").UnsortedRecords.IndexOf(record);

                    // Delete the corresponding employee schedule entry from the grid.
                    databaseGrid.GetTable("Employee Schedule").UnsortedRecords[index].Delete();

                    // Delete the employee entry from the grid.
                    record.Delete();

                    // Delete the employee and employee schedule entries from the DataSet.
                    database.Tables["Employees"].Rows[index].Delete();
                    database.Tables["Employee Schedule"].Rows[index].Delete();

                    // Commit changes to the DataSet.
                    database.AcceptChanges();

                }
                else if (table == "Customers") {

                    // Remove the customer and corresponding customer attendance entry from the database.
                    DatabaseWorker.RemoveCustomer(Convert.ToInt32(drv[0]));

                    // Get the grid customer entry.
                    Record record = gnt.ChildTable.ParentTable.CurrentElement.GetRecord();

                    // Get the index of the customer entry.
                    // This index is the same as the index of the corresponding customer attendance entry.
                    int index = databaseGrid.GetTable("Customers").UnsortedRecords.IndexOf(record);

                    // Delete the corresponding customer attendance entry from the grid.
                    databaseGrid.GetTable("Customer Attendance").UnsortedRecords[index].Delete();

                    // Delete the customer entry from the grid.
                    record.Delete();

                    // Delete the customer and customer attendance entries from the DataSet.
                    database.Tables["Customers"].Rows[index].Delete();
                    database.Tables["Customer Attendance"].Rows[index].Delete();

                    // Commit changes to the DataSet.
                    database.AcceptChanges();
                }
                // If the table is neither of the above, display the appropriate error message.
                else if (table == "Employee Schedule") {
                    MessageBoxAdv.Show(this, "Cannot remove employee schedule entry directly. \n"
                                       + "To remove it, delete the corresponding employee entry.", "Error");
                }
                else if (table == "Customer Attendance") {
                    MessageBoxAdv.Show(this, "Cannot remove customer attendance entry directly. \n"
                                       + "To remove it, delete the corresponding customer entry.", "Error");
                }
            }
            catch (Exception) { }
        }

        /// <summary>
        /// Whenever the value of a cell is edited, update the database entry and the corresponding DataSet value.
        /// Update the grid to reflect these changes.
        /// </summary>
        private void DatabaseGrid_RecordValueChanged(object sender, RecordValueChangedEventArgs e) {

            try {
                // Object that holds the values of the record that the edited cell belongs to.
                DataRowView drv = e.Record.GetData() as DataRowView;

                // If the cell's record belongs to the Employee table.
                if (e.Record.ParentChildTable.Name == "Employees") {

                    // Queue a 'Edit Employee' task to the Thread pool.
                    ThreadPool.QueueUserWorkItem(state => {

                        // Increments the job counter in a thread-safe manner.
                        Interlocked.Increment(ref jobCount);

                        string[] updatedValues = { drv[1].ToString(), drv[2].ToString(), drv[3].ToString(), drv[4].ToString() };

                        // Edit the database employee entry.
                        DatabaseWorker.EditEmployee(Convert.ToInt32(drv[0]), updatedValues[0],
                        updatedValues[1], updatedValues[2],
                        updatedValues[3]);

                        // Get the index of the grid entry.
                        int index = databaseGrid.GetTable("Employees").UnsortedRecords.IndexOf(e.Record);

                        // Set the 'Name' field in the employee schedule entry that has the same index.
                        databaseGrid.GetTable("Employee Schedule").UnsortedRecords[index].SetValue("Name", drv[1].ToString());

                        // Decrements the job counter in a thread-safe manner.
                        Interlocked.Decrement(ref jobCount);
                    }
                    );
                }

                // If the cell's record belongs to the Employee Schedule table
                else if (e.Record.ParentChildTable.Name == "Employee Schedule") {
                    string[] updatedScheduleValues = new string[7];

                    // Get updated schedule values.
                    for (int i = 2; i < 9; i++) {
                        updatedScheduleValues[i - 2] = drv[i].ToString();
                    }

                    // Queue a 'Edit Employee Schedule' task to the thread pool.
                    ThreadPool.QueueUserWorkItem(state => {

                        // Increments the job counter in a thread-safe manner.
                        Interlocked.Increment(ref jobCount);

                        DatabaseWorker.EditEmployeeSchedule(Convert.ToInt32(drv[0]), updatedScheduleValues);

                        // Decrements the job counter in a thread-safe manner.
                        Interlocked.Decrement(ref jobCount);
                    }
                    );
                }

                // If the cell's record belongs to the Customer table
                else if (e.Record.ParentChildTable.Name == "Customers") {

                    // Queue a 'Edit Customer' task to the thread pool.
                    ThreadPool.QueueUserWorkItem(state => {

                        // Increments the job counter in a thread-safe manner.
                        Interlocked.Increment(ref jobCount);

                        string[] updatedValues = { drv[1].ToString(), drv[2].ToString(), drv[3].ToString() };

                        // Edit the customer database entry.
                        DatabaseWorker.EditCustomer(Convert.ToInt32(drv[0]), updatedValues[0], updatedValues[1], updatedValues[2]);

                        // Get the index of the grid entry.
                        int index = databaseGrid.GetTable("Customers").UnsortedRecords.IndexOf(e.Record);

                        // Set the 'Name' field in the customer attendance entry that has the same index.
                        databaseGrid.GetTable("Customer Attendance").UnsortedRecords[index].SetValue("Name", drv[1].ToString());

                        // Decrements the job counter in a thread-safe manner.
                        Interlocked.Decrement(ref jobCount);
                    }
                    );
                }

                // If the cell's record belongs to the Customer Attendance table.
                else if (e.Record.ParentChildTable.Name == "Customer Attendance") {
                    string[] updatedAttendanceValues = new string[7];

                    for (int i = 2; i < 9; i++) {
                        updatedAttendanceValues[i - 2] = drv[i].ToString();
                    }

                    // Queue a 'Edit Customer Attendance' task to the thread pool
                    ThreadPool.QueueUserWorkItem(state => {

                        // Increments the job counter in a thread-safe manner.
                        Interlocked.Increment(ref jobCount);

                        DatabaseWorker.EditCustomerAttendance(Convert.ToInt32(drv[0]), updatedAttendanceValues);

                        // Decrements the job counter in a thread-safe manner.
                        Interlocked.Decrement(ref jobCount);
                    }
                    );
                }
            }
            catch (Exception) {
                MessageBoxAdv.Show(this, "Error editing value.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        #region Searching
        /// <summary>
        /// Filter table values based on search input.
        /// </summary>
        private void SearchButton_Click(object sender, EventArgs e) {

            // If the search text box is empty, stop the event.
            if (searchTextBox.Text == "") {
                return;
            }

            string filterColumn = "";

            // Clear current filters.
            databaseGrid.GetTable("Employees").TableDescriptor.RecordFilters.Remove(filterEmployees);
            databaseGrid.GetTable("Employee Schedule").TableDescriptor.RecordFilters.Remove(filterEmployeeSchedule);
            databaseGrid.GetTable("Customers").TableDescriptor.RecordFilters.Remove(filterCustomers);
            databaseGrid.GetTable("Customer Attendance").TableDescriptor.RecordFilters.Remove(filterCustomerAttendance);

            // If the search range is 'all tables'
            if (searchRangeComboBox.SelectedIndex == 0) {

                // Get the column to search in.
                switch (searchComboBox.SelectedIndex) {
                    case 0:
                        filterColumn = "[Id]";
                        break;
                    case 1:
                        filterColumn = "[Name]";
                        break;
                }

                // Set the filter expressions.
                filterEmployees.Expression = filterColumn + "match '" + searchTextBox.Text + "'";
                filterEmployeeSchedule.Expression = filterColumn + "match '" + searchTextBox.Text + "'";
                filterCustomers.Expression = filterColumn + "match '" + searchTextBox.Text + "'";
                filterCustomerAttendance.Expression = filterColumn + "match '" + searchTextBox.Text + "'";

                // Apply the filters.
                databaseGrid.GetTable("Employees").TableDescriptor.RecordFilters.Add(filterEmployees);
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.RecordFilters.Add(filterEmployeeSchedule);
                databaseGrid.GetTable("Customers").TableDescriptor.RecordFilters.Add(filterCustomers);
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.RecordFilters.Add(filterCustomerAttendance);
            }

            // If the search range is the 'Employees' table
            else if (searchRangeComboBox.SelectedIndex == 1) {

                // Get the column to search in.
                switch (searchComboBox.SelectedIndex) {
                    case 0:
                        filterColumn = "[Id]";
                        break;
                    case 1:
                        filterColumn = "[Name]";
                        break;
                    case 2:
                        filterColumn = "[Job]";
                        break;
                    case 3:
                        filterColumn = "[Address]";
                        break;
                    case 4:
                        filterColumn = "[Phone]";
                        break;
                }

                // Set the filter expressions.
                filterEmployees.Expression = filterColumn + "match '" + searchTextBox.Text + "'";
                filterEmployeeSchedule.Expression = filterColumn + "match '" + searchTextBox.Text + "'";
                
                // Apply the filters.
                databaseGrid.GetTable("Employees").TableDescriptor.RecordFilters.Add(filterEmployees);
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.RecordFilters.Add(filterEmployeeSchedule);
            }

            // If the search range is the 'Customers' table
            else if (searchRangeComboBox.SelectedIndex == 2) {

                // Get the column to search in.
                switch (searchComboBox.SelectedIndex) {
                    case 0:
                        filterColumn = "[Id]";
                        break;
                    case 1:
                        filterColumn = "[Name]";
                        break;
                    case 2:
                        filterColumn = "[Phone]";
                        break;
                }

                // Set the filter expressions.
                filterCustomers.Expression = filterColumn + "match '" + searchTextBox.Text + "'";
                filterCustomerAttendance.Expression = filterColumn + "match '" + searchTextBox.Text + "'";

                // Apply the filters.
                databaseGrid.GetTable("Customers").TableDescriptor.RecordFilters.Add(filterCustomers);
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.RecordFilters.Add(filterCustomerAttendance);
            }
        }

        /// <summary>
        /// If the search text box is empty, clear all table search filters.
        /// </summary>
        private void SearchTextBox_TextChanged(object sender, EventArgs e) {
            if (searchTextBox.Text == string.Empty) {

                // Clear all filters.
                databaseGrid.GetTable("Employees").TableDescriptor.RecordFilters.Remove(filterEmployees);
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.RecordFilters.Remove(filterEmployeeSchedule);
                databaseGrid.GetTable("Customers").TableDescriptor.RecordFilters.Remove(filterCustomers);
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.RecordFilters.Remove(filterCustomerAttendance);
            }
        }

        /// <summary>
        /// Change values of the search category combo box according to the table range.
        /// </summary>
        private void SearchCategoryComboBox_SelectedIndexChanged(object sender, EventArgs e) {

            // If the range is all tables.
            if (searchRangeComboBox.SelectedIndex == 0) {
                searchComboBox.Items.Clear();
                searchComboBox.Items.AddRange(allValues);
            }

            // If the range is the 'Employees' table.
            else if (searchRangeComboBox.SelectedIndex == 1) {
                searchComboBox.Items.Clear();
                searchComboBox.Items.AddRange(employeeValues);
            }

            // If the range is the 'Customers' table.
            else if (searchRangeComboBox.SelectedIndex == 2) {
                searchComboBox.Items.Clear();
                searchComboBox.Items.AddRange(customerValues);
            }

            // Set a default combo box value.
            searchComboBox.SelectedIndex = 1;
        }
        #endregion

        #region Printing
        /// <summary>
        /// Open the print form and if successful execute print events.
        /// </summary>
        private void PrintGridButton_Click(object sender, EventArgs e) {
            PrintForm prompt = new PrintForm();
            prompt.ShowDialog();

            if (prompt.DialogResult == DialogResult.OK) {

                // Get the selected table from the prompt.
                string selectedTable = prompt.SelectedTable;
                printingTableName = selectedTable;

                PrintDialog printDialog = new PrintDialog();
                printDialog.Document = gridDocument;
                printDialog.UseEXDialog = true;

                if (DialogResult.OK == printDialog.ShowDialog()) {

                    // Set paper margins.
                    gridDocument.DefaultPageSettings.Margins = new Margins(50, 50, 110, 50);

                    // Execute printing events.
                    gridDocument.Print();
                }
            }

        }

        #region Printing Events
        /// <summary>
        /// Printing event called for every page to be printed.
        /// </summary>
        private void GridDocument_PrintPage(object sender, PrintPageEventArgs e) {
            // If this is a new printing session, create a new GridPrinter object.
            if (firstPage) {
                printer = new GridPrinter(printingTableName, ref databaseGrid, ref database);
                firstPage = false;
            }

            // Set the current page to be printed.
            printer.SetPage(ref e);

            // Print the page.
            printer.Print();
        }

        /// <summary>
        /// When the printing events have ended, reset printing variables
        /// </summary>
        private void GridDocument_EndPrint(object sender, PrintEventArgs e) {
            firstPage = true;
            printer = null;
            printingTableName = null;

            GC.Collect();
            GC.WaitForPendingFinalizers();
        }
        #endregion

        #endregion

        /// <summary>
        /// Setup some UI controls and bind some events.
        /// </summary>
        private void SetupComponents() {

            // Bind a key press event. 
            // If the return key is pressed when typing in the search box, execute search methods.
            searchTextBox.KeyDown += (sender, args) => {
                if (args.KeyCode == Keys.Return) {
                    searchButton.PerformClick();
                }
            };

            // Override grid paste events to prevent multiple column pasting when multiple line text is pasted
            // by replacing new line characters with spaces.
            databaseGrid.GetTable("Employees").TableModel.ClipboardPaste += (sender, args) => {
                Clipboard.SetText(Clipboard.GetText().Replace(Environment.NewLine, " "));
            };
            databaseGrid.GetTable("Customers").TableModel.ClipboardPaste += (sender, args) => {
                Clipboard.SetText(Clipboard.GetText().Replace(Environment.NewLine, " "));
            };

            // Set starting items on search combo box
            searchComboBox.Items.Clear();
            searchComboBox.Items.AddRange(allValues);

            // Set default values for the search combo boxes.
            searchRangeComboBox.SelectedIndex = 0;
            searchComboBox.SelectedIndex = 1;
        }

        /// <summary>
        /// Open the settings form.
        /// </summary>
        private void SettingsButton_Click(object sender, EventArgs e) {
            SettingsForm settings = new SettingsForm();
            settings.ShowDialog();
        }

        /// <summary>
        /// Open the table reset form.
        /// </summary>
        private void TableResetButton_Click(object sender, EventArgs e) {
            TableResetForm tableReset = new TableResetForm(ref databaseGrid, ref database);
            tableReset.ShowDialog();
        }

        /// <summary>
        /// Do not allow the application to close if there are tasks running in the thread pool.
        /// Encrypt the database if it is marked for encryption before exitting the application.
        /// </summary>
        private void Fec_Main_FormClosing(object sender, FormClosingEventArgs e) {

            // Do not continue if application is exitting.
            if (exitting) {
                return;
            }

            if (jobCount > 0) {
                e.Cancel = true;

                MessageBoxAdv.Show(this, "There are database tasks running. \n"
                    + "Please wait until they are completed. \n"
                    + "If you close the application before the tasks are completed "
                    + "the database may become corrupted.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);

                return;
            }

            // If there is a value stored in the password field of the database properties, then the database is to be encrypted.
            if (DatabaseProperties.password.Length > 0) {
                EncryptingDatabaseForm encryptingDatabaseForm = new EncryptingDatabaseForm();
                encryptingDatabaseForm.Show(this);

                // Disable interaction with the main UI.
                this.Enabled = false;

                encryptionWorker.RunWorkerAsync(encryptingDatabaseForm);

                // Cancel form closing so that background worker can execute.
                e.Cancel = true;
            }
        }

        private void EncryptionWorker_DoWork(object sender, System.ComponentModel.DoWorkEventArgs e) {
            try {
                Crypto.Symmetric.EncryptDB(DatabaseProperties.DATABASE_NAME,
                                           DatabaseProperties.ENCRYPTED_DATABASE_NAME,
                                           Crypto.Utilities.SecureStringToString(DatabaseProperties.password),
                                           DatabaseProperties.PBKDF2_ITERATIONS);

            }
            catch (Exception) {
                CloseEncryptingDatabaseForm((EncryptingDatabaseForm)e.Argument);
                e.Result = e.Argument;               
                e.Cancel = true;
            }
        }

        private void EncryptionWorker_WorkCompleted(object sender, System.ComponentModel.RunWorkerCompletedEventArgs e) {

            if (e.Cancelled) {
                this.Enabled = true;

                MessageBoxAdv.Show(this, "Failed to encrypt database.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }

            // Release the database lock.
            DB_LOCK.Close();

            File.Delete(DatabaseProperties.DATABASE_NAME);

            exitting = true;
            Application.Exit();
        }

        #region CloseEncryptingDatabaseForm Method
        /// <summary>
        /// A reference to the CloseEncryptingDatabaseForm. Used for thread-safe calling of the method.
        /// </summary>
        /// <param name="form"> The EncryptingDatabaseForm to close. </param>
        private delegate void CloseEncryptingDatabaseFormDelegate(EncryptingDatabaseForm form);

        /// <summary>
        /// Close the specified EncryptingDatabaseForm in a thread-safe manner.
        /// </summary>
        /// <param name="form"> The EncryptingDatabaseForm to close. </param>
        private void CloseEncryptingDatabaseForm(EncryptingDatabaseForm form) {

            if (form.InvokeRequired) {
                // Execute delegate.
                this.Invoke(new CloseEncryptingDatabaseFormDelegate(CloseEncryptingDatabaseForm), form);
            }
            else {
                form.Close();
            }
        }
        #endregion

        /// <summary>
        /// Whenever the user finishes resizing the form, adjust the size of the grid columns.
        /// </summary>
        private void Fec_Main_ResizeEnd(object sender, EventArgs e) {
            if (oldSize != this.Size) {
                oldSize = this.Size;
                ResizeGridColumns();
            }
        }

        /// <summary>
        /// Whenever the form is maximized or unmaximized, adjust the size of the grid columns.
        /// </summary>
        private void Fec_Main_SizeChanged(object sender, EventArgs e) {

            // If the form is maximized.
            if (this.WindowState == FormWindowState.Maximized) {
                maximized = true;
                ResizeGridColumns();
            }
            // If the form is unmaximized.
            else if (maximized && this.WindowState != FormWindowState.Maximized) {
                maximized = false;
                ResizeGridColumns();
            }
        }

        /// <summary>
        /// Exit the application when the form is closed. Used to close the application if this form is not bound as the primary form.
        /// </summary>
        private void Fec_Main_FormClosed(object sender, FormClosedEventArgs e) {
            Application.Exit();
        }
    }
}
