using System;
using System.Globalization;
using System.IO;
using System.Threading;
using System.Windows.Forms;

namespace fec {

    static class Program {

        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main() {

            // Set culture info to US
            // This is done so that '.' is used instead of ',' for decimals.
            Thread.CurrentThread.CurrentCulture = new CultureInfo("en-US");

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            // If a database file does not exist, create a new one with empty tables.
            if (!File.Exists(DatabaseProperties.DATABASE_NAME) && !File.Exists(DatabaseProperties.ENCRYPTED_DATABASE_NAME)) {
                SplashScreenForm splashForm = new SplashScreenForm();
                splashForm.Show();

                // Execute startup events so that splash screen images are loaded.
                Application.DoEvents();

                DatabaseGenerator.GenerateDB();

                Application.Run(new fec_Main(splashForm));
            }

            // If an unencrypted datbase file exists, open the main form.
            else if (File.Exists(DatabaseProperties.DATABASE_NAME)) {

                SplashScreenForm splashForm = new SplashScreenForm();
                splashForm.Show();

                // Execute startup events so that splash screen images are loaded.
                Application.DoEvents();

                Application.Run(new fec_Main(splashForm));
            }

            // If an encrypted database file exists, prompt the user for a password.
            else if (File.Exists(DatabaseProperties.ENCRYPTED_DATABASE_NAME)) {
                EnterPasswordForm enterPasswordForm = new EnterPasswordForm();
                enterPasswordForm.Show();

                Application.Run();
            }
        }
    }
}
