#pragma once
#include "Book.h"
#include "User.h"

/**
	Definition of the DataHandler class. Saves
	and reads data from files. Has functionality
	for encrypting saved data.
*/
class DataHandler
{
private:

	int numBooks;
	int numUsers;
	int highestBookId;
	int highestUserId;
	int timeMax = 10000000;

public:

	int fine;
	int daysOutStudent;
	int daysOutFaculty;
	int maxOutStudent;
	int maxOutFaculty;

	DataHandler();
	void ReadData(Book books[500], User users[300], char filename[200]);
	void SaveData(Book books[500], User users[300], char filename[200]);
	void printReport(Book books[500], User users[300], char filename[200]);
	Book getBookWithId(int id, Book books[500]);
	void addBook(Book books[500], User users[300], char title[50], char author[25]);
	void addUser(Book books[500], User users[300], char name[50], bool isStudent);
	int getNumBooks();
	int getNumUsers();
	int getCurrentTime();
	int getDays(int timeOut);
	char* encrypt(char message[200]);
	char* decrypt(char message[200]);
};

