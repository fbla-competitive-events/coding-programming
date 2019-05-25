using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Fbla_Lib
{
    public partial class Help : Form
    {
        public Help()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Gets SelectedIndex to Open the right help menu
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void listHelp_SelectedIndexChanged(object sender, EventArgs e)
        {
            string help = listHelp.SelectedItem.ToString();
            //MessageBox.Show("JakeSmith: "+help); //TEST CODE REMOVE

            //Gets User Select to load the right Help Screen
            if (help == "How To Add A New Book")
            {
                //Load This... book1.html
                string book1 = Properties.Resources.book1_html;
                HelpDisplay.DocumentText = book1;

            }
            //Gets User Select to load the right Help Screen
            if (help == "How To Add A New Staff")
            {
                //Load This... staff1.html
                string staff1 = Properties.Resources.staff1_html;
                HelpDisplay.DocumentText = staff1;
            }
            //Gets User Select to load the right Help Screen
            if (help == "How To Add A New Student")
            {
                //Load This... student1.html
                string student1 = Properties.Resources.student1_html;
                HelpDisplay.DocumentText = student1;

            }
            //Gets User Select to load the right Help Screen
            if (help == "Checking out a Staff's Book")
            {
                //Load This... staff2.html
                string staff2 = Properties.Resources.staff2_html;
                HelpDisplay.DocumentText = staff2;

            }
            //Gets User Select to load the right Help Screen
            if (help == "Checking out a Student's Book")
            {
                //Load This... student2.html
                string student2 = Properties.Resources.student2_html;
                HelpDisplay.DocumentText = student2;

            }
            //Gets User Select to load the right Help Screen
            if (help == "Returning a Staff's Book")
            {
                //Load This... staff3.html
                string staff3 = Properties.Resources.staff3_html;
                HelpDisplay.DocumentText = staff3;

            }
            //Gets User Select to load the right Help Screen
            if (help == "Returning a Student's Book")
            {
                //Load This... student3.html
                string student3 = Properties.Resources.student3_html;
                HelpDisplay.DocumentText = student3;

            }

        }
    }
}
