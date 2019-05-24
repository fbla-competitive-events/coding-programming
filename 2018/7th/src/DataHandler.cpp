#include "DataHandler.h"
#include <iostream>
#include <fstream>
#include <string>
#include "stdlib.h"

#include <stdio.h>
#include <time.h>
using namespace std;

/**
	Constructor.
*/
DataHandler::DataHandler()
{
}

/**
	Reads data from file, decrypts it, and stores
	it in the proper arrays.

	@param books - global array of Books.
	@param users - global array of Users.
	@param filename - file to be read from.
*/
void DataHandler::ReadData(Book books[500], User users[300], char filename[200])
{
	ifstream myfile;
	myfile.open(filename, ios::in);
	char input[50];
	char title[50];
	char author[25];
	char name[25];
	char id[15];
	int idVal;
	int parsed;
	bool isStudent;
	int fines;
	DataHandler::numBooks = 0;
	DataHandler::numUsers = 0;
	DataHandler::highestBookId = 0;
	DataHandler::highestUserId = 0;

	myfile >> input; DataHandler::decrypt(input);
	sscanf_s(input, "%d", &(DataHandler::fine));
	myfile >> input; DataHandler::decrypt(input);
	sscanf_s(input, "%d", &(DataHandler::daysOutStudent));
	myfile >> input; DataHandler::decrypt(input);
	sscanf_s(input, "%d", &(DataHandler::daysOutFaculty));
	myfile >> input; DataHandler::decrypt(input);
	sscanf_s(input, "%d", &(DataHandler::maxOutStudent));
	myfile >> input; DataHandler::decrypt(input);
	sscanf_s(input, "%d", &(DataHandler::maxOutFaculty));
	
	while (!myfile.eof())
	{
		myfile >> input; DataHandler::decrypt(input);
		if (strcmp(input, "Book") == 0 && DataHandler::numBooks < 500)
		{
			myfile >> input; DataHandler::decrypt(input); //Title
			myfile >> input; DataHandler::decrypt(input); //Start reading the title
			title[0] = '\0';
			while (strcmp(input, "Author") != 0)
			{
				strcat_s(title, input);
				strcat_s(title, " ");
				myfile >> input; DataHandler::decrypt(input);
			}
			//get rid of extra space
			if(strlen(title)>0)
				title[strlen(title) - 1] = '\0';
			myfile >> input; DataHandler::decrypt(input); //Start reading the author
			author[0] = '\0';
			while (strcmp(input, "Id") != 0)
			{
				strcat_s(author, input);
				strcat_s(author, " ");
				myfile >> input; DataHandler::decrypt(input);
			}
			//get rid of extra space
			if (strlen(author)>0)
				author[strlen(author) - 1] = '\0';
			myfile >> input; DataHandler::decrypt(input); //Start reading the id
			id[0] = '\0';
			while (strcmp(input, "End") != 0)
			{
				strcat_s(id, input);
				myfile >> input; DataHandler::decrypt(input);
			}
			sscanf_s(id, "%d", &idVal);
			books[DataHandler::numBooks] = Book(title, author, idVal);
			if (idVal > DataHandler::highestBookId)
				DataHandler::highestBookId = idVal;
			DataHandler::numBooks++;
			//cout << title << " | " << author << " | " << idVal << '\n';
		}
		if (strcmp(input, "User") == 0 && DataHandler::numUsers< 300)
		{
			myfile >> input; DataHandler::decrypt(input); //Type
			isStudent = false;
			if (strcmp(input, "Student") == 0)
				isStudent = true;
			myfile >> input; DataHandler::decrypt(input); //Name
			myfile >> input; DataHandler::decrypt(input); //Start reading the Name
			name[0] = '\0';
			while (strcmp(input, "Id") != 0)
			{
				strcat_s(name, input);
				strcat_s(name, " ");
				myfile >> input; DataHandler::decrypt(input);
			}
			//get rid of extra space
			if (strlen(name)>0)
				name[strlen(name) - 1] = '\0';
			myfile >> input; DataHandler::decrypt(input); //Start reading the id
			id[0] = '\0';
			while (strcmp(input, "Fines") != 0)
			{
				strcat_s(id, input);
				myfile >> input; DataHandler::decrypt(input);
			}
			sscanf_s(id, "%d", &idVal);
			myfile >> input; DataHandler::decrypt(input); //Start reading the fines
			id[0] = '\0';
			while (strcmp(input, "List") != 0)
			{
				strcat_s(id, input);
				myfile >> input; DataHandler::decrypt(input);
			}
			sscanf_s(id, "%d", &fines);
			users[DataHandler::numUsers] = User(name, idVal);
			users[DataHandler::numUsers].isStudent = isStudent;
			users[DataHandler::numUsers].fines = fines;
			if (idVal > DataHandler::highestUserId)
				DataHandler::highestUserId = idVal;
			myfile >> input; DataHandler::decrypt(input); //Start reading the list
			while (strcmp(input, "End") != 0)
			{
				sscanf_s(input, "%d", &parsed);
				users[DataHandler::numUsers].out[users[DataHandler::numUsers].numOut] = parsed;
				myfile >> input; DataHandler::decrypt(input);
				sscanf_s(input, "%d", &parsed);
				users[DataHandler::numUsers].dateOut[users[DataHandler::numUsers].numOut] = parsed;
				users[DataHandler::numUsers].numOut++;
				myfile >> input; DataHandler::decrypt(input);
			}
			//cout << name << " | " << idVal << " | " << isStudent << " | " << fines << '\n';
			for (int i = 0; i < users[DataHandler::numUsers].numOut; i++)
			{
				//cout << i << " | " << users[DataHandler::numUsers].out[i] << " | " << users[DataHandler::numUsers].dateOut[i] << '\n';
			}
			DataHandler::numUsers++;
		}
	}
	myfile.close();

}

/**
	Adds a Book to the global array of books.

	@param books - global array of Books.
	@param users - global array of Users.
	@param title - string containing the title
	of the Book to be added.
	@param author - string containing the author
	of the Book to be added.
*/
void DataHandler::addBook(Book books[500], User users[300], char title[50], char author[25])
{
	if (DataHandler::numBooks >= 500)
		return;
	DataHandler::highestBookId++;
	books[DataHandler::numBooks] = Book(title, author, DataHandler::highestBookId);
	DataHandler::numBooks++;
	DataHandler::SaveData(books, users, "Data/data.store");
}

/**
	Adds a User to the global array of users.

	@param books - global array of Books.
	@param users - global array of Users.
	@param name - string containing the name
	of the User to be added.
	@param isStudent - stores the type of the
	User to be added.
*/
void DataHandler::addUser(Book books[500], User users[300], char name[50], bool isStudent)
{
	if (DataHandler::numUsers >= 300)
		return;
	DataHandler::highestUserId++;
	users[DataHandler::numUsers] = User(name, DataHandler::highestUserId);
	users[DataHandler::numUsers].fines = 0;
	users[DataHandler::numUsers].isStudent = isStudent;
	users[DataHandler::numUsers].numOut = 0;
	DataHandler::numUsers++;
	DataHandler::SaveData(books, users, "Data/data.store");
}

/**
	Reads data from the program, encrypts it,
	and writes it to the file.

	@param books - global array of Books.
	@param users - global array of Users.
	@param filename - file to be written to.
*/
void DataHandler::SaveData(Book books[500], User users[300], char filename[200])
{
	char output[200];
	
	ofstream myfile;
	myfile.open(filename, ios::out);
	
	//myfile << DataHandler::fine << '\n';
	output[0] = '\0';  _itoa_s(DataHandler::fine, output, 10);
	myfile << DataHandler::encrypt(output) << '\n';

	//myfile << DataHandler::daysOutStudent << '\n';
	output[0] = '\0';  _itoa_s(DataHandler::daysOutStudent, output, 10);
	myfile << DataHandler::encrypt(output) << '\n';

	//myfile << DataHandler::daysOutFaculty << '\n';
	output[0] = '\0';  _itoa_s(DataHandler::daysOutFaculty, output, 10);
	myfile << DataHandler::encrypt(output) << '\n';

	//myfile << DataHandler::maxOutStudent << '\n';
	output[0] = '\0';  _itoa_s(DataHandler::maxOutStudent, output, 10);
	myfile << DataHandler::encrypt(output) << '\n';

	//myfile << DataHandler::maxOutFaculty << '\n';
	output[0] = '\0';  _itoa_s(DataHandler::maxOutFaculty, output, 10);
	myfile << DataHandler::encrypt(output) << '\n';
	
	for (int i = 0; i < DataHandler::numBooks; i++)
	{
		if (books[i].deleted == true)
			continue;
		// << "Book" << '\n';
		output[0] = '\0';
		strcpy_s(output, "Book");
		myfile << DataHandler::encrypt(output) << '\n';
		
		//myfile << "Title" << '\n';
		output[0] = '\0';
		strcpy_s(output, "Title");
		myfile << DataHandler::encrypt(output) << '\n';
		//myfile << books[i].getTitle() << '\n';
		output[0] = '\0';
		strcpy_s(output, books[i].getTitle());
		//cout << output << '\n';
		myfile << DataHandler::encrypt(output) << '\n';

		//myfile << "Author" << '\n';
		output[0] = '\0';
		strcpy_s(output, "Author");
		myfile << DataHandler::encrypt(output) << '\n';
		//myfile << books[i].getAuthor() << '\n';
		output[0] = '\0';
		strcpy_s(output, books[i].getAuthor());
		//cout << output << '\n';
		myfile << DataHandler::encrypt(output) << '\n';

		//myfile << "Id" << '\n';
		output[0] = '\0';
		strcpy_s(output, "Id");
		myfile << DataHandler::encrypt(output) << '\n';
		//myfile << books[i].getId() << '\n';
		output[0] = '\0';
		_itoa_s(books[i].getId(), output, 10);
		myfile << DataHandler::encrypt(output) << '\n';

		//myfile << "End" << '\n';
		output[0] = '\0';
		strcpy_s(output, "End");
		myfile << DataHandler::encrypt(output) << '\n';
	}
	for (int i = 0; i < DataHandler::numUsers; i++)
	{
		if (users[i].deleted == true)
			continue;
		//myfile << "User" << '\n';
		output[0] = '\0';
		strcpy_s(output, "User");
		myfile << DataHandler::encrypt(output) << '\n';

		/*
		if(users[i].isStudent)
			myfile << "Student" << '\n';
		else
			myfile << "Faculty" << '\n';
		*/
		output[0] = '\0';
		if (users[i].isStudent)
			strcpy_s(output, "Student");
		else
			strcpy_s(output, "Faculty");
		myfile << DataHandler::encrypt(output) << '\n';

		//myfile << "Name" << '\n';
		output[0] = '\0';
		strcpy_s(output, "Name");
		myfile << DataHandler::encrypt(output) << '\n';
		//myfile << users[i].getName() << '\n';
		output[0] = '\0';
		strcpy_s(output, users[i].getName());
		myfile << DataHandler::encrypt(output) << '\n';

		//myfile << "Id" << '\n';
		output[0] = '\0';
		strcpy_s(output, "Id");
		myfile << DataHandler::encrypt(output) << '\n';
		//myfile << users[i].getId() << '\n';
		output[0] = '\0';
		_itoa_s(users[i].getId(), output, 10);
		myfile << DataHandler::encrypt(output) << '\n';

		//myfile << "Fines" << '\n';
		output[0] = '\0';
		strcpy_s(output, "Fines");
		myfile << DataHandler::encrypt(output) << '\n';
		//myfile << users[i].fines << '\n';
		output[0] = '\0';
		_itoa_s(users[i].fines, output, 10);
		myfile << DataHandler::encrypt(output) << '\n';

		//myfile << "List" << '\n';
		output[0] = '\0';
		strcpy_s(output, "List");
		myfile << DataHandler::encrypt(output) << '\n';
		for (int j = 0; j < users[i].numOut; j++)
		{
			//myfile << users[i].out[j] << '\n';
			output[0] = '\0';
			_itoa_s(users[i].out[j], output, 10);
			myfile << DataHandler::encrypt(output) << '\n';
			//myfile << users[i].dateOut[j] << '\n';
			output[0] = '\0';
			_itoa_s(users[i].dateOut[j], output, 10);
			myfile << DataHandler::encrypt(output) << '\n';
		}

		//myfile << "End" << '\n';
		output[0] = '\0';
		strcpy_s(output, "End");
		myfile << DataHandler::encrypt(output) << '\n';
	}
	myfile.close();
}

void DataHandler::printReport(Book books[500], User users[300], char filename[200])
{
	int maxDaysOut;
	int daysOut;
	int totalFines;
	char temp3[50];
	ofstream myfile;
	myfile.open(filename, ios::out);
	myfile << "REPORT\n";
	for (int i = 0; i < DataHandler::numUsers; i++)
	{
		totalFines = 0;
		myfile << "\n";
		
		if (users[i].isStudent)
		{
			myfile << users[i].getName() << " (Student)" << " (" << users[i].numOut << ") ";
			maxDaysOut = DataHandler::daysOutStudent;
		}
		else
		{
			myfile << users[i].getName() << " (Faculty)" << " (" << users[i].numOut << ") ";
			maxDaysOut = DataHandler::daysOutFaculty;
		}
		for (int j = 0; j < users[i].numOut; j++)
		{
			daysOut = DataHandler::getDays(users[i].dateOut[j]);
			if (maxDaysOut - daysOut < 0)
				totalFines += DataHandler::fine*(daysOut - maxDaysOut);
		}
		myfile << "Fines: $";
		totalFines += users[i].fines;
		int dollars = totalFines / 100;
		int cents = totalFines % 100;
		temp3[0] = '\0';
		_itoa_s(dollars, temp3, 10);
		myfile << dollars <<".";
		temp3[0] = '\0';
		_itoa_s(cents, temp3, 10);
		if (cents<10)
			myfile << "0";
		myfile << cents << '\n';
		
		myfile << '\n';
		for (int j = 0; j < users[i].numOut; j++)
		{
			daysOut = DataHandler::getDays(users[i].dateOut[j]);
			if (maxDaysOut - daysOut > 0)
			{
				myfile << "\t- " << DataHandler::getBookWithId(users[i].out[j], books).getTitle();
				myfile << " by " << DataHandler::getBookWithId(users[i].out[j], books).getAuthor();
				myfile << " (" << users[i].out[j] << "). Due in " << (maxDaysOut - daysOut) << " days." << '\n';
			}
			if (maxDaysOut - daysOut == 0)
			{
				myfile << "\t- " << DataHandler::getBookWithId(users[i].out[j], books).getTitle();
				myfile << " by " << DataHandler::getBookWithId(users[i].out[j], books).getAuthor();
				myfile << " (" << users[i].out[j] << "). Due today." << '\n';
			}
			if (maxDaysOut - daysOut < 0)
			{
				myfile << "\t- " << DataHandler::getBookWithId(users[i].out[j], books).getTitle();
				myfile << " by " << DataHandler::getBookWithId(users[i].out[j], books).getAuthor();
				myfile << " (" << users[i].out[j] << "). OVERDUE by " << (daysOut - maxDaysOut) << " days." << '\n';
			}

		}

	}
	myfile.close();
	char temp2[200];
	temp2[0] = '\n';
	strcat_s(temp2, "start notepad.exe ");
	strcat_s(temp2, filename);
	system(temp2);
}

Book DataHandler::getBookWithId(int id, Book books[500])
{
	for (int i = 0; i < DataHandler::numBooks; i++)
		if (books[i].getId() == id)
			return books[i];
}

/**
	Get the current time of day in seconds
	mod a large number.

	@return the time in seconds mod a large 
	number.
*/
int DataHandler::getCurrentTime()
{

	time_t seconds;
	seconds = time(NULL);
	int t = seconds % DataHandler::timeMax;
	return t;
}

/**
	Get the number of days a book was checked
	out for.

	@param timeOut - the time the book was 
	checked out in seconds.
	@return the number of days since timeOut.
*/
int DataHandler::getDays(int timeOut)
{
	int secInDay = 86400;
	if (DataHandler::getCurrentTime() < timeOut)
		return (DataHandler::getCurrentTime() + timeMax - timeOut)/secInDay;
	return (DataHandler::getCurrentTime() - timeOut) / secInDay;

}

/**
	Encrypt a string.

	Implements a basic Caesar Cipher to make
	files difficult to change by the common 
	person.

	@param message - string to be encrypted.
	@return the encrypted string.
*/
char* DataHandler::encrypt(char message[200])
{
	char out[200];
	int startLen = strlen(message);
	for (int i = 0; i < strlen(message); i++)
	{
		out[i] = char(int(message[i]) - 10);
	}
	out[strlen(message)] = '\0';
	for (int i = 0; i < strlen(message); i++)
	{
		message[i] = out[i];
	}
	message[startLen] = '\0';
	return message;
}

/**
	Decrypt a string.

	Implements a basic Caesar Cipher to make
	files difficult to change by the common 
	person.

	@param message - string to be decrypted.
	@return the decrypted string.
*/
char* DataHandler::decrypt(char message[200])
{
	char out[200];
	for (int i = 0; i < strlen(message); i++)
	{
		out[i] = char(int(message[i]) + 10);
	}
	out[strlen(message)] = '\0';
	for (int i = 0; i < strlen(message); i++)
	{
		message[i] = out[i];
	}
	return message;
}

/**
	Accessors and mutators.
*/
int DataHandler::getNumBooks()
{
	return DataHandler::numBooks;
}
int DataHandler::getNumUsers()
{
	return DataHandler::numUsers;
}

