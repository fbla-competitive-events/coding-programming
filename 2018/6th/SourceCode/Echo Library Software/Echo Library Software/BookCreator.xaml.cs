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
    /// Interaction logic for BookCreator.xaml
    /// </summary>
    public partial class BookCreator : Window
    {
        public BookCreator()
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
        private bool CanExecuteSaveBookCommand()
        {
            //Title is empty, book cannot be saved.
            if (string.IsNullOrEmpty(Title.TextBoxOverride))
                return false;
            //Author is empty, book cannot be saved.
            else if (string.IsNullOrEmpty(Author.TextBoxOverride))
                return false;
            //Genre is empty, book cannot be saved.
            else if (string.IsNullOrEmpty(BookGenre.Text))
                return false;
            //ISBN is empty, book cannot be saved.
            else if (string.IsNullOrEmpty(ISBN.TextBoxOverride))
                return false;

            return true;
        }

        //Saves data to list in MainWindow.
        public void BookSaveExecute()
        {
            //Users can copy and paste in illegal characters so errors must be handled.
            try
            {
                //Create new variable, parse out result beforehand due to C#'s structure.
                BookGenre _bookGenre;
                Enum.TryParse(BookGenre.Text, out _bookGenre);

                Book _book = new Book()
                {
                    Title = Title.TextBoxOverride,
                    Author = Author.TextBoxOverride,
                    BookGenre = _bookGenre,
                    ISBN = ISBN.TextBoxOverride
                };
                _book.GenerateUniqueID();

                MainWindow.books.Add(_book);

                //Close window after saving user.
                this.Close();
            }
            catch (FormatException e)
            {
                MessageBoxResult _messageBoxResult =
                    MessageBox.Show("Error: You've entered invalid input for something. Did you copy and paste data?", "Error");
            }
        }

        //Bound to button click.
        private void ButtonBookSaveExecute(object sender, RoutedEventArgs e)
        {
            //All fields aren't filled out, don't save.
            if (CanExecuteSaveBookCommand() == false)
            {
                MessageBoxResult _messageBoxResult =
                    MessageBox.Show("Error: You must fill out all parameters to save a new book", "Error");
            }
            else if (CanExecuteSaveBookCommand())
            {
                BookSaveExecute();
            }
        }
    }
}
