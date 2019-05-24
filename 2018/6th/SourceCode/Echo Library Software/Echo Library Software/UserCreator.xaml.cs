using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace Echo_Library_Software
{
    /// <summary>
    /// Interaction logic for UserCreator.xaml
    /// </summary>
    public partial class UserCreator : Window
    {
        public UserCreator()
        {
            InitializeComponent();
        }

        private void OnPreviewNumbersOnly(object sender, TextCompositionEventArgs e)
        {
            e.Handled = !IsTextAllowed(e.Text);
        }

        private static bool IsTextAllowed(string text)
        {
            Regex _regex = new Regex("[^0-9.]+"); //regex that matches allowed text
            return !_regex.IsMatch(text);
        }

        private void CloseWindow(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

        //Determines whether a new user can be added to the list based on whether the boxes are filled in.
        private bool CanExecuteSaveUserCommand()
        {
            //Name box is empty, user cannot be saved.
            if (string.IsNullOrEmpty(Name.TextBoxOverride))
                return false;
            //Age box is empty, user cannot be saved.
            else if (string.IsNullOrEmpty(Age.TextBoxOverride))
                return false;
            //UserType is empty, user cannot be saved.
            else if (string.IsNullOrEmpty(UserType.Text))
                return false;
            //Email is empty, user cannot be saved.
            else if (string.IsNullOrEmpty(Email.TextBoxOverride))
                return false;
            //PhoneNumber is empty, user cannot be saved.
            else if (string.IsNullOrEmpty(PhoneNumber.TextBoxOverride))
                return false;

            return true;
        }

        //Saves data to list in MainWindow.
        public void UserSaveExecute()
        {
            //Users can copy and paste in illegal characters so errors must be handled.
            try
            {
                //Create new variable, parse out result beforehand. Workaround due to C#'s nuances.
                AccountType _accountType;
                Enum.TryParse(UserType.Text, out _accountType);

                MainWindow.users.Add(new User()
                {
                    Name = Name.TextBoxOverride,
                    Age = Convert.ToInt32(Age.TextBoxOverride),
                    AccountType = _accountType,
                    Email = Email.TextBoxOverride,
                    PhoneNumber = Convert.ToInt64(PhoneNumber.TextBoxOverride)
                });

                //Close window after saving user.
                this.Close();
            }
            catch (Exception e)
            {
                MessageBoxResult _messageBoxResult =
                    MessageBox.Show("Error: You've entered invalid input for something. Did you copy and paste data?", "Error");
            }
        }

        //Bound to button click.
        private void ButtonUserSaveExecute(object sender, RoutedEventArgs e)
        {
            //All fields aren't filled out, don't save.
            if (CanExecuteSaveUserCommand() == false)
            {
                MessageBoxResult _messageBoxResult =
                    MessageBox.Show("Error: You must fill out all parameters to save a new user", "Error");
            }
            else if (CanExecuteSaveUserCommand())
            {
                UserSaveExecute();
            }
        }
    }
}
