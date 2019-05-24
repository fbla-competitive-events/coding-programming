using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Diagnostics;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;
using System.Xml.Serialization;
using ThinkSharp.FeatureTouring.Models;

public enum AccountType { Student, Teacher }

public enum BookGenre
{
    ActionAndAdventure, Anthology, Art, Autobiography, Biography, Childrens,
    Comic, Cookbook, Dictionary, Drama, Encyclopedia, Fantasy, Fiction, Guide, History,
    Horror, Math, Mystery, Poetry, Religion, Romance, Satire, Science, ScienceFiction
}

namespace Echo_Library_Software
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        //ObservableCollection refreshes in GUI when changed.
        public static ObservableCollection<User> users = new ObservableCollection<User>();
        public static ObservableCollection<Book> books = new ObservableCollection<Book>();
        public static ObservableCollection<Checkout> checkouts = new ObservableCollection<Checkout>();

        //Declare and store all the data necessary for the tutorial system.
        Tour programTour = new Tour()
        {
            Name = "Echo Library Software Assistant",
            ShowNextButtonDefault = true,
            Steps = new[]
            {
                new Step("DAddUser", "Adding a user",
                    "This button is used to add a new user to your database. Clicking on it " +
                    "will open a new window that is used to define the new user."),

                new Step("DRemoveUser", "Removing a user",
                    "This button is used to remove a user from your database. " +
                    "Select a user by clicking on it in the list, and then press this button."),

                new Step("DSearchUser", "Searching for a user",
                    "This search bar can be used to filter out users that don't have the given word(s) in their name."),

                new Step("DUserList", "Looking at your users' data",
                    "This list shows every user in your database by name and account type. " +
                    "Double-click on any user to view more information on them."),

                new Step("DAddBook", "Adding a book",
                    "This button is used to add a book to your database. Clicking on it " +
                    "will open a new window that is used to define the new book."),

                new Step("DRemoveBook", "Removing a book",
                    "This button is used to remove a book from your database. \r\n" +
                    "Select a book by clicking on it in the list, and then press this button."),

                new Step("DSearchBook", "Searching for a book",
                    "This search bar can filter out books that don't have the given word(s) in their name." ),

                new Step("DBookList", "Looking at your books' data",
                    "This list shows every book in your database by title and author. \r\n" +
                    "Double-click on any book to view more information on it."),

                new Step("DAddCheckout", "Adding a checkout",
                    "This button is used to add a new checkout to your database. A 'checkout' records \r\n" +
                    "which user checked out which book along with the current date, and also generates \r\n" +
                    "a date that the book must be returned by. Select a user and book from their corresponding \r\n" +
                    "lists, and then press this button."),

                new Step("DRemoveCheckout", "Removing a checkout",
                    "This button is used to remove a checkout from your database. \r\n" +
                    "Select a checkout by clicking on it in the list, and then press this button."),

                new Step("DCheckoutList", "Looking at your checkout data",
                    "This list shows every checkout in your database by the user that checked the book out, \r\n" +
                    "the book that was checked out, the date the book was checked out, and the date the \r\n" +
                    "book should be returned by."),

                new Step("DSaveData", "Saving your data",
                    "This button is used to manually save your data to the lists that are used when the program is run. \r\n" +
                    "Note that there is a dynamic backup feature in this program that backs up all data every 3 minutes \r\n" +
                    "and stores it in the 'Data' folder under the name 'Backups'. These folders can be found in the directory \r\n" +
                    "of the program."),

                new Step("DBooksReport", "Generating a report of this week's checkouts",
                    "This button is used to output and open a text file containing all the books checked out this week. \r\n" +
                    "The text file can be found in the directory of the program"),

                new Step("DFinesReport", "Generating a report of current fines owed",
                    "This button is used to output and open a text file containing all books that have not been returned on time. \r\n" +
                    "Fees are calculated as a flat fee of $1 in addition to $0.50 for each day late."),

                new Step("DTutorial", "Learning to use Echo Library Software",
                    "This button is used to go through the tutorial, which will show the functionality and usage of every aspect of this program.")
            }
        };

        public MainWindow()
        {
            InitializeComponent();

            //Bind listviews to lists in code.
            UsersListView.ItemsSource = users;
            BooksListView.ItemsSource = books;
            CheckoutsListview.ItemsSource = checkouts;

            //Sets up searchbar filtering system for users.
            CollectionView _userView = (CollectionView) CollectionViewSource.GetDefaultView(UsersListView.ItemsSource);
            _userView.Filter = UserFilter;

            //Sets up searchbar filtering system for books.
            CollectionView _bookView = (CollectionView)CollectionViewSource.GetDefaultView(BooksListView.ItemsSource);
            _bookView.Filter = BookFilter;

            //Make sure all the necessary blank data files exist so we don't get any errors.
            CheckForDataFiles();
            //Load up whatever data is in the .txt files.
            DeserializeData(null, null);

            StartTimer();
        }

        #region UserMethods

        //Opens UserCreator window.
        private void OpenUserCreator(object sender, RoutedEventArgs e)
        {
            UserCreator _userCreator = new UserCreator();
            _userCreator.Show();
        }

        private void RemoveUser(object sender, RoutedEventArgs e)
        {
            //Loop through list
            foreach (User _user in users)
            {
                //Find if current item is equal to item selected in listview
                if (_user == UsersListView.SelectedItem)
                {
                    users.Remove(_user); //Remove item if it is

                    break;
                }
            }
        }

        //Returns new index of object we are searching for.
        private bool UserFilter(object item)
        {
            if (String.IsNullOrEmpty(UsersTextFilter.Text))
                return true;
            else
                return ((item as User).Name.IndexOf(UsersTextFilter.Text, StringComparison.OrdinalIgnoreCase) >= 0);
        }

        //Opens dialog box showing full data of selected user.
        private void ShowUserData(object sender, MouseButtonEventArgs e)
        {
            User _selectedUser = (User)UsersListView.SelectedItem;

            //Don't allow the method to execute if there is no selected user.
            if (_selectedUser == null) { return; }

            MessageBoxResult _messageBoxResult =
                MessageBox.Show(_selectedUser.DataToString(), "User Info");
        }

        //Method that is executed when the text inside the search box changes.
        private void UserSearchBarFilter(object sender, System.Windows.Controls.TextChangedEventArgs e)
        {
            CollectionViewSource.GetDefaultView(UsersListView.ItemsSource).Refresh();
        }
        #endregion UserMethods

        #region BookMethods

        //Opens UserCreator window.
        private void OpenBookCreator(object sender, RoutedEventArgs e)
        {
            BookCreator _bookCreator = new BookCreator();
            _bookCreator.Show();
        }

        private void RemoveBook(object sender, RoutedEventArgs e)
        {
            //Loop through list
            foreach (Book _book in books)
            {
                //Find if current item is equal to item selected in listview
                if (_book == BooksListView.SelectedItem)
                {
                    books.Remove(_book); //Remove item if it is

                    break;
                }
            }
        }

        //Returns new index of object we are searching for.
        private bool BookFilter(object item)
        {
            if (String.IsNullOrEmpty(BooksTextFilter.Text))
                return true;
            else
                return ((item as Book).Title.IndexOf(BooksTextFilter.Text, StringComparison.OrdinalIgnoreCase) >= 0);
        }

        //Opens dialog box showing full data of selected book.
        private void ShowBookData(object sender, MouseButtonEventArgs e)
        {
            Book _selectedBook = (Book)BooksListView.SelectedItem;

            //Don't allow the method to execute if there is no selected book.
            if (_selectedBook == null) { return;}

            MessageBoxResult _messageBoxResult =
                MessageBox.Show(_selectedBook.DataToString(), "Book Info");
        }

        //Contains search bar functionality for book list.
        private void BookSearchBarFilter(object sender, TextChangedEventArgs e)
        {
            CollectionViewSource.GetDefaultView(BooksListView.ItemsSource).Refresh();
        }
#endregion BookMethods

        #region SerializationMethods

        private void SerializeData(object sender, RoutedEventArgs e)
        {
            ObservableCollection<User> _documentUsers
                = new ObservableCollection<User>();
            string _savePath = AppDomain.CurrentDomain.BaseDirectory;

            foreach (User _user in users)
            {
                _documentUsers.Add(_user);
            }
            
            MiscHelper.SerializeObjectToXML(_documentUsers, _savePath + @"\Data\Users.txt");

            ObservableCollection<Book> _documentBooks
                = new ObservableCollection<Book>();

            foreach (Book _book in books)
            {
                _documentBooks.Add(_book);
            }

            MiscHelper.SerializeObjectToXML(_documentBooks, _savePath + @"\Data\Books.txt");

            ObservableCollection<Checkout> _documentCheckouts
                = new ObservableCollection<Checkout>();

            foreach (Checkout _checkout in checkouts)
            {
                _documentCheckouts.Add(_checkout);
            }

            MiscHelper.SerializeObjectToXML(_documentCheckouts, _savePath + @"\Data\Checkouts.txt");
        }

        public void DeserializeData(object sender, EventArgs e)
        {
            //Get the current directory.
            string _savePath = AppDomain.CurrentDomain.BaseDirectory;

            //Load all the data into arrays, so we can copy it into the ObservableCollections.
            User[] _loadedUsers = MiscHelper.DeserializeXMLFileToObject<User[]>(_savePath + @"\Data\Users.txt");
            Book[] _loadedBooks = MiscHelper.DeserializeXMLFileToObject<Book[]>(_savePath + @"\Data\Books.txt");
            Checkout[] _loadedCheckouts = MiscHelper.DeserializeXMLFileToObject<Checkout[]> (_savePath + @"\Data\Checkouts.txt");

            //Load UserSettings.
            MiscHelper.userSettings = 
                MiscHelper.DeserializeXMLFileToObject<UserSettings>(_savePath + @"\Data\UserSettings.txt");
            
            //Check that data was properly deserialized before adding it to the current lists.
            if (_loadedUsers != null && _loadedBooks != null && _loadedCheckouts != null)
            {
                //Add each item to the ObservableCollections to make sure it refreshes with the new data.
                foreach (User _user in _loadedUsers)
                {
                    users.Add(new User()
                    {
                        Name = _user.Name,
                        Age = _user.Age,
                        AccountType = _user.AccountType,
                        Email = _user.Email,
                        PhoneNumber = _user.PhoneNumber,
                        AmountBooksCheckedOut = _user.AmountBooksCheckedOut
                    });
                }
                foreach (Book _book in _loadedBooks)
                {
                    books.Add(new Book()
                    {
                        Title = _book.Title,
                        Author = _book.Author,
                        BookGenre = _book.BookGenre,
                        ISBN = _book.ISBN,
                        ID = _book.ID
                    });
                }
                foreach (Checkout _checkout in _loadedCheckouts)
                {
                    checkouts.Add(new Checkout()
                    {
                        User = _checkout.User,
                        Book = _checkout.Book,
                        DateOfCheckout = _checkout.DateOfCheckout,
                        DateOfReturn = _checkout.DateOfReturn
                    });
                }
            }
        }

        /// <summary>
        /// Makes sure .txt data files exist. If not, create them.
        /// </summary>
        private void CheckForDataFiles()
        {
            string _savePath = AppDomain.CurrentDomain.BaseDirectory;

            string _usersPath = _savePath + @"\Data\Users.txt";
            string _booksPath = _savePath + @"\Data\Books.txt";
            string _checkoutsPath = _savePath + @"\Data\Checkouts.txt";
            string _userSettingsPath = _savePath + @"\Data\UserSettings.txt";
            string _dataFolderPath = _savePath + @"\Data";
            string _backupFolderPath = _savePath + @"\Data\Backups";

            //Data folder doesn't exist, make it.
            DirectoryInfo _dataDirectoryInfo = new DirectoryInfo(_dataFolderPath);
            if (_dataDirectoryInfo.Exists == false)
            {
                _dataDirectoryInfo.Create();
            }

            //Bkacup folder doesn't exist, make it.
            DirectoryInfo _backupDirectoryInfo = new DirectoryInfo(_backupFolderPath);
            if (_backupDirectoryInfo.Exists == false)
            {
                _backupDirectoryInfo.Create();
            }

            if (File.Exists(_usersPath) == false)
            {
                FileInfo _fileInfo = new FileInfo(_usersPath);

                _fileInfo.Create().Dispose();
            }

            if (File.Exists(_booksPath) == false)
            {
                FileInfo _fileInfo = new FileInfo(_booksPath);

                _fileInfo.Create().Dispose();
            }

            if (File.Exists(_checkoutsPath) == false)
            {
                FileInfo _fileInfo = new FileInfo(_checkoutsPath);

                _fileInfo.Create().Dispose();
            }

            if (File.Exists(_userSettingsPath) == false)
            {
                string _defaultUserSettings =
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                    "<UserSettings xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\r\n  " +
                    "<TeacherBookLimit>7</TeacherBookLimit>\r\n  <StudentBookLimit>3</StudentBookLimit>\r\n  " +
                    "<MaxCheckoutLengthTeacher>28</MaxCheckoutLengthTeacher>\r\n  " +
                    "<MaxCheckoutLengthStudent>7</MaxCheckoutLengthStudent>\r\n" +
                    "</UserSettings>";

                File.WriteAllText(_userSettingsPath, _defaultUserSettings);
            }
        }

        /// <summary>
        /// This is essentially the serialize method, but we serialize to a different directory and add the date to the filename.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void SerializeDataBackup(object sender, CancelEventArgs e)
        {
            //Cache current directory.
            string _savePath = AppDomain.CurrentDomain.BaseDirectory;

            //Create necessary folder.
            DirectoryInfo _backupFolder = new DirectoryInfo(_savePath + @"\Data\Backups\Backup" + DateTime.Now.ToFileTime());
            _backupFolder.Create();

            ObservableCollection<User> _documentUsers
                = new ObservableCollection<User>();
            foreach (User _user in users)
            {
                _documentUsers.Add(_user);
            }

            MiscHelper.SerializeObjectToXML(_documentUsers, _backupFolder.FullName + @"\Users.txt");

            ObservableCollection<Book> _documentBooks
                = new ObservableCollection<Book>();

            foreach (Book _book in books)
            {
                _documentBooks.Add(_book);
            }

            MiscHelper.SerializeObjectToXML(_documentBooks, _backupFolder.FullName + @"\Books.txt");

            ObservableCollection<Checkout> _documentCheckouts
                = new ObservableCollection<Checkout>();

            foreach (Checkout _checkout in checkouts)
            {
                _documentCheckouts.Add(_checkout);
            }

            MiscHelper.SerializeObjectToXML(_documentCheckouts, _backupFolder.FullName + @"\Checkouts.txt");
        }

        /// <summary>
        /// This is essentially the serialize method, but we serialize to a different directory and add the date to the filename.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void SerializeDataBackup(object sender, EventArgs e)
        {
            //This is used with an eventhandler for the backup system. An overload of this type was needed.

            //Cache current directory.
            string _savePath = AppDomain.CurrentDomain.BaseDirectory;

            //Create necessary folder.
            DirectoryInfo _backupFolder = new DirectoryInfo(_savePath + @"\Data\Backups\Backup" + DateTime.Now.ToFileTime());
            _backupFolder.Create();

            ObservableCollection<User> _documentUsers
                = new ObservableCollection<User>();
            foreach (User _user in users)
            {
                _documentUsers.Add(_user);
            }

            MiscHelper.SerializeObjectToXML(_documentUsers, _backupFolder.FullName + @"\Users.txt");

            ObservableCollection<Book> _documentBooks
                = new ObservableCollection<Book>();

            foreach (Book _book in books)
            {
                _documentBooks.Add(_book);
            }

            MiscHelper.SerializeObjectToXML(_documentBooks, _backupFolder.FullName + @"\Books.txt");

            ObservableCollection<Checkout> _documentCheckouts
                = new ObservableCollection<Checkout>();

            foreach (Checkout _checkout in checkouts)
            {
                _documentCheckouts.Add(_checkout);
            }

            MiscHelper.SerializeObjectToXML(_documentCheckouts, _backupFolder.FullName + @"\Checkouts.txt");
        }
        #endregion

        #region CheckoutMethods

        private void CreateCheckout(object sender, RoutedEventArgs e)
        {
            //Make sure that the book is not already checked out.
            foreach (Checkout _checkout in checkouts)
            {
                if (_checkout.Book.ID == ((Book) BooksListView.SelectedItem).ID)
                {
                    MessageBoxResult _messageBoxResult =
                        MessageBox.Show("Error: The book selected has already been checked out by another person.", "Error");

                    return;
                }
            }

            if (UsersListView.SelectedItem != null && BooksListView.SelectedItem != null)
            {
               AccountType _accountType = ((User)UsersListView.SelectedItem).AccountType;

               int _lengthOfCheckout = (_accountType == AccountType.Student)
                   ? MiscHelper.userSettings.MaxCheckoutLengthStudent
                   : MiscHelper.userSettings.MaxCheckoutLengthTeacher;

                //User has already met the max amount of books they're allowed to check out.
                if (_accountType == AccountType.Student)
                {
                    if (GetUserFromDatabase((User) UsersListView.SelectedItem).AmountBooksCheckedOut >=
                        MiscHelper.userSettings.StudentBookLimit)
                    {
                        MessageBoxResult _messageBoxResult =
                            MessageBox.Show("Error: This user has met the checkout limit and can no longer borrow any books.", "Error");
                        return;
                    }
                }
                else if (_accountType == AccountType.Teacher)
                {
                    if (GetUserFromDatabase((User) UsersListView.SelectedItem).AmountBooksCheckedOut >=
                        MiscHelper.userSettings.TeacherBookLimit)
                    {
                        MessageBoxResult _messageBoxResult =
                            MessageBox.Show("Error: This user has met the checkout limit and can no longer borrow any books.", "Error");
                        return;
                    }
                }

               checkouts.Add(new Checkout()
               {
                   User = (User)UsersListView.SelectedItem,
                   Book = (Book)BooksListView.SelectedItem,
                   DateOfCheckout = DateTime.Now,
                   DateOfReturn = DateTime.Now.AddDays(_lengthOfCheckout)
               });

                //Track this book to make sure the user does not check too many books out.
                ((User) UsersListView.SelectedItem).AmountBooksCheckedOut++;
            }
            else
            {
                MessageBoxResult _messageBoxResult =
                    MessageBox.Show("Error: You must select a user, book, and date to save a new checkout", "Error");
            }
        }

        private void RemoveCheckout(object sender, RoutedEventArgs e)
        {
            foreach (Checkout _checkout in checkouts)
            {
                if (_checkout == CheckoutsListview.SelectedItem)
                {
                    checkouts.Remove(_checkout);

                    //Record the user as having one less book.
                    _checkout.User.AmountBooksCheckedOut--;

                    break;
                }
            }
        }

        private void ShowCheckoutData(object sender, MouseButtonEventArgs e)
        {
            Checkout _selectedCheckout = (Checkout)CheckoutsListview.SelectedItem;

            //Don't allow the method to execute if there is no selected book.
            if (_selectedCheckout == null) { return; }

            MessageBoxResult _messageBoxResult =
                MessageBox.Show(_selectedCheckout.DataToString(), "Checkout Info");
        }
        #endregion

        #region ReportMethods

        /// <summary>
        /// Creates a .txt file containing data on all the books checked out this week.
        /// </summary>
        private void GenerateBooksCheckedOutWeekly(object sender, RoutedEventArgs e)
        {
            DateTime _currentDateTime = DateTime.Now;
            string _output = "Books Checked Out This Week - \r\n \r\n";

            string _path = AppDomain.CurrentDomain.BaseDirectory + @"Data\WeeklyReport.txt";

            foreach (Checkout _checkout in checkouts)
            {
                //The book was checked out 7 or less days ago. Abs to make sure a date in the past doesn't go into negatives.
                if (Math.Abs((_checkout.DateOfCheckout - _currentDateTime).TotalDays) <= 7)
                {
                    _output += _checkout.DataToString(); //Add it to output string.
                    _output += "\r\n";
                    _output += "Book is due in: " + (_checkout.DateOfReturn - _checkout.DateOfCheckout).TotalDays +
                               " days";

                    //Add a buffer before the next item.
                    _output += "\r\n";
                    _output += "\r\n";
                }
            }

            //Write data to file.
            System.IO.File.WriteAllText(_path, _output);

            //Open newly created file.
            Process.Start(_path);
        }

        private void GenerateFineReport(object sender, RoutedEventArgs e)
        {
            DateTime _currentDateTime = DateTime.Now;
            string _output = "Owed Fines On Checkouts - \r\n \r\n";

            string _path = AppDomain.CurrentDomain.BaseDirectory + @"Data\FineReport.txt";

            foreach (Checkout _checkout in checkouts)
            {
                //The return date has been missed. CurrentDate minus DateOfReturn should be negative when the book is not overdue,
                //As it would mean DateOfReturn is still in the future.
                if ((_currentDateTime - _checkout.DateOfReturn).TotalDays > 0)
                {
                    double _daysOverdue = (_currentDateTime - _checkout.DateOfReturn).TotalDays;

                    //Fine a dollar flat fee and 50 cents for each day overdue.
                    //y = 1 + .25x
                    int _totalFine = (int)Math.Ceiling(1 + _daysOverdue * .50);

                    _output += _checkout.DataToString();
                    _output += "\r\n" + "Fine: $" + _totalFine;

                    //Add a buffer before the next item.
                    _output += "\r\n";
                    _output += "\r\n";
                }
            }

            //Write data to file.
            System.IO.File.WriteAllText(_path, _output);

            //Open newly created file.
            Process.Start(_path);
        }
        #endregion

        private void StartTimer()
        {
            //Setup timing system to create backups every 3 minutes.
            DispatcherTimer _dispatcherTimer = new DispatcherTimer();
            _dispatcherTimer.Tick += SerializeDataBackup; //Bind backup serialization method.
            _dispatcherTimer.Interval = new TimeSpan(0, 0, 3, 0);
            _dispatcherTimer.Start();
        }

        private void StartTour(object sender, RoutedEventArgs e)
        {
            programTour.Start();
        }

        /// <summary>
        /// Returns a reference to the user in the database with the same values as the inputted one.
        /// </summary>
        /// <returns></returns>
        private User GetUserFromDatabase(User itemToCheckAgainst)
        {
            foreach (User _user in users)
            {
                if (_user == itemToCheckAgainst)
                {
                    return _user;
                }
            }

            return null;
        }
    }

    #region DataClasses

    public class User
    {
        //Parameters must be properties or ListViews cannot read them.
        public string Name { get; set; }
        public int Age { get; set; }
        public AccountType AccountType { get; set; }
        public string Email { get; set; } 
        public long PhoneNumber { get; set; } 
        public int AmountBooksCheckedOut { get; set; }

        //Converts all data into a string to be shown in a dialogbox.
        public string DataToString()
        {
            return "Name: " + Name +
                   "\nAge: " + Age +
                   "\nAccount Type: " + AccountType +
                   "\nEmail: " + Email +
                   "\nPhone Number: " + PhoneNumber + 
                   "\nAmount of Books Checked Out: " + AmountBooksCheckedOut;
        }
    }

    public class Book
    {
        //Parameters must be properties or ListViews cannot read them.
        public string Title { get; set; }
        public string Author { get; set; }
        public BookGenre BookGenre { get; set; }
        public string ISBN { get; set; }

        //Todo: automatically generated by hashing title of book and adding some sort of unique number to the end.
        public string ID { get; set; }

        public string DataToString()
        {
            return "Title: " + Title +
                   "\nAuthor: " + Author +
                   "\nBook Genre: " + BookGenre +
                   "\nISBN: " + ISBN +
                   "\nID: " + ID;
        }

        public void GenerateUniqueID()
        {
            //Only generate an ID if one does not already exist.
            if (ID == null)
            {
                string _generatedId;

                //Hash title to generate the beginning of an ID.
                _generatedId = MiscHelper.FNVHash(Title).ToString();
                //Add a semicolon, meaning that the next 8 characters are the book itself's unique ID.
                _generatedId += ":";
                _generatedId += MiscHelper.EchoHash(8);

                ID = _generatedId;
            }

            else
                return;
        }
    }

    public class Checkout
    {
        //User that checked out the book in this instance of a checkout.
        public User User { get; set; }
        //Book that was checked out in this instance of a checkout.
        public Book Book { get; set; }

        //Store dates that indicate when the  book was checked out and when it must be returned by.
        public DateTime DateOfCheckout { get; set; }
        public DateTime DateOfReturn { get; set; }

        public string DataToString()
        {
            return "User: " + User.Name +
                   "\r\nBook: " + Book.Title +
                   "\r\nDate of checkout: " + DateOfCheckout.ToString() +
                   "\r\nBook due date: " + DateOfReturn.ToString();
        }
    }

    public class UserSettings
    {
        public UserSettings()
        {
            
        }

        public UserSettings(int teacherBookLimit, int studentBookLimit, 
                            int maxCheckoutLengthTeacher, int maxCheckoutLengthStudent)
        {
            TeacherBookLimit = teacherBookLimit;
            StudentBookLimit = studentBookLimit;
            MaxCheckoutLengthTeacher = maxCheckoutLengthTeacher;
            MaxCheckoutLengthStudent = maxCheckoutLengthStudent;
        }

        public int TeacherBookLimit { get; set; }
        public int StudentBookLimit { get; set; }

        public int MaxCheckoutLengthTeacher { get; set; }
        public int MaxCheckoutLengthStudent { get; set; }
    }
    #endregion
}
