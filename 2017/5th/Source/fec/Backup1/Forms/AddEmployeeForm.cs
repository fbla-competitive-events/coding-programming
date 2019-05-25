using Syncfusion.Windows.Forms;
using System;
using System.Collections.Generic;
using System.Windows.Forms;

namespace fec {

    public partial class AddEmployeeForm : MetroForm {

        // Holds the schedule combo boxes.
        List<ComboBox> scheduleComboBoxes = new List<ComboBox>(7);
        static Random random = new Random();

        public AddEmployeeForm() {
            InitializeComponent();
            this.Icon = Properties.Resources.fecIcon;

            SetupScheduleComboBoxes();
        }

        private void addButton_Click(object sender, EventArgs e) {

            if (ValidInput()) {
                string[] scheduleValues = new string[7];

                for (int i = 0; i < 7;i++) {
                    scheduleValues[i] = scheduleComboBoxes[i].Text;
                }

                int id = random.Next(int.MaxValue);
                bool exists = true;

                while (exists) {
                    exists = DatabaseWorker.IdExists(id);
                    if (!exists) {
                        DatabaseWorker.tempId = id;
                        DatabaseWorker.AddEmployee(id, nameTextBox.Text, jobTextBox.Text, addressTextBox.Text, phoneTextBox.Text, scheduleValues);
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
            if (nameTextBox.Text != string.Empty && jobTextBox.Text != string.Empty 
                && addressTextBox.Text != string.Empty && phoneTextBox.Text != string.Empty) {

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
        /// Print an error message according to fields not filled.
        /// </summary>
        private void PrintErrorMessage() {
            string error = "";

            if (nameTextBox.Text == string.Empty)
                error += "Please fill the Name field.\n";
            if (jobTextBox.Text == string.Empty)
                error += "Please fill the Job field.\n";
            if (addressTextBox.Text == string.Empty)
                error += "Please fill the Address field.\n";
            if (phoneTextBox.Text == string.Empty)
                error += "Please fill the Phone field.\n";
            if (mondayComboBox.SelectedIndex == 0)
                error += "Please select a schedule for Monday.\n";
            if (tuesdayComboBox.SelectedIndex == 0)
                error += "Please select a schedule for Tuesday.\n";
            if (wednesdayComboBox.SelectedIndex == 0)
                error += "Please select a schedule for Wednesday.\n";
            if (thursdayComboBox.SelectedIndex == 0)
                error += "Please select a schedule for Thursday.\n";
            if (fridayComboBox.SelectedIndex == 0)
                error += "Please select a schedule for Friday.\n";
            if (saturdayComboBox.SelectedIndex == 0)
                error += "Please select a schedule for Saturday.\n";
            if (sundayComboBox.SelectedIndex == 0)
                error += "Please select a schedule for Sunday.\n";

            MessageBoxAdv.MessageBoxStyle = MessageBoxAdv.Style.Metro;
            MessageBoxAdv.Show(this, error, "Error");
        }

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
    }
}
