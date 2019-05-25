using System;
using System.Windows.Forms;
using static Entertainment_Elevated.ChangeFormPanel;

namespace Entertainment_Elevated
{
    public partial class MenuForm : Form, IPanelForm
    {
        public MenuForm()
        {
            InitializeComponent();
        }
       
        private void EmployeeButton_Click(object sender, EventArgs e)
        {
            ChangeFormPanels<EmployeeForm>(sender);
        }

        private void ScheduleButton_Click(object sender, EventArgs e)
        {
            ChangeFormPanels<ScheduleForm>(sender);
        }

        private void CustomerButton_Click(object sender, EventArgs e)
        {
            ChangeFormPanels<CustomerForm>(sender);
        }

        private void HelpButton_Click(object sender, EventArgs e)
        {
            ChangeFormPanels<HelpForm>(sender);
        }

        // To satisfy the IPanelForm interface requirement
        public Panel Panel()
        {
            return MainFormPanel;
        }
    }
}