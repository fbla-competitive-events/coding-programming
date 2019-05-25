using System;
using System.Windows.Forms;

namespace Entertainment_Elevated
{
    public partial class DeleteEmployeeForm : Form
    {
        private DataGridView gridView;

        // Pass in the GridView to refresh it
        public DeleteEmployeeForm(DataGridView GridView)
        {
            InitializeComponent();
            SetEmployeeListBox();
            gridView = GridView;
        }

        private void DeleteEmployeesButton_Click(object sender, EventArgs e)
        {
            // Show a warning before deleting the employees
            DialogResult result = MessageBox.Show("Are you sure you want to delete these employees?", "Caution", MessageBoxButtons.YesNo);

            // Do not delete the employees if the user is not sure
            if (result == DialogResult.No)
                return;

            // If the employee was selected, delete them
            foreach (Employee employee in EmployeeListBox.SelectedItems)
            {
                EmployeeForm.Employees.Remove(employee);
            }
            SetEmployeeListBox();
        }

        private void SetEmployeeListBox()
        {
            EmployeeListBox.Items.Clear();

            // Use a delegate operation to quickly add all of the employee names into the ListBox
            EmployeeForm.Employees.ForEach(employee => EmployeeListBox.Items.Add(employee));
        }

        private void DeleteEmployeeForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            // Reset the data source of the GridView to be able to force it to refresh the data within the table
            gridView.DataSource = null;
            gridView.DataSource = EmployeeForm.Employees;

            // Reset the order of the columns of the GridView
            gridView.Columns["FirstName"].DisplayIndex = 0;
            gridView.Columns["LastName"].DisplayIndex = 1;
            gridView.Columns["Email"].DisplayIndex = 2;
            gridView.Columns["PhoneNumber"].DisplayIndex = 3;
        }
    }
}
