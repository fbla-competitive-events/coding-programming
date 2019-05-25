using System;
using System.Collections.Generic;
using System.IO;
using System.Windows.Forms;

namespace Entertainment_Elevated
{
    public partial class AddEmployeeForm : Form
    {
        private DataGridView gridView;

        // The list of different positions that an employee can have
        public List<string> Positions { get; private set; } = new List<string>();
        
        // Pass in the employee datagridview from the employeeForm to refresh it
        public AddEmployeeForm(DataGridView GridView)
        {
            InitializeComponent();

            // Create a reference gridView in this class to the GridView from the employeeForm
            // To be able to refresh the one in the other form
            gridView = GridView;
        }

        // Occurs when the form is opened
        private void AddEmployeeForm_Load(object sender, EventArgs e)
        {
            try
            {
                // Read the text file with the positions in it and put it into the Positions list
                using (StreamReader streamReader = new StreamReader("Positions.txt"))
                {
                    string line = streamReader.ReadLine();

                    // While there are still more lines, continue reading
                    while (line != null)
                    {
                        Positions.Add(line);
                        line = streamReader.ReadLine();
                    }
                }
            }
            catch
            {
                Console.WriteLine("Error in position text file");
            }
            
            // Add all of the positions into the corresponding ComboBox
            PositionComboBox.Items.AddRange(Positions.ToArray());
        }

        private void AddEmployeeForm_FormClosed(object sender, EventArgs e)
        {
            // Save all of the positions in the text file
            using (StreamWriter streamWriter = new StreamWriter("Positions.txt"))
            {
                foreach (string position in Positions)
                {
                    if (position != string.Empty)
                        streamWriter.WriteLine(position);
                }
            }

            // Reset the data source of the GridView to be able to force it to refresh the data within the table
            gridView.DataSource = null;
            gridView.DataSource = EmployeeForm.Employees;

            // Reset the order of the columns of the GridView
            gridView.Columns["FirstName"].DisplayIndex = 0;
            gridView.Columns["LastName"].DisplayIndex = 1;
            gridView.Columns["Email"].DisplayIndex = 2;
            gridView.Columns["PhoneNumber"].DisplayIndex = 3;
        }

        private void AddEmployeeButton_Click(object sender, EventArgs e)
        {
            // If the user typed their own position into the box, then add it to the list of positions
            if (!PositionComboBox.Items.Contains(PositionComboBox.Text))
                Positions.Add(PositionComboBox.Text);

            string errorText = "";
            if (FirstNameTextBox.Text == "")
                errorText += "Please enter a first name.\n";
            if (LastNameTextBox.Text == "")
                errorText += "Please enter a last name.\n";

            // Phone number is a masked textbox 
            if (!PhoneNumberTextBox.MaskCompleted)
                errorText += "Please enter a complete phone number.\n";

            // Don't throw error for email so an email is optional
            if (errorText != "")
            {
                MessageBox.Show(errorText);
                return;
            }

            try
            {
                // Create an employee object and add it to the Employee list
                Employee employee = new Employee(FirstNameTextBox.Text, LastNameTextBox.Text, PhoneNumberTextBox.Text,
                                                EmailTextBox.Text, PositionComboBox.Text, decimal.Parse(PayrateTextbox.Text));
                EmployeeForm.Employees.Add(employee);
            }
            catch
            {
                // Throw an error and display a popup box if user enters erroneous information
                MessageBox.Show("Please check entered data.");
                return;
            }

            Close();
        }

        private void AddEmployeeForm_HelpButtonClicked(object sender, System.ComponentModel.CancelEventArgs e)
        {
        }
    }
}
