using Syncfusion.Grouping;
using Syncfusion.Windows.Forms;
using Syncfusion.Windows.Forms.Grid;
using Syncfusion.Windows.Forms.Grid.Grouping;
using System;
using System.Collections.Specialized;
using System.Data;
using System.Data.SQLite;
using System.Drawing.Printing;
using System.IO;
using System.Threading;
using System.Windows.Forms;

namespace fec {

    public partial class fec_Main : MetroForm {

        // The object containing the database values which acts as the foundation of the grid.
        public static DataSet database = new DataSet();

        // Searching Variables. Used to add and remove table filters.
        RecordFilterDescriptor filterEmployees = new RecordFilterDescriptor();
        RecordFilterDescriptor filterEmployeeSchedule = new RecordFilterDescriptor();
        RecordFilterDescriptor filterCustomers = new RecordFilterDescriptor();
        RecordFilterDescriptor filterCustomerAttendance = new RecordFilterDescriptor();

        // Used for searching. These are the values displayed in the search category combo box.
        string[] allValues = { "Id", "Name" };
        string[] employeeValues = { "Id", "Name", "Job", "Address", "Phone" };
        string[] customerValues = { "Id", "Name", "Phone" };

        // Used for printing.
        string tableName;

        /// <summary>
        /// The main form constructor. Initializes UI objects and binds events.
        /// </summary>
        public fec_Main(SplashScreenForm splashForm) {
            InitializeComponent();
            this.Icon = Properties.Resources.fecIcon;

            SetupComponents();

            // If a database file does not exist, create a new one with empty tables.
            if (!File.Exists(DatabaseGenerator.DATABASE_NAME)) {
                DatabaseGenerator.GenerateDB();
            }

            // Setup and format the grid.
            SetupDatabaseGrid();

            splashForm.Close();
        }

        /// <summary>
        /// Setup the database DataSet, populate the grid with database data, and format it.
        /// </summary>
        private void SetupDatabaseGrid() {

            // Used to retrieve data from the database and to populate the grid.
            SQLiteDataAdapter employeeTableAdapter = new SQLiteDataAdapter("SELECT * FROM Employees", Queries.connection);
            SQLiteDataAdapter employeeScheduleTableAdapter = new SQLiteDataAdapter("SELECT * FROM [Employee Schedule]", Queries.connection);
            SQLiteDataAdapter customersTableAdapter = new SQLiteDataAdapter("SELECT * FROM Customers", Queries.connection);
            SQLiteDataAdapter customerAttendanceTableAdapter = new SQLiteDataAdapter("SELECT * FROM [Customer Attendance]", Queries.connection);

            // Setup the DataSet oject and add tables to it in order to hold corresponding database values.
            database.DataSetName = "Database";
            database.Tables.Add(new DataTable("Employees"));
            database.Tables.Add(new DataTable("Employee Schedule"));
            database.Tables.Add(new DataTable("Customers"));
            database.Tables.Add(new DataTable("Customer Attendance"));

            // Fill data tables and populate DataSet using the SQL adapters.
            employeeTableAdapter.Fill(database, "Employees");
            employeeScheduleTableAdapter.Fill(database, "Employee Schedule");
            customersTableAdapter.Fill(database, "Customers");
            customerAttendanceTableAdapter.Fill(database, "Customer Attendance");

            // Set the data source of the grid to the DataSource.
            // Makes the grid show the values.
            databaseGrid.DataSource = database;

            // Holds the combo box values of the employee schedule table.
            StringCollection employeeScheduleValues = new StringCollection();
            employeeScheduleValues.Add("AM/PM");
            employeeScheduleValues.Add("AM");
            employeeScheduleValues.Add("PM");
            employeeScheduleValues.Add("OFF");

            // Format the employee schedule table and add combo boxes.
            for (int i = 2;i < databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns.Count;i++) {
                GridTableCellStyleInfo styleEmployeeSchedule = databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell;

                styleEmployeeSchedule.DropDownStyle = GridDropDownStyle.Exclusive;
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell.CellType = GridCellTypeName.ComboBox;
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell.ChoiceList = employeeScheduleValues;
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell.CellValue = "CHOICES";
            }


            // Holds the combo box values of the customer attendance table.
            StringCollection customerAttendanceValues = new StringCollection();
            customerAttendanceValues.Add("AM");
            customerAttendanceValues.Add("PM");
            customerAttendanceValues.Add("AM/PM");
            customerAttendanceValues.Add("N/A");

            // Format the customer attendance table and add combo boxes.
            for (int i = 2;i < databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns.Count;i++) {
                GridTableCellStyleInfo customerAttendance = databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell;

                customerAttendance.DropDownStyle = GridDropDownStyle.Exclusive;
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell.CellType = GridCellTypeName.ComboBox;
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell.ChoiceList = customerAttendanceValues;
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns[i].Appearance.AnyRecordFieldCell.CellValue = "CHOICES";
            }

            // Holds the combo box values of the customer membership cell in the Customer table.
            StringCollection customerMembershipValues = new StringCollection();
            customerMembershipValues.Add("N/A");
            customerMembershipValues.Add("SMILE PLUS");

            // Format the Customer table and add membership combo boxes.
            GridTableCellStyleInfo customers = databaseGrid.GetTable("Customers").TableDescriptor.Columns[2].Appearance.AnyRecordFieldCell;
            customers.DropDownStyle = GridDropDownStyle.Exclusive;
            databaseGrid.GetTable("Customers").TableDescriptor.Columns[2].Appearance.AnyRecordFieldCell.CellType = GridCellTypeName.ComboBox;
            databaseGrid.GetTable("Customers").TableDescriptor.Columns[2].Appearance.AnyRecordFieldCell.ChoiceList = customerMembershipValues;
            databaseGrid.GetTable("Customers").TableDescriptor.Columns[2].Appearance.AnyRecordFieldCell.CellValue = "CHOICES";

            // Make the Id and Name cells readonly so as not to allow editing outside of appropriate tables.
            // This way, employee names can only be edited from the Employee table, while Ids cannot be changed.
            databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns[0].Appearance.AnyRecordFieldCell.ReadOnly = true;
            databaseGrid.GetTable("Employee Schedule").TableDescriptor.Columns[1].Appearance.AnyRecordFieldCell.ReadOnly = true;
            databaseGrid.GetTable("Employees").TableDescriptor.Columns[0].Appearance.AnyRecordFieldCell.ReadOnly = true;

            // Make the Id and Name cells readonly so as not to allow editing outside of appropriate tables.
            // This way, customer names can only be edited from the Employee table, while Ids cannot be changed.
            databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns[0].Appearance.AnyRecordFieldCell.ReadOnly = true;
            databaseGrid.GetTable("Customer Attendance").TableDescriptor.Columns[1].Appearance.AnyRecordFieldCell.ReadOnly = true;
            databaseGrid.GetTable("Customers").TableDescriptor.Columns[0].Appearance.AnyRecordFieldCell.ReadOnly = true;

            // Limit input length of Phone column cells
            databaseGrid.GetTable("Employees").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.MaxLength = 12;
            databaseGrid.GetTable("Customers").TableDescriptor.Columns["Phone"].Appearance.AnyRecordFieldCell.MaxLength = 12;

            // Set message box style
            MessageBoxAdv.MessageBoxStyle = MessageBoxAdv.Style.Metro;
        }

        /// <summary>
        /// When the "Add Employee" button is clicked, prompt the user to enter the information of the new
        /// employee, and then add the values to the database. Update the grid after the employee was added.
        /// </summary>
        private void addEmployeeButton_Click(object sender, EventArgs e) {

            // Create and display an "Add Employee" form for the user to enter employee information.
            // This form will also insert the new entries to the database.
            AddEmployeeForm prompt = new AddEmployeeForm();
            prompt.ShowDialog();

            // If the form was fully filled in and the entry was successfully inserted in the database, 
            // create new rows and update the grid.
            if (prompt.DialogResult == DialogResult.OK) {

                // Hold the employee's basic information and schedule values that are retrieved from the database,
                // from the newly added employee entries.
                string[] employeeData = DatabaseWorker.GetEmployeeEntry(DatabaseWorker.tempId);
                string[] scheduleData = DatabaseWorker.GetEmployeeScheduleEntry(DatabaseWorker.tempId);

                // Create new rows that will hold the newly added employee's basic information
                // and schedule values.
                DataRow employeeEntry = database.Tables["Employees"].NewRow();
                DataRow employeeScheduleEntry = database.Tables["Employee Schedule"].NewRow();

                // Populate each column of the new Employee and Employe Schedule rows with data from the database.
                for (int i = 0;i < database.Tables["Employees"].Columns.Count;i++) {
                    employeeEntry[i] = employeeData[i];
                }

                for (int i = 0;i < database.Tables["Employee Schedule"].Columns.Count;i++) {
                    employeeScheduleEntry[i] = scheduleData[i];
                }

                // Add the newly created Employee and Employee Schedule rows to the DataSet
                database.Tables["Employees"].Rows.Add(employeeEntry);
                database.Tables["Employee Schedule"].Rows.Add(employeeScheduleEntry);

            }
        }

        /// <summary>
        /// When the "Add Customer" button is clicked, prompt the user to enter the information of the new
        /// customer, and then add the values to the database. Update the grid after the customer was added.
        /// </summary>
        private void addCustomerButton_Click(object sender, EventArgs e) {

            // Create and display an "Add Customer" form for the user to enter customer information.
            // This form will also insert the new entries to the database.
            AddCustomerForm prompt = new AddCustomerForm();
            prompt.ShowDialog();

            // If the form was fully filled in and the entry was successfully inserted in the database, 
            // create new rows and update the grid.
            if (prompt.DialogResult == DialogResult.OK) {

                // Hold the customer's basic information and attendance values that are retrieved from the database,
                // from the newly added customer entries.
                string[] customerData = DatabaseWorker.GetCustomerEntry(DatabaseWorker.tempId);
                string[] customerAttendanceData = DatabaseWorker.GetCustomerAttendanceEntry(DatabaseWorker.tempId);

                // Create new rows that will hold the newly added customer's basic information
                // and attendance values.
                DataRow customerEntry = database.Tables["Customers"].NewRow();
                DataRow customerAttendanceEntry = database.Tables["Customer Attendance"].NewRow();

                // Populate each column of the new Customer and Customer Attendance rows with data from the database.
                for (int i = 0;i < database.Tables["Customers"].Columns.Count;i++) {
                    customerEntry[i] = customerData[i];
                }

                for (int i = 0;i < database.Tables["Customer Attendance"].Columns.Count;i++) {
                    customerAttendanceEntry[i] = customerAttendanceData[i];
                }

                // Add the newly created Customer and Customer Attendance rows to the DataSet
                database.Tables["Customers"].Rows.Add(customerEntry);
                database.Tables["Customer Attendance"].Rows.Add(customerAttendanceEntry);

            }
        }

        /// <summary>
        /// **************** TODO: COMPLETE DOCUMENTATION *****************
        /// 
        /// When the "Remove Entry" button is clicked, remove the selected employee and customer entries
        /// along with the related schedule and attendance entries respectively, from the database and from the
        /// DataSet. Update the grid to the reflect these changes.
        /// </summary>
        private void removeButton_Click(object sender, EventArgs e) {
            try {
                GridRangeInfoList range = databaseGrid.TableModel.Selections.GetSelectedRows(true, true);

                foreach (GridRangeInfo info in range) {

                    Element element = databaseGrid.TableModel.GetDisplayElementAt(info.Top);

                    GridNestedTable gnt = element as GridNestedTable;
                    GridNestedTable gnt1 = gnt;

                    while (gnt1 != null && gnt1.ChildTable != null) {
                        gnt = gnt1;
                        gnt1 = gnt.ChildTable.ParentTable.CurrentElement as GridNestedTable;
                    }

                    DataRowView drv = gnt.ChildTable.ParentTable.CurrentElement.GetData() as DataRowView;

                    string table = gnt.ChildTable.ParentTable.CurrentElement.GetRecord().ParentChildTable.Name;

                    if (table == "Employees") {
                        DatabaseWorker.RemoveEmployee(Convert.ToInt32(drv[0]));

                        Record record = gnt.ChildTable.ParentTable.CurrentElement.GetRecord();

                        int index = databaseGrid.GetTable("Employees").UnsortedRecords.IndexOf(record);

                        databaseGrid.GetTable("Employee Schedule").UnsortedRecords[index].Delete();
                        record.Delete();

                        database.Tables["Employees"].Rows[index].Delete();
                        database.Tables["Employee Schedule"].Rows[index].Delete();

                        database.AcceptChanges();

                    } else if (table == "Customers") {
                        DatabaseWorker.RemoveCustomer(Convert.ToInt32(drv[0]));

                        Record record = gnt.ChildTable.ParentTable.CurrentElement.GetRecord();

                        int index = databaseGrid.GetTable("Customers").UnsortedRecords.IndexOf(record);

                        databaseGrid.GetTable("Customer Attendance").UnsortedRecords[index].Delete();
                        record.Delete();

                        database.Tables["Customers"].Rows[index].Delete();
                        database.Tables["Customer Attendance"].Rows[index].Delete();

                        database.AcceptChanges();

                    } else if (table == "Employee Schedule") {
                        MessageBoxAdv.Show(this, "Cannot remove employee schedule entry directly. \n"
                                           + "To remove it, delete the corresponding employee entry.", "Error");
                    } else if (table == "Customer Attendance") {
                        MessageBoxAdv.Show(this, "Cannot remove customer attendance entry directly. \n"
                                           + "To remove it, delete the corresponding customer entry.", "Error");
                    }
                }
            } catch (Exception) {
                MessageBoxAdv.Show(this, "Not a valid entry", "Error");
            }
        }

        /// <summary>
        /// **************** TODO: COMPLETE DOCUMENTATION *****************
        /// 
        /// Whenever the value of a cell is edited, update the database entry and the corresponding DataSet value.
        /// Updat the grid to reflect these changes.
        /// </summary>
        private void databaseGrid_RecordValueChanged(object sender, RecordValueChangedEventArgs e) {

            // Object that holds the values of the record that the edited cell belongs to.
            DataRowView drv = e.Record.GetData() as DataRowView;

            // If the cell's record belongs to the Employee table
            if (e.Record.ParentChildTable.Name == "Employees") {

                ThreadPool.QueueUserWorkItem(state => {
                    DatabaseWorker.EditEmployee(Convert.ToInt32(drv[0]), drv[1].ToString(),
                    drv[2].ToString(), drv[3].ToString(),
                    drv[4].ToString());

                    string[] updatedEmployeeValues = DatabaseWorker.GetEmployeeEntryNoId(Convert.ToInt32(drv[0]));

                    int index = databaseGrid.GetTable("Employees").UnsortedRecords.IndexOf(e.Record);

                    e.Record.UpdateValues(updatedEmployeeValues);

                    databaseGrid.GetTable("Employee Schedule").UnsortedRecords[index].SetValue("Name", drv[1].ToString());
                }
                );
            }

            // If the cell's record belongs to the Employee Schedule table
            else if (e.Record.ParentChildTable.Name == "Employee Schedule") {
                string[] scheduleValues = new string[7];

                for (int i = 2;i < 9;i++) {
                    scheduleValues[i - 2] = drv[i].ToString();
                }

                ThreadPool.QueueUserWorkItem(state => {
                    DatabaseWorker.EditEmployeeSchedule(Convert.ToInt32(drv[0]), scheduleValues);

                    string[] updatedScheduleValues = DatabaseWorker.GetEmployeeScheduleEntry(Convert.ToInt32(drv[0]));
                    e.Record.UpdateValues(updatedScheduleValues);
                }
                );
            }

           // If the cell's record belongs to the Customer table
           else if (e.Record.ParentChildTable.Name == "Customers") {

                ThreadPool.QueueUserWorkItem(state => {
                    DatabaseWorker.EditCustomer(Convert.ToInt32(drv[0]), drv[1].ToString(), drv[2].ToString(), drv[3].ToString());

                    string[] updatedCustomerValues = DatabaseWorker.GetCustomerEntryNoId(Convert.ToInt32(drv[0]));

                    int index = databaseGrid.GetTable("Customers").UnsortedRecords.IndexOf(e.Record);

                    e.Record.UpdateValues(updatedCustomerValues);

                    databaseGrid.GetTable("Customer Attendance").UnsortedRecords[index].SetValue("Name", drv[1].ToString());
                }
                );
            }

            // If the cell's record belongs the Customer Attendance table.
            else if (e.Record.ParentChildTable.Name == "Customer Attendance") {
                string[] attendanceValues = new string[7];

                for (int i = 2;i < 9;i++) {
                    attendanceValues[i - 2] = drv[i].ToString();
                }

                ThreadPool.QueueUserWorkItem(state => {
                    DatabaseWorker.EditCustomerAttendance(Convert.ToInt32(drv[0]), attendanceValues);

                    string[] updatedAttendanceValues = DatabaseWorker.GetCustomerAttendanceEntry(Convert.ToInt32(drv[0]));
                    e.Record.UpdateValues(updatedAttendanceValues);
                }
                );

            }
        }

        #region Searching
        /// <summary>
        /// Filter table values based on search input.
        /// </summary>
        private void searchButton_Click(object sender, EventArgs e) {
            string filterColumn = "";

            //if (searchTextBox.Text != string.Empty) {
                databaseGrid.GetTable("Employees").TableDescriptor.RecordFilters.Remove(filterEmployees);
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.RecordFilters.Remove(filterEmployeeSchedule);
                databaseGrid.GetTable("Customers").TableDescriptor.RecordFilters.Remove(filterCustomers);
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.RecordFilters.Remove(filterCustomerAttendance);
            //}

            if (searchCategoryComboBox.SelectedIndex == 0) {
                switch (searchComboBox.SelectedIndex) {
                    case 0:
                        filterColumn = "[Id]";
                        break;
                    case 1:
                        filterColumn = "[Name]";
                        break;
                }

                filterEmployees.Expression = filterColumn + "like '" + searchTextBox.Text + "'";
                filterEmployeeSchedule.Expression = filterColumn + "like '" + searchTextBox.Text + "'";
                filterCustomers.Expression = filterColumn + "like '" + searchTextBox.Text + "'";
                filterCustomerAttendance.Expression = filterColumn + "like '" + searchTextBox.Text + "'";
                databaseGrid.GetTable("Employees").TableDescriptor.RecordFilters.Add(filterEmployees);
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.RecordFilters.Add(filterEmployeeSchedule);
                databaseGrid.GetTable("Customers").TableDescriptor.RecordFilters.Add(filterCustomers);
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.RecordFilters.Add(filterCustomerAttendance);
            } else if (searchCategoryComboBox.SelectedIndex == 1) {
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

                filterEmployees.Expression = filterColumn + "like '" + searchTextBox.Text + "'";
                filterEmployeeSchedule.Expression = filterColumn + "like '" + searchTextBox.Text + "'";
                databaseGrid.GetTable("Employees").TableDescriptor.RecordFilters.Add(filterEmployees);
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.RecordFilters.Add(filterEmployeeSchedule);
            } else if (searchCategoryComboBox.SelectedIndex == 2) {
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

                filterCustomers.Expression = filterColumn + "like '" + searchTextBox.Text + "'";
                filterCustomerAttendance.Expression = filterColumn + "like '" + searchTextBox.Text + "'";
                databaseGrid.GetTable("Customers").TableDescriptor.RecordFilters.Add(filterCustomers);
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.RecordFilters.Add(filterCustomerAttendance);
            }
        }

        /// <summary>
        /// If the search text box is empty, clear all table search filters.
        /// </summary>
        private void searchTextBox_TextChanged(object sender, EventArgs e) {
            if (searchTextBox.Text == string.Empty) {
                databaseGrid.GetTable("Employees").TableDescriptor.RecordFilters.Remove(filterEmployees);
                databaseGrid.GetTable("Employee Schedule").TableDescriptor.RecordFilters.Remove(filterEmployeeSchedule);
                databaseGrid.GetTable("Customers").TableDescriptor.RecordFilters.Remove(filterCustomers);
                databaseGrid.GetTable("Customer Attendance").TableDescriptor.RecordFilters.Remove(filterCustomerAttendance);
            }
        }

        /// <summary>
        /// Change values of the search combo box according to the table range.
        /// </summary>
        private void searchCategoryComboBox_SelectedIndexChanged(object sender, EventArgs e) {
            if (searchCategoryComboBox.SelectedIndex == 0) {
                searchComboBox.Items.Clear();
                searchComboBox.Items.AddRange(allValues);
            } else if (searchCategoryComboBox.SelectedIndex == 1) {
                searchComboBox.Items.Clear();
                searchComboBox.Items.AddRange(employeeValues);
            } else if (searchCategoryComboBox.SelectedIndex == 2) {
                searchComboBox.Items.Clear();
                searchComboBox.Items.AddRange(customerValues);
            }

            searchComboBox.SelectedIndex = 1;
        }
        #endregion

        /// <summary>
        /// Setup some UI controls and bind some events.
        /// </summary>
        private void SetupComponents() {

            // If the return key is pressed when typing in the search box, execute search events.
            searchTextBox.KeyDown += (sender, args) => {
                if (args.KeyCode == Keys.Return) {
                    searchButton.PerformClick();
                }
            };

            // Set default selected values of the search category combo boxes.
            searchCategoryComboBox.SelectedIndex = 0;
            searchComboBox.SelectedIndex = 1;
        }

        /// <summary>
        /// Open the print form and if successful execute print events.
        /// </summary>
        private void printGridButton_Click(object sender, EventArgs e) {
            PrintForm prompt = new PrintForm();
            prompt.ShowDialog();

            if (prompt.DialogResult == DialogResult.OK) {

                string selectedTable = prompt.SelectedTable;
                tableName = selectedTable;

                PrintDialog printDialog = new PrintDialog();
                printDialog.Document = gridDocument;
                printDialog.UseEXDialog = true;

                if (DialogResult.OK == printDialog.ShowDialog()) {
                    gridDocument.DefaultPageSettings.Margins = new Margins(50, 50, 110, 50);
                    gridDocument.Print();
                }
            }

        }

        /// <summary>
        /// Print the selected table.
        /// </summary>
        private void gridDocument_PrintPage(object sender, PrintPageEventArgs e) {
            GridPrinter printer = new GridPrinter(tableName, ref databaseGrid, ref database, e);
            printer.Print();
        }

        private void settingsButton_Click(object sender, EventArgs e) {
            SettingsForm settings = new SettingsForm();
            settings.ShowDialog();
        }
    }
}
