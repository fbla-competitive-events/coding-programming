#include "User.h"
#include "DataHandler.h"
#include <iostream>
using namespace std;

/**
	Constructor.

	@param name - name of User.
	@param id - id of User.
*/
User::User(char name[25], int id)
{
	strcpy_s(User::name, name);
	User::id = id;
	User::numOut = 0;
	User::deleted = false;
}

/**
	Checks in a book.

	@param index - index of Book to check in.
*/
void User::checkIn(int index)
{
	if (User::numOut <= 0 || index >= User::numOut)
		return;
	for (int i = index; i < User::numOut-1; i++)
	{
		User::out[i] = User::out[i + 1];
		User::dateOut[i] = User::dateOut[i + 1];
	}
	User::numOut--;

}

/**
	Accessors and mutators.
*/
char* User::getName()
{
	return User::name;
}
int User::getId()
{
	return User::id;
}

