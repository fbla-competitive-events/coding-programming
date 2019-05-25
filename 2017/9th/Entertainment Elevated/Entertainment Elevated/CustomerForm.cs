using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using static Entertainment_Elevated.ChangeFormPanel;

namespace Entertainment_Elevated
{
    public partial class CustomerForm : Form, IPanelForm
    {
        // Is a consideration to use a hash table here, especially if the customer list becomes larger and larger
        // But hashing the names of the customers does not allow searching for part of a customer's name
        // So a List is used instead
        public static List<Customer> Customers = new List<Customer>();

        // The jagged array for when customers come into the store
        // Jagged arrays generally run faster and are easier to deal with than a 2D array
        // Also, jagged arrays are XML serializable and 2D arrays are not which makes it easier to save the data
        public static int[][] CustomerAttendance = new int[7][];

        public CustomerForm()
        {
            InitializeComponent();

            CustomerListBox.DataSource = Customers;
        }

        // When the user tries to search for a customer
        private void SearchTextBox_TextChanged(object sender, EventArgs e)
        {
            RefreshListBox();
        }

        private void AddCustomerButton_Click(object sender, EventArgs e)
        {
            AddCustomerForm addCustomerForm = new AddCustomerForm();
            addCustomerForm.ShowDialog();
            RefreshListBox();
        }

        private void ReportButton_Click(object sender, EventArgs e)
        {
            PrintPreviewDialog.Document = PrintDocument;
            PrintPreviewDialog.ShowDialog();
        }

        private void PrintDocument_PrintPage(object sender, System.Drawing.Printing.PrintPageEventArgs e)
        {
            // Create a font to write with
            Font font = new Font("Arial", 14, FontStyle.Bold);
            float lineHeightFloat = font.GetHeight();
            float verticalPrintPosition = e.MarginBounds.Top;
            float horizontalPrintPosition = e.MarginBounds.Left;

            // For each customer, write their name and how many times they have visited the FEC
            foreach (Customer customer in Customers)
            {
                e.Graphics.DrawString(customer.ToString() + ": " + customer.StoreVisits.Count.ToString(), font, Brushes.Black,
                        horizontalPrintPosition, verticalPrintPosition);
                verticalPrintPosition += 2 * lineHeightFloat;
            }
        }

        private void AddVisitButton_Click(object sender, EventArgs e)
        {
            try
            {
                ((Customer)CustomerListBox.SelectedItem).StoreVisits.Add(new StoreVisit(DateTime.Now));
                CustomerAttendance[(int)DateTime.Now.DayOfWeek][DateTime.Now.Hour]++;

                // Refresh the ListBox
                CustomerListBox.DataSource = null;
                CustomerListBox.DataSource = Customers;
            }
            catch
            {
                MessageBox.Show("Please select a customer");
            }
        }

        public void RefreshListBox()
        {
            CustomerListBox.Items.Clear();

            // Search for the text and only display the ones that match the search text 
            foreach (Customer customer in Customers)
            {
                if (customer.ToString().ToLower().Contains(SearchTextBox.Text.ToLower()))
                    CustomerListBox.Items.Add(customer);
            }
        }

        private void CustomerStatisticsButton_Click(object sender, EventArgs e)
        {
            ChangeFormPanels<CustomerDataForm>(sender);
        }

        private void MenuButton_Click(object sender, EventArgs e)
        {
            ChangeFormPanels<MenuForm>(sender);
        }

        public Panel Panel()
        {
            ActiveControl = SearchTextBox;
            SearchTextBox.Select();
            SearchTextBox.Focus();
            return CustomerFormPanel;
        }        
    }
}
