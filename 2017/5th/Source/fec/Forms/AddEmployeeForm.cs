using Syncfusion.Windows.Forms;
using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using System.Windows.Forms;

namespace fec {

    public partial class AddEmployeeForm : MetroForm {

        // Holds the schedule combo boxes.
        List<ComboBox> scheduleComboBoxes = new List<ComboBox>(7);
        static Random random = new Random();

        public AddEmployeeForm() {
            InitializeComponent();

            // Set the form icon
            this.Icon = Properties.Resources.fecIcon;

            SetupScheduleComboBoxes();
            stateComboBox.SelectedIndex = 0;

            // Set message box colors to green.
            MessageBoxAdv.MetroColorTable.BorderColor = System.Drawing.Color.LimeGreen;
            MessageBoxAdv.MetroColorTable.YesButtonBackColor = System.Drawing.Color.LimeGreen;
            MessageBoxAdv.MetroColorTable.NoButtonBackColor = System.Drawing.Color.LimeGreen;
            MessageBoxAdv.MetroColorTable.OKButtonBackColor = System.Drawing.Color.LimeGreen;
        }

        /// <summary>
        /// Add a new employee in the database.
        /// </summary>
        private void AddButton_Click(object sender, EventArgs e) {

            if (ValidInput()) {
                string[] scheduleValues = new string[7];

                // Get combo box schedule values.
                for (int i = 0; i < 7;i++) {
                    scheduleValues[i] = scheduleComboBoxes[i].Text;
                }

                string name = BuildName();
                string address = BuildAddress();

                // Generate a random id.
                int id = random.Next(int.MaxValue);

                // Used to check whether the id alredy exists in the database.
                bool exists = true;

                // Loop until a non-existent id is generated.
                while (exists) {
                    exists = DatabaseWorker.IdExists(id);
                    if (!exists) {
                        DatabaseWorker.tempId = id;
                        DatabaseWorker.AddEmployee(id, name, jobTextBox.Text.Trim(' '), address, phoneTextBox.Text, scheduleValues);
                    }
                    id = random.Next(int.MaxValue);
                }

                DialogResult = DialogResult.OK;
                Close();
            } 
            else {
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
  
            name += firstNameTextBox.Text.ToLower()[0].ToString().ToUpper() 
                 + firstNameTextBox.Text.ToLower().Substring(1) + " ";

            if (middleTextBox.Text != string.Empty) {
                name += middleTextBox.Text.ToUpper() + ". ";
            }

            name += lastNameTextBox.Text.ToLower()[0].ToString().ToUpper() 
                 + lastNameTextBox.Text.ToLower().Substring(1);

            return name;
        }

        /// <summary>
        /// Combine address parts to build a full address. Remove multiple spaces
        /// and trim the parts to ensure good format.
        /// </summary>
        /// <returns> The full address. </returns>
        private string BuildAddress() {

            // Create a new reglar expression to remove multiple spaces.
            Regex regex = new Regex("[ ]{2,}", RegexOptions.IgnoreCase);

            return  streetNumTextBox.Text + " " 
                    + regex.Replace(streetTextBox.Text.Trim(' '), " ") + ", " 
                    + regex.Replace(cityTextBox.Text.Trim(' '), " ") + ", " 
                    + stateComboBox.Text + " " 
                    + zipTextBox.Text;
        }

        /// <summary>
        /// Checks whether the form is completely filled in with input.
        /// </summary>
        /// <returns> Whether the form is completely filled in with input. </returns>
        private bool ValidInput() {
            if (firstNameTextBox.Text.Replace(" ", "") != string.Empty 
                && lastNameTextBox.Text.Replace(" ", "") != string.Empty
                && phoneTextBox.Text.Replace(" ", "").Length == 10 
                && jobTextBox.Text.Replace(" ", "") != string.Empty 
                && streetNumTextBox.Text.Replace(" ", "") != string.Empty
                && streetTextBox.Text.Replace(" ", "") != string.Empty 
                && cityTextBox.Text.Replace(" ", "") != string.Empty
                && stateComboBox.SelectedIndex != 0 
                && zipTextBox.Text.Length == 5) 
            {

                foreach (ComboBox comboBox in scheduleComboBoxes) {
                    if (comboBox.SelectedIndex == 0) {
                        return false;
                    }
                }

                return true;
            }

            return false;
        }

        /// <summary>
        /// Print an error message according to the fields not filled in.
        /// </summary>
        private void PrintErrorMessage() {
            string error = "";

            if (firstNameTextBox.Text.Replace(" ", "") == string.Empty)
                error += "• Please fill in the First Name field.\n";

            if (lastNameTextBox.Text.Replace(" ", "") == string.Empty)
                error += "• Please fill in the Last Name field.\n";

            if (phoneTextBox.Text.Replace(" ", "").Length != 10)
                error += "• Please fill in a 10 digit number for the Phone field.\n";

            if (jobTextBox.Text.Replace(" ", "") == string.Empty)
                error += "• Please fill in the Job field.\n";

            if (streetNumTextBox.Text.Replace(" ", "") == string.Empty)
                error += "• Please fill in the Street Number field.\n";

            if (streetTextBox.Text.Replace(" ", "") == string.Empty)
                error += "• Please fill in the Street field.\n";

            if (cityTextBox.Text.Replace(" ", "") == string.Empty)
                error += "• Please fill in the City field.\n";

            if (stateComboBox.SelectedIndex == 0)
                error += "• Please select a State.\n";

            if (zipTextBox.Text.Length != 5)
                error += "• Please fill in a 5 digit number for the ZIP code field.\n";

            if (mondayComboBox.SelectedIndex == 0)
                error += "• Please select a schedule value for Monday.\n";

            if (tuesdayComboBox.SelectedIndex == 0)
                error += "• Please select a schedule value for Tuesday.\n";

            if (wednesdayComboBox.SelectedIndex == 0)
                error += "• Please select a schedule value for Wednesday.\n";

            if (thursdayComboBox.SelectedIndex == 0)
                error += "• Please select a schedule value for Thursday.\n";

            if (fridayComboBox.SelectedIndex == 0)
                error += "• Please select a schedule value for Friday.\n";

            if (saturdayComboBox.SelectedIndex == 0)
                error += "• Please select a schedule value for Saturday.\n";

            if (sundayComboBox.SelectedIndex == 0)
                error += "• Please select a schedule value for Sunday.\n";

            MessageBoxAdv.Show(this, error, "Error");
        }

        #region Input Limitations
        /// <summary>
        /// Limit the characters entered into the 'First Name' text box to letters, and control characters only.
        /// </summary>
        private void FirstNameTextBox_KeyPress(object sender, KeyPressEventArgs e) {
            if (!char.IsControl(e.KeyChar) && !char.IsLetter(e.KeyChar)) {
                e.Handled = true;
            }
        }

        /// <summary>
        /// Limit the characters entered into the 'Middle Initial' text box to letters, and control characters only.
        /// </summary>
        private void MiddleTextBox_KeyPress(object sender, KeyPressEventArgs e) {
            if (!char.IsControl(e.KeyChar) && !char.IsLetter(e.KeyChar)) {
                e.Handled = true;
            }
        }

        /// <summary>
        /// Limit the characters entered into the 'Last Name' text box to letters, and control characters only.
        /// </summary>
        private void LastNameTextBox_KeyPress(object sender, KeyPressEventArgs e) {
            if (!char.IsControl(e.KeyChar) && !char.IsLetter(e.KeyChar)) {
                e.Handled = true;
            }
        }

        /// <summary>
        /// Limit the characters entered into the 'Street Number' text box to numbers, and control characters only.
        /// </summary>
        private void StreetNumTextBox_KeyPress(object sender, KeyPressEventArgs e) {
            if (!char.IsControl(e.KeyChar) && !char.IsDigit(e.KeyChar)) {
                e.Handled = true;
            }
        }

        /// <summary>
        /// Limit the characters entered into the 'Street' text box to space, letters, and control characters only.
        /// </summary>
        private void StreetTextBox_KeyPress(object sender, KeyPressEventArgs e) {
            if (!char.IsControl(e.KeyChar) && (e.KeyChar != ' ') && !char.IsLetter(e.KeyChar)) {
                e.Handled = true;
            }
        }

        /// <summary>
        /// Limit the characters entered into the 'City' text box to space, letters, and control characters only.
        /// </summary>
        private void CityTextBox_KeyPress(object sender, KeyPressEventArgs e) {
            if (!char.IsControl(e.KeyChar) && (e.KeyChar != ' ') && !char.IsLetter(e.KeyChar)) {
                e.Handled = true;
            }
        }

        /// <summary>
        /// Limit the characters entered into the 'Zip' text box to numbers, and control characters only.
        /// </summary>
        private void ZipTextBox_KeyPress(object sender, KeyPressEventArgs e) {
            if (!char.IsControl(e.KeyChar) && !char.IsDigit(e.KeyChar)) {
                e.Handled = true;
            }
        }
        #endregion

        /// <summary>
        /// Add schedule combo boxes to the schedule combo box list and set their initial index to their first item.
        /// </summary>
        private void SetupScheduleComboBoxes() {
            scheduleComboBoxes.Add(mondayComboBox);
            scheduleComboBoxes.Add(tuesdayComboBox);
            scheduleComboBoxes.Add(wednesdayComboBox);
            scheduleComboBoxes.Add(thursdayComboBox);
            scheduleComboBoxes.Add(fridayComboBox);
            scheduleComboBoxes.Add(saturdayComboBox);
            scheduleComboBoxes.Add(sundayComboBox);
         

            foreach(ComboBox comboBox in scheduleComboBoxes) {
                comboBox.SelectedIndex = 0;
            }
        }

        private void AddEmployeeForm_FormClosing(object sender, FormClosingEventArgs e) {
            // Revert message box colors to blue.
            MessageBoxAdv.MetroColorTable.BorderColor = System.Drawing.Color.DeepSkyBlue;
            MessageBoxAdv.MetroColorTable.YesButtonBackColor = System.Drawing.Color.DeepSkyBlue;
            MessageBoxAdv.MetroColorTable.NoButtonBackColor = System.Drawing.Color.DeepSkyBlue;
            MessageBoxAdv.MetroColorTable.OKButtonBackColor = System.Drawing.Color.DeepSkyBlue;
        }
    }
}
