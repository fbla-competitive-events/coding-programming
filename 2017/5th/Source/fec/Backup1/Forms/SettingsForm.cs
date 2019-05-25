using Syncfusion.Windows.Forms;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace fec {
    public partial class SettingsForm : MetroForm {
        public SettingsForm() {
            InitializeComponent();
        }

        private void compactDatabaseButton_Click(object sender, EventArgs e) {
            DatabaseWorker.CompactDatabase();
        }
    }
}
