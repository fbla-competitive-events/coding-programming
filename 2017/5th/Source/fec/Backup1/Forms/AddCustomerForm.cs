using Syncfusion.Windows.Forms;
using System;
using System.Collections.Generic;
using System.Windows.Forms;

namespace fec {
    public partial class AddCustomerForm : MetroForm {

        // Holds the customer attendance combo boxes
        List<ComboBox> attendanceComboBoxes = new List<ComboBox>(7);
        static Random random = new Random();

        public AddCustomerForm() {
            InitializeComponent();
            this.Icon = Properties.Resources.fecIcon;

            SetupComboBoxes();
        }

        private void addButton_Click(object sender, EventArgs e) {
            if (ValidInput()) {
                string[] attendanceValues = new string[7];

                for (int i = 0;i < 7;i++) {
                    attendanceValues[i] = attendanceComboBoxes[i].Text;
                }

                int id = random.Next(Int32.MaxValue);
                bool exists = true;

                while (exists) {
                    exists = DatabaseWorker.IdExists(id);
                    if (!exists) {
                        DatabaseWorker.tempId = id;
                        DatabaseWorker.AddCustomer(id, nameTextBox.Text, membershipComboBox.SelectedItem.ToString(), phoneTextBox.Text, attendanceValues);
                    }
                    id = random.Next(Int32.MaxValue);
                }

                DialogResult = DialogResult.OK;
                Close();
            } else {
                PrintErrorMessage();
            }
        }

        private void nameTextBox_KeyPress(object sender, KeyPressEventArgs e) {
            if (!char.IsControl(e.KeyChar) && (e.KeyChar != ' ') && !char.IsLetter(e.KeyChar)) {
                e.Handled = true;
            }
        }

        private void phoneTextBox_KeyPress(object sender, KeyPressEventArgs e) {
            if (!char.IsControl(e.KeyChar) && !char.IsDigit(e.KeyChar)) {
                e.Handled = true;
            }
        }

        /// <summary>
        /// Checks whether the form is completely filled with input.
        /// </summary>
        /// <returns> Whether the form is completely filled with input. </returns>
        private bool ValidInput() {
            if (nameTextBox.Text != string.Empty && membershipComboBox.SelectedIndex != 0
                && phoneTextBox.Text != string.Empty) {

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
        /// Print an error message according to fields not filled.
        /// </summary>
        private void PrintErrorMessage() {
            string error = "";

            if (nameTextBox.Text == string.Empty)
                error += "Please fill the Name field.\n";
            if (membershipComboBox.SelectedIndex == 0)
                error += "Please select a value for Membership.\n";
            if (phoneTextBox.Text == string.Empty)
                error += "Please fill the Phone field.\n";
            if (mondayComboBox.SelectedIndex == 0)
                error += "Please select attendance for Monday.\n";
            if (tuesdayComboBox.SelectedIndex == 0)
                error += "Please select attendance for Tuesday.\n";
            if (wednesdayComboBox.SelectedIndex == 0)
                error += "Please select attendance for Wednesday.\n";
            if (thursdayComboBox.SelectedIndex == 0)
                error += "Please select attendance for Thursday.\n";
            if (fridayComboBox.SelectedIndex == 0)
                error += "Please select attendance for Friday.\n";
            if (saturdayComboBox.SelectedIndex == 0)
                error += "Please select attendance for Saturday.\n";
            if (sundayComboBox.SelectedIndex == 0)
                error += "Please select attendance for Sunday.\n";

            MessageBoxAdv.MessageBoxStyle = MessageBoxAdv.Style.Metro;
            MessageBoxAdv.Show(this, error, "Error");
        }

        /// <summary>
        /// Add schedule combo boxes to the schedule combo box list and set their initial index to their first item.
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
    }
}
