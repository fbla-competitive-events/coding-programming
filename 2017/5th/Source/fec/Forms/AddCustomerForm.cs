using Syncfusion.Windows.Forms;
using System;
using System.Collections.Generic;
using System.Windows.Forms;

namespace fec {

    public partial class AddCustomerForm : MetroForm {

        // Holds the customer attendance combo boxes.
        List<ComboBox> attendanceComboBoxes = new List<ComboBox>(7);

        static Random random = new Random();

        public AddCustomerForm() {
            InitializeComponent();

            // Set the form icon
            this.Icon = Properties.Resources.fecIcon;

            SetupComboBoxes();

            // Set message box colors to green.
            MessageBoxAdv.MetroColorTable.BorderColor = System.Drawing.Color.LimeGreen;
            MessageBoxAdv.MetroColorTable.YesButtonBackColor = System.Drawing.Color.LimeGreen;
            MessageBoxAdv.MetroColorTable.NoButtonBackColor = System.Drawing.Color.LimeGreen;
            MessageBoxAdv.MetroColorTable.OKButtonBackColor = System.Drawing.Color.LimeGreen;
        }

        /// <summary>
        /// Add a new customer in the database.
        /// </summary>
        private void AddButton_Click(object sender, EventArgs e) {
            if (ValidInput()) {
                string[] attendanceValues = new string[7];

                // Get combo box attendance values.
                for (int i = 0;i < 7;i++) {
                    attendanceValues[i] = attendanceComboBoxes[i].Text;
                }

                string name = BuildName();

                // Generate a random id.
                int id = random.Next(Int32.MaxValue);

                // Used to check whether the id alredy exists in the database.
                bool exists = true;

                // Loop until a non-existent id is generated.
                while (exists) {
                    exists = DatabaseWorker.IdExists(id);
                    if (!exists) {
                        DatabaseWorker.tempId = id;
                        DatabaseWorker.AddCustomer(id, name, membershipComboBox.SelectedItem.ToString(), phoneTextBox.Text, attendanceValues);
                    }

                    // Generate a new id.
                    id = random.Next(Int32.MaxValue);
                }

                DialogResult = DialogResult.OK;
                Close();
            } else {
                PrintErrorMessage();
            }
        }

        /// <summary>
        /// Combine name parts to build a full name.
        /// Capitalize letters were appropriate to ensure proper formatting.
        /// </summary>
        /// <returns> The full formatted name. </returns>
        private string BuildName() {
            string name = "";

            name += firstNameTextBox.Text.ToLower()[0].ToString().ToUpper() + firstNameTextBox.Text.ToLower().Substring(1) + " ";
            if (middleTextBox.Text != string.Empty) {
                name += middleTextBox.Text.ToUpper() + ". ";
            }
            name += lastNameTextBox.Text.ToLower()[0].ToString().ToUpper() + lastNameTextBox.Text.ToLower().Substring(1);

            return name;
        }

        /// <summary>
        /// Checks whether the form is completely filled in with input.
        /// </summary>
        /// <returns> Whether the form is completely filled with input. </returns>
        private bool ValidInput() {
            if (firstNameTextBox.Text.Replace(" ", "") != string.Empty
                && lastNameTextBox.Text.Replace(" ", "") != string.Empty 
                && membershipComboBox.SelectedIndex != 0
                && phoneTextBox.Text.Replace(" ", "").Length == 10) {

                foreach (ComboBox comboBox in attendanceComboBoxes) {
                    if (comboBox.SelectedIndex == 0) {
                        return false;
                    }
                }

                return true;
            }

            return false;
        }

        /// <summary>
        /// Print an error message according to the fields not filled.
        /// </summary>
        private void PrintErrorMessage() {
            string error = "";

            if (firstNameTextBox.Text.Replace(" ", "") == string.Empty)
                error += "• Please fill in the First Name field.\n";

            if (lastNameTextBox.Text.Replace(" ", "") == string.Empty)
                error += "• Please fill in the Last Name field.\n";

            if (membershipComboBox.SelectedIndex == 0)
                error += "• Please select a value for Membership.\n";

            if (phoneTextBox.Text.Replace(" ", "").Length != 10)
                error += "• Please fill in a 10 digit number for the Phone field.\n";

            if (mondayComboBox.SelectedIndex == 0)
                error += "• Please select an attendance value for Monday.\n";

            if (tuesdayComboBox.SelectedIndex == 0)
                error += "• Please select an attendance value for Tuesday.\n";

            if (wednesdayComboBox.SelectedIndex == 0)
                error += "• Please select an attendance value for Wednesday.\n";

            if (thursdayComboBox.SelectedIndex == 0)
                error += "• Please select an attendance value for Thursday.\n";

            if (fridayComboBox.SelectedIndex == 0)
                error += "• Please select an attendance value for Friday.\n";

            if (saturdayComboBox.SelectedIndex == 0)
                error += "• Please select an attendance value for Saturday.\n";

            if (sundayComboBox.SelectedIndex == 0)
                error += "• Please select an attendance value for Sunday.\n";

            MessageBoxAdv.Show(this, error, "Error");
        }

        #region Input Limitation
        /// <summary>
        /// Limit the characters entered into the 'First Name' text box to control characters, and letters only.
        /// </summary>
        private void FirstNameTextBox_KeyPress(object sender, KeyPressEventArgs e) {
            if (!char.IsControl(e.KeyChar) && !char.IsLetter(e.KeyChar)) {
                e.Handled = true;
            }
        }

        /// <summary>
        /// Limit the characters entered into the 'Middle Initial' text box to control characters, and letters only.
        /// </summary>
        private void MiddleTextBox_KeyPress(object sender, KeyPressEventArgs e) {
            if (!char.IsControl(e.KeyChar) && !char.IsLetter(e.KeyChar)) {
                e.Handled = true;
            }
        }

        /// <summary>
        /// Limit the characters entered into the 'Last Name' text box to control characters, and letters only.
        /// </summary>
        private void LastNameTextBox_KeyPress(object sender, KeyPressEventArgs e) {
            if (!char.IsControl(e.KeyChar) && !char.IsLetter(e.KeyChar)) {
                e.Handled = true;
            }
        }
        #endregion

        /// <summary>
        /// Add attendance combo boxes to the attendance combo box list and set their initial index to their first item.
        /// </summary>
        private void SetupComboBoxes() {
            attendanceComboBoxes.Add(mondayComboBox);
            attendanceComboBoxes.Add(tuesdayComboBox);
            attendanceComboBoxes.Add(wednesdayComboBox);
            attendanceComboBoxes.Add(thursdayComboBox);
            attendanceComboBoxes.Add(fridayComboBox);
            attendanceComboBoxes.Add(saturdayComboBox);
            attendanceComboBoxes.Add(sundayComboBox);

            foreach (ComboBox comboBox in attendanceComboBoxes) {
                comboBox.SelectedIndex = 0;
            }

            membershipComboBox.SelectedIndex = 0;
        }

        private void AddCustomerForm_FormClosing(object sender, FormClosingEventArgs e) {
            // Revert message box colors to blue.
            MessageBoxAdv.MetroColorTable.BorderColor = System.Drawing.Color.DeepSkyBlue;
            MessageBoxAdv.MetroColorTable.YesButtonBackColor = System.Drawing.Color.DeepSkyBlue;
            MessageBoxAdv.MetroColorTable.NoButtonBackColor = System.Drawing.Color.DeepSkyBlue;
            MessageBoxAdv.MetroColorTable.OKButtonBackColor = System.Drawing.Color.DeepSkyBlue;
        }
    }
}
