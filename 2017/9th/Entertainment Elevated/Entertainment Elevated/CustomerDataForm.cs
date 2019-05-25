using System;
using System.Drawing;
using System.Windows.Forms;
using System.Windows.Forms.DataVisualization.Charting;
using static Entertainment_Elevated.ChangeFormPanel;

namespace Entertainment_Elevated
{
    public partial class CustomerDataForm : Form, IPanelForm
    {
        public CustomerDataForm()
        {
            InitializeComponent();
            DayOfWeekComboBox.SelectedIndex = 0;
        }

        private void DayOfWeekComboBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            DayOfWeek dayOfWeek = (DayOfWeek)DayOfWeekComboBox.SelectedIndex;
            CustomerDataChart.Series.Clear();
            CustomerDataChart.ChartAreas[0].AxisX.Minimum = 0;
            CustomerDataChart.ChartAreas[0].AxisX.Maximum = 24;
            CustomerDataChart.ChartAreas[0].AxisX.Interval = 6;
            Series series = CustomerDataChart.Series.Add(DayOfWeekComboBox.Text);
            series.ChartType = SeriesChartType.Column;
            for (int i = 0; i < CustomerForm.CustomerAttendance[0].Length; i++)
            {
                if (CustomerForm.CustomerAttendance[DayOfWeekComboBox.SelectedIndex][i] != 0)
                    series.Points.AddXY(i, CustomerForm.CustomerAttendance[DayOfWeekComboBox.SelectedIndex][i]);
            }
        }

        private void MenuButton_Click(object sender, EventArgs e)
        {
            ChangeFormPanels<MenuForm>(sender);
        }

        private void CustomerButton_Click(object sender, EventArgs e)
        {
            ChangeFormPanels<CustomerForm>(sender);
        }

        public Panel Panel()
        {
            return CustomerDataFormPanel;
        }
    }
}
