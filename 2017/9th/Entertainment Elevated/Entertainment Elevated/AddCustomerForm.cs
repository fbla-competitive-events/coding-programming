using System;
using System.Windows.Forms;

namespace Entertainment_Elevated
{
    public partial class AddCustomerForm : Form
    {
        public AddCustomerForm()
        {
            InitializeComponent();
        }

        private void AddCustomerButton_Click(object sender, EventArgs e)
        {
            try
            {
                string errorText = string.Empty;
                if (FirstNameTextBox.Text == string.Empty)
                    errorText += "Please enter a first name.\n";
                if (LastNameTextBox.Text == string.Empty)
                    errorText += "Please enter a last name.\n";

                // Phone number is a masked textbox 
                if (!PhoneNumberTextBox.MaskCompleted)
                    errorText += "Please enter a complete phone number.\n";

                // Don't throw error for email so an email is optional
                if (errorText != string.Empty)
                {
                    MessageBox.Show(errorText);
                    return;
                }

                // Create a new customer object and add it to the Customers list
                Customer customer = new Customer(FirstNameTextBox.Text, LastNameTextBox.Text, PhoneNumberTextBox.Text, EmailTextBox.Text);
                CustomerForm.Customers.Add(customer);
            }
            catch
            {
                // Throw an error and display a popup box if user enters erroneous information
                MessageBox.Show("Please check entered data.");
                return;
            }

            Close();
        }

        private void AddCustomerForm_HelpButtonClicked(object sender, System.ComponentModel.CancelEventArgs e)
        {
            MessageBox.Show("Enter in all of the information for the customer and click the button to create the new customer. "
                + "Entering an email is optional.");
        }
    }
}
