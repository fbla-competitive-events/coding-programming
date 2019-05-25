using System;
using System.Collections.Generic;
using System.IO;
using System.Windows.Forms;
using System.Xml.Serialization;
using static Entertainment_Elevated.ChangeFormPanel;

namespace Entertainment_Elevated
{
    public partial class GeneralForm : Form
    {
        public const int FORMWIDTH = 600;
        public const int FORMHEIGHT = 500;

        public GeneralForm()
        {
            // Start with the Main Menu Screen
            ChangeFormPanels<MenuForm>(this);
            InitializeComponent();
        }

        // Load the employee XML file
        private void GeneralForm_Load(object sender, EventArgs e)
        {
            // Create an XML reader to correctly read the list of employees
            XmlSerializer serializer = new XmlSerializer(typeof(List<Employee>));
            try
            {
                using (FileStream fileStream = File.OpenRead("Employees.xml"))
                {
                    // Don't read the file if it is empty
                    if (fileStream.Length == 0)
                        return;

                    // Deserialize the list and then set the Employee list equal to this list
                    List<Employee> deserializedList = (List<Employee>)serializer.Deserialize(fileStream);
                    EmployeeForm.Employees = deserializedList;
                }
            }
            catch
            {
                // Throw an error if this XML file is damaged in some way
                Console.WriteLine("XML Error for Employees");
            }

            try
            {
                // Repeat the same process for the customer list
                serializer = new XmlSerializer(typeof(List<Customer>));
                using (FileStream fileStream = File.OpenRead("Customers.xml"))
                {
                    if (fileStream.Length == 0)
                        return;
                    List<Customer> deserializedList = (List<Customer>)serializer.Deserialize(fileStream);
                    CustomerForm.Customers = deserializedList;
                }
            }
            catch
            {
                Console.WriteLine("XML Error for Customers");
            }

            try
            {
                // Repeat the same process for the Customer Attendance array
                serializer = new XmlSerializer(typeof(int[][]));
                using (FileStream fileStream = File.OpenRead("CustomerAttendance.xml"))
                {
                    if (fileStream.Length == 0)
                        return;
                    int[][] deserializedArray = (int[][])serializer.Deserialize(fileStream);
                    CustomerForm.CustomerAttendance = deserializedArray;
                }
            }
            catch
            {
                Console.WriteLine("XML Error for CustomerAttendance");
            }
        }

        // Save the employee and customer data in an XML file
        private void GeneralForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            // Write all text to the employee xml file
            File.WriteAllText("Employees.xml", string.Empty);

            // Use a serializer to seralize the data into XML format
            XmlSerializer serializer = new XmlSerializer(typeof(List<Employee>));
            using (FileStream fileStream = File.OpenWrite("Employees.xml"))
            {
                serializer.Serialize(fileStream, EmployeeForm.Employees);
            }

            // Repeat the same process for the customers
            File.WriteAllText("Customers.xml", string.Empty);
            serializer = new XmlSerializer(typeof(List<Customer>));
            using (FileStream fileStream = File.OpenWrite("Customers.xml"))
            {
                serializer.Serialize(fileStream, CustomerForm.Customers);
            }

            // Repeat the same process for the customer attendance
            File.WriteAllText("CustomerAttendance.xml", string.Empty);
            serializer = new XmlSerializer(typeof(int[][]));
            using (FileStream fileStream = File.OpenWrite("CustomerAttendance.xml"))
            {
                serializer.Serialize(fileStream, CustomerForm.CustomerAttendance);
            }
        }
    }
}
