using System;
using System.Drawing;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using static Entertainment_Elevated.ChangeFormPanel;

namespace Entertainment_Elevated
{
    public partial class ScheduleForm : Form, IPanelForm
    {
        private decimal hours = 0;
        private decimal pay = 0;

        public ScheduleForm()
        {
            InitializeComponent();

            // The first column in the GridView will have the employee names
            ScheduleDataGridView.Columns.Add("Name", "Name");

            // The initial starting day will be today
            DateTime today = DateTime.Today;

            // Add the next week into the GridView as columns
            for (int i = 0; i < 7; i++)
            {
                ScheduleDataGridView.Columns.Add(today.AddDays(i).ToShortDateString(), today.AddDays(i).ToShortDateString());
            }

            // Loop through all the employees and add a row for each of them
            for (int i = 0; i < EmployeeForm.Employees.Count; i++)
            {
                ScheduleDataGridView.Rows.Add();

                // Label the first cell in the row with the employee name
                ScheduleDataGridView.Rows[i].Cells[0].Value = EmployeeForm.Employees[i].ToString();

                // For each cell that is not a header for a row or a column
                // Add on a tooltip that helps explain how to enter in shifts
                for (int j = 1; j <= 7; j++)
                {
                    ScheduleDataGridView.Rows[i].Cells[j].ToolTipText = "eg:  9:00 AM-5:00 PM";
                }
            }

            DisplayShifts();
        }

        private void MenuButton_Click(object sender, EventArgs e)
        {
            ChangeFormPanels<MenuForm>(sender);
        }

        private void ScheduleCalendar_DateSelected(object sender, DateRangeEventArgs e)
        {
            // When the user selects a different date on the calendar
            // Move the whole GridView so it shows that day first and then the whole week afterwards
            for (int i = 1; i < ScheduleDataGridView.ColumnCount; i++)
            {
                ScheduleDataGridView.Columns[i].Name = ScheduleCalendar.SelectionStart.AddDays(i - 1).ToShortDateString();
                ScheduleDataGridView.Columns[i].HeaderText = ScheduleCalendar.SelectionStart.AddDays(i - 1).ToShortDateString();
            }

            DisplayShifts();
        }

        // This is run after the user finishes editing a cell
        private void ScheduleDataGridView_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            // Get the text that the user has edited
            // e is the cell that started this method
            string text = ScheduleDataGridView.Rows[e.RowIndex].Cells[e.ColumnIndex].Value.ToString().ToLower();

            // Return if the text is empty
            if (text == "")
                return;

            // Remove all whitespace in the text by using a regular expression
            text = Regex.Replace(text, @"\s+", "");

            // Get the name of the column which has the day of the shift

            DateTime day = DateTime.Parse(ScheduleDataGridView.Columns[e.ColumnIndex].Name);

            // Split the text at the hyphen
            string[] times = text.Split('-');

            Shift shift = new Shift();
            try
            {
                // Find the number of hours from the start of the day that the shift starts and ends
                int startHours = DateTime.Parse(times[0]).Hour;
                int endHours = DateTime.Parse(times[1]).Hour;
                int startMinutes = DateTime.Parse(times[0]).Minute;
                int endMinutes = DateTime.Parse(times[1]).Minute;
            
                // Create new DateTimes for the starting and ending of the shift and create the shift
                DateTime start = new DateTime(day.Year, day.Month, day.Day, startHours, startMinutes, 0);
                DateTime end = new DateTime(day.Year, day.Month, day.Day, endHours, endMinutes, 0);
                shift = new Shift(start, end);
            }
            catch
            {
                try
                {
                    int startHours = int.Parse(times[0]);
                    int endHours = int.Parse(times[1]) + 12;
                    DateTime start = new DateTime(day.Year, day.Month, day.Day, startHours, 0, 0);
                    DateTime end = new DateTime(day.Year, day.Month, day.Day, endHours, 0, 0);
                    shift = new Shift(start, end);
                }
                catch
                {
                    MessageBox.Show("Please check the entered text.");
                }
            }

            // Find which employee to add the shift to by searching through the Employee list
            string employeeName = ScheduleDataGridView.Rows[e.RowIndex].Cells[0].Value.ToString();
            foreach (Employee employee in EmployeeForm.Employees)
            {
                // Unfortunately, using employee names to compare employees could cause problems
                // If there are two employees with the exact same names
                // However, this will be extremely rare and the workaround to this problem would be complex
                if (employee.ToString() == employeeName)
                {
                    // First remove all shifts that are on this day
                    employee.Shifts.RemoveAll(s => s.OnSameDay(shift));

                    // Add the shift to the employee
                    employee.Shifts.Add(shift);
                    break;
                }
            }

            // Update the shifts
            DisplayShifts();
        }

        private void DisplayShifts()
        {
            // Clear the DataGridView
            for (int i = 1; i < ScheduleDataGridView.ColumnCount; i++)
            {
                for (int j = 0; j < ScheduleDataGridView.RowCount; j++)
                {
                    ScheduleDataGridView.Rows[j].Cells[i].Value = "";
                }
            }

            // Reset the hours and pay to recalculate them 
            hours = 0;
            pay = 0;

            // This loop runs in at least cubic worse case time with respect to the number of employees
            // Although this could cause problems if the number of employees or shifts grows large
            // It is highly unlikely for these to be high enough for this loop to cause lag
            
            // Loop through all of the employees
            foreach (Employee employee in EmployeeForm.Employees)
            {
                // Loop through each day
                for (int i = 0; i < 7; i++)
                {
                    DateTime day = ScheduleCalendar.SelectionStart.AddDays(i);

                    // Loop through the shifts
                    foreach (Shift shift in employee.Shifts)
                    {
                        // See if they match up with the current day
                        if (shift.StartTime.DayOfYear == day.DayOfYear && shift.StartTime.Year == day.Year)
                        {
                            // Add to the hours and pay
                            hours += shift.NumberHours();
                            pay += shift.Pay(employee.Payrate);

                            // Loop through the rows of the grid to add the shift onto the GridView
                            foreach (DataGridViewRow row in ScheduleDataGridView.Rows)
                            {
                                // Checking for correct employee
                                if (row.Cells[0].Value.ToString() == employee.ToString())
                                {
                                    string text = "";
                                    text = shift.StartTime.ToShortTimeString();
                                    text += "-";
                                    text += shift.EndTime.ToShortTimeString();

                                    // Increment by one since first cell is employee name
                                    row.Cells[i + 1].Value = text;
                                }
                            }
                        }
                    }
                }
            }

            // Update the labels to show correct total of hours of pay
            HoursLabel.Text = "Hours: " + hours.ToString();
            PayLabel.Text = "Pay: " + pay.ToString("C");
        }

        private void ReportButton_Click(object sender, EventArgs e)
        {
            // Show the preview of the document to be printed
            PrintPreviewDialog.Document = PrintDocument;
            PrintPreviewDialog.ShowDialog();
        }

        private void PrintDocument_PrintPage(object sender, System.Drawing.Printing.PrintPageEventArgs e)
        {
            // Clear all of the user selections to prepare for the printing of the schedule
            ScheduleDataGridView.ClearSelection();

            // Get the height of the GridView
            int height = ScheduleDataGridView.Rows.GetRowsHeight(DataGridViewElementStates.None);

            // Add the height of the column headers along with a bit of buffer room
            height += ScheduleDataGridView.ColumnHeadersHeight + 2;

            // Get the width of the GridView and add on a bit of buffer room 
            int width = ScheduleDataGridView.Columns.GetColumnsWidth(DataGridViewElementStates.None) + 3;

            // Resize the form of the GridView to the size specified above
            ScheduleDataGridView.FindForm().ClientSize = new Size(width, height);

            // Fill the whole form with the GridView
            // Fills the whole thing except for the buffer room
            ScheduleDataGridView.Dock = DockStyle.Fill;

            // Create a new bitmap that encompasses the whole GridView
            Bitmap bitmap = new Bitmap(ScheduleDataGridView.Width, ScheduleDataGridView.Height);

            // Draws the whole GridView onto the bitmap
            ScheduleDataGridView.DrawToBitmap(bitmap, new Rectangle(0, 0, ScheduleDataGridView.Width, ScheduleDataGridView.Height));

            // Reset the position of the GridView in the form
            ScheduleDataGridView.Dock = DockStyle.Top;

            // Reset the size of the form
            ScheduleDataGridView.FindForm().Width = GeneralForm.FORMWIDTH;
            ScheduleDataGridView.FindForm().Height = GeneralForm.FORMHEIGHT;

            // Draw the bitmap image onto the page
            e.Graphics.DrawImage(bitmap, 0, 0);

            // Create a font to write with 
            Font font = new Font("Arial", 14, FontStyle.Bold);

            // The distance between each line
            float lineHeightFloat = font.GetHeight();

            // Where to write
            // Shift vertical position down to bottom of bitmap
            float verticalPrintPosition = e.MarginBounds.Top + bitmap.Height;
            float horizontalPrintPosition = e.MarginBounds.Left;

            // Write the number of hours onto the page in the specified positions
            e.Graphics.DrawString("Number of hours: " + hours.ToString(), font, Brushes.Black, 
                horizontalPrintPosition, verticalPrintPosition);

            // Move the vertical position 2 times the line height to double space the information
            verticalPrintPosition += 2 * lineHeightFloat;

            // Write the total amount of pay for the week onto the report
            e.Graphics.DrawString("Amount of pay: " + pay.ToString("C"), font, Brushes.Black,
                horizontalPrintPosition, verticalPrintPosition);
        }

        // To satisfy the IPanelForm interface requirement
        public Panel Panel()
        {
            return ScheduleFormPanel;
        }

        private void HelpButton_Click(object sender, EventArgs e)
        {
            MessageBox.Show("You can create schedules for the employees here. "
                     + "Click and scroll through the calendar to find the week that you want to schedule. "
                     + "Add in the shift by simply typing in the box corresponding to the correct day and employee. "
                     + "The tooltip on the grid helps explain how to type shifts. "
                     + "You can also print a report to print the schedule for the week and total amount of pay and hours of that week.");
        }
    }
}
