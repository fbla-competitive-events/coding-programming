using System;
using System.Collections.Generic;
using System.Windows.Forms;
using static Entertainment_Elevated.ChangeFormPanel;

namespace Entertainment_Elevated
{
    public partial class EmployeeForm : Form, IPanelForm
    {
        public static List<Employee> Employees = new List<Employee>();

        public EmployeeForm()
        {
            InitializeComponent();

            // Connect the GridView to the Employees List
            EmployeeDataGridView.DataSource = Employees;
            
            // Order the Columns in the GridView
            EmployeeDataGridView.Columns["FirstName"].DisplayIndex = 0;
            EmployeeDataGridView.Columns["LastName"].DisplayIndex = 1;
            EmployeeDataGridView.Columns["Email"].DisplayIndex = 2;
            EmployeeDataGridView.Columns["PhoneNumber"].DisplayIndex = 3;
            EmployeeDataGridView.Columns["Payrate"].DisplayIndex = 4;
            EmployeeDataGridView.Columns["Position"].DisplayIndex = 5;            

            // Allow the user to sort by certain columns
            foreach (DataGridViewColumn column in EmployeeDataGridView.Columns)
            {
                column.SortMode = DataGridViewColumnSortMode.Automatic;
            }
        }

        private void MenuButton_Click(object sender, EventArgs e)
        {
            ChangeFormPanels<MenuForm>(sender);
        }

        // Open the AddEmployeeForm
        private void AddEmployeeButton_Click(object sender, EventArgs e)
        {
            AddEmployeeForm addEmployeeForm = new AddEmployeeForm(EmployeeDataGridView);
            addEmployeeForm.ShowDialog();
        }

        // Open the DeleteEmployeeForm
        private void DeleteEmployeeButton_Click(object sender, EventArgs e)
        {
            DeleteEmployeeForm deleteEmployeeForm = new DeleteEmployeeForm(EmployeeDataGridView);
            deleteEmployeeForm.ShowDialog();
        }

        // To satisfy the IPanelForm interface requirement
        public Panel Panel()
        {
            return EmployeeFormPanel;
        }
    }
}
