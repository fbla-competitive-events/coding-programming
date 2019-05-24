#pragma once
/**
	Definition of a User.
	Users are used to store
	data.
*/
class User
{
	char name[25];
	int id;
public:
	int searchWeight;
	int out[15];
	int dateOut[15];
	int numOut;
	bool isStudent;
	int fines;
	bool deleted;
	User() {};
	User(char name[25], int id);
	char* getName();
	int getId();
	void checkIn(int index);
};

