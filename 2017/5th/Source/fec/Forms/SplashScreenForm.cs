using System.Windows.Forms;

namespace fec {

    public partial class SplashScreenForm : Form {

        public SplashScreenForm() {
            InitializeComponent();

            // Set the form icon.
            this.Icon = Properties.Resources.fecIcon;
        }

    }
}
