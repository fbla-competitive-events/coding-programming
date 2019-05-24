from django.db import models
import datetime
# CREATE MODELS
#A model is django's term for table, which after they are created here, are registered in admin.py
#After being created, they are migrated into PostgreSQL (In my case), only for storage.

#Create the table to store all books in:
class usertable2(models.Model):
    #Choices created for user type, S for Student and F for Faculty/Teachers
    STUDENT = 'S'
    FACULTY = 'F'
    BOOK_S = 3
    BOOK_F = 5
    DAY_S = 7
    DAY_F = 14
    USER_TYPE_CHOICES = (
        (STUDENT, 'S'),
        (FACULTY, 'F'),
    )
    #The book checkout limit choices
    USER_CHECKOUT_BOOK_LIMIT = (
        (BOOK_S, 3),
        (BOOK_F, 5),
    )
    #The day amount choices that you may check out a book for, (7 for Student and 14 for Faculty)
    USER_CHECKOUT_DAY_LIMIT = (
        (DAY_S, 7),
        (DAY_F, 14),
    )
    #Create the fields that will be used and displayed of the users: Last name, first name, middle name, id, type, and all limits.
    ######################################################################################
    #There is a foreign-key 1 to 1 relationship between the user table and the book table#
    ######################################################################################
    user_last_name = models.CharField("User's Last Name:", max_length=20, null=False, help_text='*The last name of the user')
    user_first_name = models.CharField("User's First Name:", max_length=20, null=False, help_text='*The first name of the user')
    user_middle_name = models.CharField("User's Middle Name:", max_length=20, null=True, blank=True, help_text='*The middle name of the user')
    user_id = models.CharField("User's Unique ID:", max_length=6, primary_key=True, help_text='*The unique ID assigned to each user. first 3 letters of last name, number of last name ex: Bill Gates would be gat001, then if there was another person with the last name Gates, that person would be gat002')
    user_type = models.CharField("Student or Faculty:", max_length=1, null=False, choices=USER_TYPE_CHOICES, default='S', help_text='"S" Indicates Student, "F" Indicates Faculty (Teachers). Students may only view information of books, and not view other users, while Faculty may edit, add, or delete information of books AND users.')
    user_book_checkout_limit = models.IntegerField("Book Limit:", null=False, choices=USER_CHECKOUT_BOOK_LIMIT, default=3, help_text='The number of books this user is able to check out.')
    user_book_check_out_days_limit = models.IntegerField("Max Checkout Days:", null=False, choices=USER_CHECKOUT_DAY_LIMIT, default=7, help_text='The number of days this user is able to check out books before the book becomes Overdue and starts accumulating a fine. (The fine is automatically calculated, with $0.20 added for every day it is overdue.)')
    def __str__(self):
        return self.user_id
    #The plural name that people accessing the library see of users
    class Meta:
        verbose_name_plural = 'List of Current Library Users'


#Create the fields that will be used and displayed of the users: Title, author, id, if its in library, date it was checked out, when its due, days until, and who has it currently.
class booktable2(models.Model):
    book_title = models.CharField("Title of Book:", max_length=100, null=False, help_text='*The Title of the book.')
    book_author = models.CharField("Author of Book:", max_length=40, null=False, help_text='*The Author of the book.')
    book_id = models.CharField("Unique ID of Book:", max_length=3, primary_key=True, help_text='*The unique ID number of the book. (From 001-999)')
    in_library = models.BooleanField("In Library:", null=False, default=True, help_text='*(When the checkbox is checked, the book is currently in the library)')
    #Foreign Key, makes this database relational as it connects to the User Table to get ID's
    user_id = models.ForeignKey(usertable2, on_delete=models.SET_NULL, null=True, default='INLIBR', help_text='The User ID of the person who has checked out this book. (Click on the drop down menu to select a user) Once a User is selected you may click on the pencil icon to edit the profile of that user, the green plus icon to add a user, or the red X icon to remove the user.')
    checked_out_date = models.DateField("The date the Book was Checked Out:", blank=True, null=True, help_text="*yyy-mm-dd The date the book was checked out from the library. Click on 'Today' if the book is being checked out right now. (Click on the calendar icon to select the date from a calendar)")
    book_due_date = models.DateField("The Date the Book is Due Back:", blank=True, null=True, help_text='*yyyy-mm-dd The date the book is due back to the library. (Click on the calendar icon to select the date from a calendar)')
    days_until_due = models.IntegerField("Days Until the Book is Due:", null=True, blank=True, editable=False, help_text='The number of days until the book is due back to the library.')

    #Function that calculates and returns the number of days until a book is due dependent on today's date and when it is due
    #Returns OVERDUE if overdue
    def calc_days_until_due(self):
        if self.book_due_date != None:
            if self.book_due_date > datetime.date.today():
                delta = self.book_due_date - datetime.date.today()
                return delta.days
            else:
                return "OVERDUE"

    #Function that calculates and returns the fine amount dependent on whether a book is due, and for how long it was due for.
    #Multiplies number of days it has been overdue by 0.2 to return a dollar amount, incrementing by 20 cents a day
    def calc_fine(self):
        if self.book_due_date != None:
            if self.book_due_date < datetime.date.today():
                calcdays = abs(self.book_due_date - datetime.date.today())
                calcfine = float(calcdays.days * 0.2)
                return "$" + str("%.2f" % round(calcfine, 2))

    def user(self):
        return self.user_id

    #The names that display from the values returned from the function above i.e. instead of 'calc_fine' it will look like 'Book Fine'
    class Meta:
        verbose_name_plural = 'Catalog Of Books'
    calc_days_until_due.short_description = 'Days Until Due'
    calc_fine.short_description = 'Book Fine'
