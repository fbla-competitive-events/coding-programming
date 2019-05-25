using System;
using System.Windows.Forms;
using static Entertainment_Elevated.ChangeFormPanel;

namespace Entertainment_Elevated
{
    public partial class HelpForm : Form, IPanelForm
    {
        public HelpForm()
        {
            InitializeComponent();
            HelpLabel.Text = "This is an application to help your Family Entertainment Center\nrun and keep track of everything. "
                            + "The employee screen helps you \nkeep track of the information of employees. "
                            + "The schedule screen will \nlet you make shifts for employees and keep track of total pay \nand hours. "
                            + "The customer screen will keep track of your customers \nand how often they come in. "
                            + "In some screens, there will be a help \nbutton in the title bar for further help well as many tooltips.";
        }

        private void MenuButton_Click(object sender, EventArgs e)
        {
            ChangeFormPanels<MenuForm>(sender);
        }

        // To satisfy the IPanelForm interface requirement
        public Panel Panel()
        {
            return HelpFormPanel;
        }
    }
}
