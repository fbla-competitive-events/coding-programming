using System;
using System.Globalization;
using System.Threading;
using System.Windows.Forms;

namespace fec {
    static class Program {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main() {
            CultureInfo ci = new CultureInfo("en-US");
            Thread.CurrentThread.CurrentCulture = ci;

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            SplashScreenForm splashForm = new SplashScreenForm();
            splashForm.Show();

            // Execute startup events so that splash screen images are loaded
            Application.DoEvents();

            // Open main form
            Application.Run(new fec_Main(splashForm));
        }
    }
}
