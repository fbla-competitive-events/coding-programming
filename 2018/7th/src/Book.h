#pragma once
/**
	Definition of a Book.
	Books are used to store
	data.
*/
class Book
{
	char title[50];
	char author[25];
	int id;

public:

	int searchWeight;
	bool deleted;
	Book() {};
	Book(char title[50], char author[25], int id);
	char* getTitle();
	char* getAuthor();
	int getId();
};

