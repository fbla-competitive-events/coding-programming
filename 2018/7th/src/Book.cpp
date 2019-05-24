#include "Book.h"
#include <iostream>
using namespace std;
Book::Book(char title[50], char author[25], int id)
{
	strcpy_s(Book::title, title);
	strcpy_s(Book::author, author);
	Book::id = id;
	Book::deleted = false;
}
char* Book::getTitle()
{
	return Book::title;
}
char* Book::getAuthor()
{
	return Book::author;
}
int Book::getId()
{
	return Book::id;
}
