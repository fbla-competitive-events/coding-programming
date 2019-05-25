using Syncfusion.Windows.Forms;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace fec {

    public partial class EncryptingDatabaseForm : MetroForm {

        public EncryptingDatabaseForm() {
            InitializeComponent();
        }

        /// <summary>
        /// When the form is loaded, center it to parent form.
        /// </summary>
        private void EncryptionDatabaseForm_Load(object sender, EventArgs e) {
            this.CenterToParent();
        }
    }
}
