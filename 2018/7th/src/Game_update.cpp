#include <SFML/Graphics.hpp>
#include "Game.h"
#include <iostream>
using namespace std;

/**
	Main update function. Controls the updating
	of main Game Elements.

	Updates different Game Elements depending
	on the state of the Game.
*/
void Game::update()
{
	Game::updateEvents();
	Game::updateWindow();
	Game::updateTyped();
	Game::updateFPS();

	
	//Game Elements//
	Game::updateInfo();
	Game::Title.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
	Game::updateMenu();
	if (Game::menuCurrent == 0 || Game::menuCurrent == 1)
	{
		Game::updateSearchClick();
		if (Game::infoWindowActive == 0)
		{
			Game::userInput.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
			if (strlen(Game::userInput.getSaved()) > 0)
			{
				if (Game::menuCurrent == 0)
					strcpy_s(Game::bQuery, Game::userInput.getSaved());
				if (Game::menuCurrent == 1)
					strcpy_s(Game::uQuery, Game::userInput.getSaved());
				Game::updateMainSearch();
				Game::userInput.resetSaved();
			}
			Game::mainSearch.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps, Game::keyPressed[int(sf::Keyboard::Up)], Game::keyPressed[int(sf::Keyboard::Down)], Game::userInput.getSize().y, Game::mouseDist);
		}
	}
	if (Game::menuCurrent == 2 || Game::menuCurrent == 3)
	{
		Game::updateForm();
	}
	if (Game::menuCurrent == 4)
	{
		Game::updateSettings();
	}
	if(Game::transitionPeriod >0)
		Game::transitionPeriod--;
	Game::fullyStarted--;
	if (Game::fullyStarted < 0)
		Game::fullyStarted = 0;

	
}

/**
	Sets the state of the Info Game Element.

	@param text - text to displayed by the box.
	@param type - the type of message.
*/
void Game::setInfo(char text[200], int type)
{
	Game::infoBoxCoolDown = fps * 4;
	if (Game::infoBoxCoolDown < 350)
		Game::infoBoxCoolDown = 250;
	Game::Info.TextBoxs[0].changeText(text);
	if(type < 0)
		Game::Info.TextBoxs[0].setColor(sf::Color(50, 25, 25, 200), sf::Color(255, 255, 255, 255), sf::Color(5, 5, 5, 255));
	if(type == 0)
		Game::Info.TextBoxs[0].setColor(sf::Color(25, 25, 25, 200), sf::Color(255, 255, 255, 255), sf::Color(5, 5, 5, 255));
	if (type > 0)
		Game::Info.TextBoxs[0].setColor(sf::Color(25, 50, 25, 200), sf::Color(255, 255, 255, 255), sf::Color(5, 5, 5, 255));
}

/**
	Updates the Info Game Element.

	If a message is active, updates the
	box to be displayed.
*/
void Game::updateInfo()
{
	Game::infoBoxCoolDown--;

	if (Game::Info.TextBoxs[0].getActive() == true && Game::infoBoxCoolDown > Game::fps && Game::fullyStarted == 0)
		Game::infoBoxCoolDown = Game::fps-1;

	if (Game::infoBoxCoolDown < Game::fps)
		Game::Info.setAlpha(((Game::infoBoxCoolDown * 225) / Game::fps));
	else
		Game::Info.setAlpha(225);

	Game::Info.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
}

/**
	Fint the index of the Book with a certain
	id in the books array.

	@param id - id of book to be found.
	@return the Book with the matching id.
*/
Book Game::getBookWithId(int id)
{
	for (int i = 0; i < Game::fileio.getNumBooks(); i++)
		if (Game::books[i].getId() == id)
			return Game::books[i];
}

/**
	If the Book or User search is active, detects
	and processes when a Book or User is pressed.
*/
void Game::updateSearchClick()
{
	if (Game::infoWindowActive == 0)
	{
		for (int i = 0; i < Game::mainSearch.numel; i++)
		{
			if (Game::transitionPeriod != 0)
				break;
			if (Game::mainSearch.chat[i].getSelected() == true && Game::mousePos.y < Game::windowSize.y - 80)
			{
				int parsedID;
				char parseString[25];
				parseString[0] = '\0';
				strcpy_s(parseString, Game::mainSearch.chat[i].TextBoxs[1].getText());
				for (int j = 3; j < strlen(parseString); j++)
				{
					parseString[j - 3] = parseString[j];
				}
				parseString[strlen(parseString) - 3] = '\0';
				sscanf_s(parseString, "%d", &parsedID);
				
				if (Game::menuCurrent == 0)
				{
					for (int j = 0; j < Game::fileio.getNumBooks(); j++)
						if (Game::books[j].getId() == parsedID)
							Game::indexWindowElement = j;
				}
				if (Game::menuCurrent == 1)
				{
					for (int j = 0; j < Game::fileio.getNumUsers(); j++)
						if (Game::users[j].getId() == parsedID)
							Game::indexWindowElement = j;

					Game::bookDisplay.setJumpToTop();
				}
				if (Game::selection == false)
				{
					Game::chatIndexWindowElement = i;
					Game::infoWindowActive = Game::menuCurrent + 1;
					Game::mainSearch.chat[i].setSelectecd(false);
					Game::transitionPeriod = Game::fps;
				}
				else
				{
					if (Game::menuCurrent == 0)
					{
						if (Game::indexOfUserWithBook(parsedID) == -1)
						{
							Game::users[Game::indexSelected].out[Game::users[Game::indexSelected].numOut] = parsedID;
							Game::users[Game::indexSelected].dateOut[Game::users[Game::indexSelected].numOut] = Game::fileio.getCurrentTime();
							Game::users[Game::indexSelected].numOut++;
							Game::selection = false;

							Game::infoWindowActive = 0;
							Game::menuCurrent = 1;
							Game::menuLast = 1;
							Game::menu[0].TextBoxs[0].setSelected(false);
							Game::menu[1].TextBoxs[0].setSelected(true);
							Game::mainSearch.title.changeText("User Search");
							Game::transitionPeriod = fps;
							Game::fileio.SaveData(Game::books, Game::users, "Data/data.store");
							Game::updateMainSearch();

							char temp2[200];
							temp2[0] = '\0';
							strcat_s(temp2, Game::books[Game::indexWindowElement].getTitle());
							strcat_s(temp2, " successfully checked out to ");
							strcat_s(temp2, Game::users[Game::indexSelected].getName());
							strcat_s(temp2, ".");
							Game::setInfo(temp2, 1);
							return;
						}
						else
						{
							char temp2[200];
							temp2[0] = '\0';
							strcat_s(temp2, "This book is already checked out.");
							Game::setInfo(temp2, -1);
						}
					}
					if (Game::menuCurrent == 1)
					{
						int maxOut;
						if (Game::users[Game::indexWindowElement].isStudent == true)
							maxOut = Game::fileio.maxOutStudent;
						else
							maxOut = Game::fileio.maxOutFaculty;
						if (Game::users[Game::indexWindowElement].numOut < maxOut)
						{

							Game::users[Game::indexWindowElement].out[Game::users[Game::indexWindowElement].numOut] = Game::books[Game::indexSelected].getId();
							Game::users[Game::indexWindowElement].dateOut[Game::users[Game::indexWindowElement].numOut] = Game::fileio.getCurrentTime();
							Game::users[Game::indexWindowElement].numOut++;
							Game::selection = false;

							Game::infoWindowActive = 0;
							Game::menuCurrent = 0;
							Game::menuLast = 0;
							Game::menu[0].TextBoxs[0].setSelected(true);
							Game::menu[1].TextBoxs[0].setSelected(false);
							Game::mainSearch.title.changeText("Book Search");
							Game::transitionPeriod = fps;
							Game::fileio.SaveData(Game::books, Game::users, "Data/data.store");
							Game::updateMainSearch();

							char temp2[200];
							temp2[0] = '\0';
							strcat_s(temp2, Game::books[Game::indexSelected].getTitle());
							strcat_s(temp2, " successfully checked out to ");
							strcat_s(temp2, Game::users[Game::indexWindowElement].getName());
							strcat_s(temp2, ".");
							Game::setInfo(temp2, 1);
						}
						else
						{
							char temp2[200];
							temp2[0] = '\0';
							strcat_s(temp2, "This user already has the max number of books checked out.");
							Game::setInfo(temp2, -1);
						}
					}
				}
			}
		}
	}
	if (Game::infoWindowActive == 1)
	{
		Game::updateInfoWindowBook();
	}
	if (Game::infoWindowActive == 2)
	{
		Game::updateInfoWindowUser();
	}
}

/**
	If a Book has been selected, update the 
	Game Elements that will display information
	about the Book in the info window.
*/
void Game::updateInfoWindowBook()
{
	Game::mainSearch.chat[Game::chatIndexWindowElement].setPos(sf::Vector2f(Game::mainSearch.chat[Game::chatIndexWindowElement].getPos().x, 3*Game::border +Game::titleSpace));
	Game::mainSearch.chat[Game::chatIndexWindowElement].setAlpha(255);
	Game::mainSearch.chat[Game::chatIndexWindowElement].update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
	
	Game::windowText[0].setPos(sf::Vector2f(windowSize.x / 8 + 3 * Game::border, Game::mainSearch.chat[Game::chatIndexWindowElement].getPos().y + Game::mainSearch.chat[Game::chatIndexWindowElement].getSize().y + 2 * Game::border));
	Game::windowText[0].setSize(sf::Vector2f(windowSize.x - windowSize.x / 8 - 6 * Game::border, 100));
	int indexUser = Game::indexOfUserWithBook(books[Game::indexWindowElement].getId());
	int indexBookinUser;
	int daysOut;
	int maxDaysOut;
	char temp2[200];
	char temp3[25];
	if (indexUser == -1)
	{
		Game::windowText[0].TextBoxs[0].changeText("This book is currently avialable.\0");
	}
	else
	{
		for (int i = 0; i < users[indexUser].numOut; i++)
		{
			if (users[indexUser].out[i] == books[Game::indexWindowElement].getId())
				indexBookinUser = i;
		}
		daysOut = Game::fileio.getDays(users[indexUser].dateOut[indexBookinUser]);
		if (users[indexUser].isStudent == true)
			maxDaysOut = Game::fileio.daysOutStudent;
		else
			maxDaysOut = Game::fileio.daysOutFaculty;
		if (maxDaysOut - daysOut > 0)
		{
			temp2[0] = '\0';
			strcat_s(temp2, "This book is due in \0");
			_itoa_s(maxDaysOut - daysOut, temp3, 10);
			strcat_s(temp2, temp3);
			strcat_s(temp2, " days.\0");
		}
		else if (maxDaysOut - daysOut == 0)
		{
			temp2[0] = '\0';
			strcat_s(temp2, "This book is due today.\0");
		}
		else
		{
			temp2[0] = '\0';
			strcat_s(temp2, "This book is OVERDUE by \0");
			_itoa_s(daysOut - maxDaysOut, temp3, 10);
			strcat_s(temp2, temp3);
			strcat_s(temp2, " days.\0");
		}
		Game::windowText[0].TextBoxs[0].changeText(temp2);
	}
	Game::windowText[0].update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);

	Game::button1.setPos(sf::Vector2f(windowSize.x / 8 + 3 * Game::border, Game::windowText[0].getPos().y + Game::windowText[0].getSize().y + 2*Game::border));
	Game::button1.setSize(sf::Vector2f((windowSize.x - windowSize.x / 8 - 6 * Game::border)/2 - Game::border, (8 * Game::fontSize) / 3));

	Game::button2.setPos(sf::Vector2f(windowSize.x / 8 + 3 * Game::border + (windowSize.x - windowSize.x / 8 - 6 * Game::border) / 2 + Game::border, Game::windowText[0].getPos().y + Game::windowText[0].getSize().y + 2 * Game::border));
	Game::button2.setSize(sf::Vector2f((windowSize.x - windowSize.x / 8 - 6 * Game::border) / 2 - Game::border, (8 * Game::fontSize) / 3));
	
	if (indexUser == -1)
		Game::button1.TextBoxs[0].changeText("Check out.\0");
	else
		Game::button1.TextBoxs[0].changeText("Check in.\0");
	Game::button2.TextBoxs[0].changeText("Delete Book.\0");

	Game::button1.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
	Game::button2.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);

	if (Game::transitionPeriod != 0)
	{
		button1.TextBoxs[0].setActive(false);
		button2.TextBoxs[0].setActive(false);
		button1.TextBoxs[0].setSelected(false);
		button2.TextBoxs[0].setSelected(false);
		button1.setSelectecd(false);
		button2.setSelectecd(false);
	}
	if (button2.getSelected() == true && Game::transitionPeriod == 0)
	{
		indexUser = Game::indexOfUserWithBook(books[Game::indexWindowElement].getId());
		if (indexUser != -1)
		{
			char temp2[200];
			temp2[0] = '\0';
			strcat_s(temp2, "Cannot delete ");
			strcat_s(temp2, books[Game::indexWindowElement].getTitle());
			strcat_s(temp2, ". It is currently checked out.");
			Game::setInfo(temp2, -1);
			return;
		}
		Game::books[Game::indexWindowElement].deleted = true;
		Game::fileio.SaveData(Game::books, Game::users, "Data/data.store");
		Game::fileio.ReadData(Game::books, Game::users, "Data/data.store");
		Game::infoWindowActive = 0;
		Game::updateMainSearch();
		Game::transitionPeriod = fps;
		Game::fileio.SaveData(Game::books, Game::users, "Data/data.store");
		
		char temp2[200];
		temp2[0] = '\0';
		strcat_s(temp2, "Successfully deleted ");
		strcat_s(temp2, books[Game::indexWindowElement].getTitle());
		strcat_s(temp2, ".");
		Game::setInfo(temp2, 1);

	}
	indexUser = Game::indexOfUserWithBook(books[Game::indexWindowElement].getId());
	if (button1.getSelected() == true && Game::transitionPeriod == 0)
	{
		if (indexUser != -1)
		{
			int indexOfIn;
			int overdueFines;
			int maxDaysOut;
			int daysOut;
			if (Game::users[indexUser].isStudent == true)
				maxDaysOut = Game::fileio.daysOutStudent;
			else
				maxDaysOut = Game::fileio.daysOutFaculty;
			for (int i = 0; i < Game::users[indexUser].numOut; i++)
			{
				if (Game::users[indexUser].out[i] == Game::books[Game::indexWindowElement].getId())
					indexOfIn = i;
			}
			daysOut = Game::fileio.getDays(Game::users[indexUser].dateOut[indexOfIn]);
			overdueFines = (daysOut - maxDaysOut)*Game::fileio.fine;
			if (overdueFines < 0)
				overdueFines = 0;
			users[indexUser].fines += overdueFines;
			users[indexUser].checkIn(indexOfIn);
			Game::updateMainSearch();
			button1.TextBoxs[0].setActive(false);
			button1.TextBoxs[0].setSelected(false);
			button1.setSelectecd(false);
			Game::transitionPeriod = Game::fps;
			Game::fileio.SaveData(Game::books, Game::users, "Data/data.store");

			char temp2[200];
			temp2[0] = '\0';
			strcat_s(temp2, "Successfully checked in ");
			strcat_s(temp2, books[Game::indexWindowElement].getTitle());
			strcat_s(temp2, ".");
			Game::setInfo(temp2, 1);
		}
		else
		{
			Game::infoWindowActive = 0;
			Game::menuCurrent = 1;
			Game::menuLast = 1;
			Game::selection = true;
			Game::updateMainSearch();
			Game::menu[0].TextBoxs[0].setSelected(false);
			Game::menu[1].TextBoxs[0].setSelected(true);
			Game::mainSearch.title.changeText("Select a user to checkout to.");
			Game::transitionPeriod = fps;
			Game::indexSelected = Game::indexWindowElement;
		}
	}
}

/**
	If a User has been selected, update the 
	Game Elements that will display information
	about the User in the info window.
*/
void Game::updateInfoWindowUser()
{
	
	Game::mainSearch.chat[Game::chatIndexWindowElement].setPos(sf::Vector2f(Game::mainSearch.chat[Game::chatIndexWindowElement].getPos().x, 2*Game::border+ Game::titleSpace));
	Game::mainSearch.chat[Game::chatIndexWindowElement].setAlpha(255);
	Game::mainSearch.chat[Game::chatIndexWindowElement].update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);

	Game::button1.setPos(sf::Vector2f(windowSize.x / 8 + 3 * Game::border, Game::mainSearch.chat[Game::chatIndexWindowElement].getPos().y + Game::mainSearch.chat[Game::chatIndexWindowElement].getSize().y + 3 * Game::border));
	Game::button1.setSize(sf::Vector2f((windowSize.x - windowSize.x / 8 - 6 * Game::border) / 2 - Game::border, (8*Game::fontSize)/3));

	Game::button2.setPos(sf::Vector2f(windowSize.x / 8 + 3 * Game::border + (windowSize.x - windowSize.x / 8 - 6 * Game::border) / 2 + Game::border, Game::mainSearch.chat[Game::chatIndexWindowElement].getPos().y + Game::mainSearch.chat[Game::chatIndexWindowElement].getSize().y + 3 * Game::border));
	Game::button2.setSize(sf::Vector2f((windowSize.x - windowSize.x / 8 - 6 * Game::border) / 2 - Game::border, (8 * Game::fontSize) / 3));

	Game::button1.TextBoxs[0].changeText("Check out book.\0");
	Game::button2.TextBoxs[0].changeText("Delete User.\0");

	Game::button1.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
	Game::button2.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
	
	if (Game::transitionPeriod != 0)
	{
		button1.TextBoxs[0].setActive(false);
		button2.TextBoxs[0].setActive(false);
		button1.TextBoxs[0].setSelected(false);
		button2.TextBoxs[0].setSelected(false);
		button1.setSelectecd(false);
		button2.setSelectecd(false);
	}
	if (button2.getSelected() == true && Game::transitionPeriod == 0)
	{
		if (Game::users[Game::indexWindowElement].numOut == 0)
		{
			Game::users[Game::indexWindowElement].deleted = true;
			Game::fileio.SaveData(Game::books, Game::users, "Data/data.store");
			Game::fileio.ReadData(Game::books, Game::users, "Data/data.store");
			Game::infoWindowActive = 0;
			Game::updateMainSearch();
			Game::transitionPeriod = fps;
			Game::fileio.SaveData(Game::books, Game::users, "Data/data.store");
			char temp2[200];
			temp2[0] = '\0';
			strcat_s(temp2, "Successfully deleted ");
			strcat_s(temp2, Game::users[Game::indexWindowElement].getName());
			strcat_s(temp2, ".");
			Game::setInfo(temp2, 1);
		}
		else
		{
			char temp2[200];
			temp2[0] = '\0';
			strcat_s(temp2, "Cannot delete ");
			strcat_s(temp2, Game::users[Game::indexWindowElement].getName());
			strcat_s(temp2, ". It currently has books checked out.");
			Game::setInfo(temp2, -1);
		}
	}
	if (button1.getSelected() == true && Game::transitionPeriod == 0)
	{
		int maxOut;
		if (Game::users[Game::indexWindowElement].isStudent == true)
			maxOut = Game::fileio.maxOutStudent;
		else
			maxOut = Game::fileio.maxOutFaculty;
		if (Game::users[Game::indexWindowElement].numOut < maxOut)
		{
			Game::infoWindowActive = 0;
			Game::menuCurrent = 0;
			Game::menuLast = 0;
			Game::selection = true;
			Game::updateMainSearch();
			Game::menu[0].TextBoxs[0].setSelected(true);
			Game::menu[1].TextBoxs[0].setSelected(false);
			Game::mainSearch.title.changeText("Select a book to checkout.");
			Game::transitionPeriod = fps;
			Game::indexSelected = Game::indexWindowElement;
		}
		else
		{
			char temp2[200];
			temp2[0] = '\0';
			strcat_s(temp2, "This user already has the max number of books checked out.");
			Game::setInfo(temp2, -1);
		}
	}


	int totalY = Game::border + windowSize.y - 2 * Game::border;

	Game::bookDisplay.setPos(sf::Vector2f(windowSize.x / 8 + 3 * Game::border, Game::button1.getPos().y + Game::button1.getSize().y + 3 * Game::border));
	Game::bookDisplay.setSize(sf::Vector2f(windowSize.x - windowSize.x / 8 - 6 * Game::border, totalY - (Game::button1.getPos().y + Game::button1.getSize().y + 3 * Game::border + 2*Game::border)));
	
	Game::bookDisplay.current = 0;
	Game::bookDisplay.numel = 0;
	char temp2[200];
	char temp3[15];
	int maxDaysOut;
	int daysOut;
	if (Game::users[Game::indexWindowElement].isStudent == true)
		maxDaysOut = Game::fileio.daysOutStudent;
	else
		maxDaysOut = Game::fileio.daysOutFaculty;
	for (int i = 0; i < Game::users[Game::indexWindowElement].numOut; i++)
	{
		
		temp2[0] = '\0';
		temp3[0] = '\0';
		strcat_s(temp2, "Title: \0");
		strcat_s(temp2, getBookWithId(Game::users[Game::indexWindowElement].out[i]).getTitle());
		strcat_s(temp2, "\nAuthor: \0");
		strcat_s(temp2, getBookWithId(Game::users[Game::indexWindowElement].out[i]).getAuthor());
		Game::bookDisplay.chat[Game::bookDisplay.current].TextBoxs[0].changeText(temp2);
		
		temp2[0] = '\0';
		strcat_s(temp2, "Id:\0");
		_itoa_s(getBookWithId(Game::users[Game::indexWindowElement].out[i]).getId(), temp3, 10);
		strcat_s(temp2, temp3);
		Game::bookDisplay.chat[Game::bookDisplay.current].TextBoxs[1].changeText(temp2);

		temp3[0] = '\0';
		daysOut = Game::fileio.getDays(Game::users[Game::indexWindowElement].dateOut[i]);
		if (maxDaysOut - daysOut > 0)
		{
			temp2[0] = '\0';
			strcat_s(temp2, "This book is due in \0");
			_itoa_s(maxDaysOut - daysOut, temp3, 10);
			strcat_s(temp2, temp3);
			strcat_s(temp2, " days.\0");
		}
		else if (maxDaysOut - daysOut == 0)
		{
			temp2[0] = '\0';
			strcat_s(temp2, "This book is due today.\0");
		}
		else
		{
			temp2[0] = '\0';
			strcat_s(temp2, "This book is OVERDUE by \0");
			_itoa_s(daysOut - maxDaysOut, temp3, 10);
			strcat_s(temp2, temp3);
			strcat_s(temp2, " days.\0");
		}
		Game::bookDisplay.chat[Game::bookDisplay.current].TextBoxs[2].changeText(temp2);

		Game::bookDisplay.current++;
		Game::bookDisplay.numel++;
		if (Game::bookDisplay.numel > Game::bookDisplay.maxel)
			Game::bookDisplay.numel = Game::bookDisplay.maxel;
		Game::bookDisplay.current %= Game::bookDisplay.maxel;
	}
	Game::mainSearch.setJumpToTop();
	
	
	
	Game::bookDisplay.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps, Game::keyPressed[int(sf::Keyboard::Up)], Game::keyPressed[int(sf::Keyboard::Down)], 0, Game::mouseDist);
}

/**
	If the user has opened the 'Add Book' or 
	'Add User' menu, update the Game Elements
	that will allow the user to add the Book or 
	User.
*/
void Game::updateForm()
{
	Game::input1.setAllignY(1 * Game::windowSize.y / 7 + Game::titleSpace + 20);
	Game::input2.setAllignY(2 * Game::windowSize.y / 7 + Game::titleSpace + 20);
	Game::button1.setPos(sf::Vector2f(windowSize.x / 8 + 4 * Game::border, 2.5 * Game::windowSize.y / 7 + Game::titleSpace + 20));
	Game::button1.setSize(sf::Vector2f(300, 75));
	Game::button1.TextBoxs[0].changeText("Add\0");
	Game::button1.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
	if (button1.getSelected())
	{
		if (Game::lastBState == false)
		{
			//cout << "Button1 Selected\n";
			Game::input1.forcedEnter();
			Game::input2.forcedEnter();

			Game::input1.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
			Game::input2.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);

			//cout << Game::input1.getSaved() << " | " << Game::input2.getSaved() << '\n';
			if (strlen(Game::input1.getSaved()) > 0 && strlen(Game::input2.getSaved()) > 0 && strlen(Game::input1.getSaved()) < 48 && strlen(Game::input2.getSaved()) < 23)
			{
				if (Game::menuCurrent == 2)
				{
					Game::fileio.addBook(Game::books, Game::users, Game::input1.getSaved(), Game::input2.getSaved());
					char temp2[200];
					temp2[0] = '\0';
					strcat_s(temp2, "Successfully added ");
					strcat_s(temp2, Game::input1.getSaved());
					strcat_s(temp2, " by ");
					strcat_s(temp2, Game::input2.getSaved());
					strcat_s(temp2, ".");
					Game::setInfo(temp2, 1);
					
				}
				if (Game::menuCurrent == 3)
				{
					if((strcmp(Game::input2.getSaved(), "Student") == 0))
						Game::fileio.addUser(Game::books, Game::users, Game::input1.getSaved(), true);
					else
						Game::fileio.addUser(Game::books, Game::users, Game::input1.getSaved(), false);

					char temp2[200];
					temp2[0] = '\0';
					strcat_s(temp2, "Successfully added ");
					strcat_s(temp2, Game::input1.getSaved());
					if ((strcmp(Game::input2.getSaved(), "Student") == 0))
						strcat_s(temp2, ", student.");
					else
						strcat_s(temp2, ", faculty.");
					Game::setInfo(temp2, 1);
				}
				//cout << "Book/User Added!\n";
			}
			else if (!(strlen(Game::input1.getSaved()) > 0 && strlen(Game::input2.getSaved()) > 0))
			{
				char temp2[200];
				temp2[0] = '\0';
				strcat_s(temp2, "Some fields are incomplete.");
				Game::setInfo(temp2, -1);
			}
			else
			{
				char temp2[200];
				temp2[0] = '\0';
				strcat_s(temp2, "Some fields contain too many characters.");
				Game::setInfo(temp2, -1);
			}
			Game::input1.resetSaved();
			Game::input2.resetSaved();
			if (Game::menuCurrent == 2)
			{
				Game::input1.changeEmpty("Enter Title");
				Game::input2.changeEmpty("Enter Author");
			}
			if (Game::menuCurrent == 3)
			{
				Game::input1.changeEmpty("Enter Name");
				Game::input2.changeEmpty("User Type (Student/Faculty)");
			}
			Game::lastBState = true;

		}
		Game::button1.TextBoxs[0].setSelected(false);
	}
	else
		Game::lastBState = false;

	if (strlen(Game::input1.getSaved()) > 0)
	{
		Game::input1.changeEmpty(Game::input1.getSaved());
	}
	if (strlen(Game::input2.getSaved()) > 0)
	{
		Game::input2.changeEmpty(Game::input2.getSaved());
	}

	Game::input1.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
	Game::input2.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
}

/**
	If the user has opened the 'Settings' 
	menu, update the Game Elements that 
	will allow the user to change the settings.
*/
void Game::updateSettings()
{
	Game::input1.setAllignY(1 * Game::windowSize.y / 7 + Game::titleSpace + 20);
	Game::input2.setAllignY(2 * Game::windowSize.y / 7 + Game::titleSpace + 20);
	Game::input1.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
	Game::input2.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
	Game::input3.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
	Game::input4.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
	Game::input5.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);

	int temp1;
	char temp2[200];
	char temp3[50];
	char temp4[200];
	if (strlen(Game::input1.getSaved()) > 0)
	{
		temp1 = -1;
		temp2[0] = '\0';
		temp3[0] = '\0';
		strcat_s(temp2, "Daily fine of overdue books (in cents): \0");
		sscanf_s(Game::input1.getSaved(), "%d", &temp1);
		if (temp1 >= 0 && temp1 <= 100)
		{
			Game::fileio.fine = temp1;
			temp4[0] = '\0';
			strcat_s(temp4, "Successfully changed setting.");
			Game::setInfo(temp4, 1);
		}
		else
		{
			temp4[0] = '\0';
			strcat_s(temp4, "Invalid Input");
			Game::setInfo(temp4, -1);
		}
		_itoa_s(Game::fileio.fine, temp3, 10);
		strcat_s(temp2, temp3);
		strcat_s(temp2, "\n Enter a number from (0-100) to change.");
		Game::input1.changeEmpty(temp2);
		Game::input1.resetSaved();
		Game::fileio.SaveData(books, users, "Data/data.store");

	}
	if (strlen(Game::input2.getSaved()) > 0)
	{
		temp1 = -1;
		temp2[0] = '\0';
		temp3[0] = '\0';
		strcat_s(temp2, "Maxium number days Students can checkout books: \0");
		sscanf_s(Game::input2.getSaved(), "%d", &temp1);
		if (temp1 >= 7 && temp1 <= 28)
		{
			Game::fileio.daysOutStudent = temp1;
			temp4[0] = '\0';
			strcat_s(temp4, "Successfully changed setting.");
			Game::setInfo(temp4, 1);
		}
		else
		{
			temp4[0] = '\0';
			strcat_s(temp4, "Invalid Input");
			Game::setInfo(temp4, -1);
		}
		_itoa_s(Game::fileio.daysOutStudent, temp3, 10);
		strcat_s(temp2, temp3);
		strcat_s(temp2, "\n Enter a number from (7-28) to change.");
		Game::input2.changeEmpty(temp2);
		Game::input2.resetSaved();
		Game::fileio.SaveData(books, users, "Data/data.store");
	}
	if (strlen(Game::input3.getSaved()) > 0)
	{
		temp1 = -1;
		temp2[0] = '\0';
		temp3[0] = '\0';
		strcat_s(temp2, "Maxium number days Faculty can checkout books: \0");
		sscanf_s(Game::input3.getSaved(), "%d", &temp1);
		if (temp1 >= 7 && temp1 <= 28)
		{
			Game::fileio.daysOutFaculty = temp1;
			temp4[0] = '\0';
			strcat_s(temp4, "Successfully changed setting.");
			Game::setInfo(temp4, 1);
		}
		else
		{
			temp4[0] = '\0';
			strcat_s(temp4, "Invalid Input");
			Game::setInfo(temp4, -1);
		}
		_itoa_s(Game::fileio.daysOutFaculty, temp3, 10);
		strcat_s(temp2, temp3);
		strcat_s(temp2, "\n Enter a number from (7-28) to change.");
		Game::input3.changeEmpty(temp2);
		Game::input3.resetSaved();
		Game::fileio.SaveData(books, users, "Data/data.store");
	}
	if (strlen(Game::input4.getSaved()) > 0)
	{
		temp1 = -1;
		temp2[0] = '\0';
		temp3[0] = '\0';
		strcat_s(temp2, "Maxium numberof books Students can checkout at once: \0");
		sscanf_s(Game::input4.getSaved(), "%d", &temp1);
		if (temp1 >= 1 && temp1 <= 15)
		{
			Game::fileio.maxOutStudent = temp1;
			temp4[0] = '\0';
			strcat_s(temp4, "Successfully changed setting.");
			Game::setInfo(temp4, 1);
		}
		else
		{
			temp4[0] = '\0';
			strcat_s(temp4, "Invalid Input");
			Game::setInfo(temp4, -1);
		}
		_itoa_s(Game::fileio.maxOutStudent, temp3, 10);
		strcat_s(temp2, temp3);
		strcat_s(temp2, "\n Enter a number from (1-15) to change.");
		Game::input4.changeEmpty(temp2);
		Game::input4.resetSaved();
		Game::fileio.SaveData(books, users, "Data/data.store");
	}
	if (strlen(Game::input5.getSaved()) > 0)
	{
		temp1 = -1;
		temp2[0] = '\0';
		temp3[0] = '\0';
		strcat_s(temp2, "Maxium numberof books Faculty can checkout at once: \0");
		sscanf_s(Game::input5.getSaved(), "%d", &temp1);
		if (temp1 >= 1 && temp1 <= 15)
		{
			Game::fileio.maxOutFaculty = temp1;
			temp4[0] = '\0';
			strcat_s(temp4, "Successfully changed setting.");
			Game::setInfo(temp4, 1);
		}
		else
		{
			temp4[0] = '\0';
			strcat_s(temp4, "Invalid Input");
			Game::setInfo(temp4, -1);
		}
		_itoa_s(Game::fileio.maxOutFaculty, temp3, 10);
		strcat_s(temp2, temp3);
		strcat_s(temp2, "\n Enter a number from (1-15) to change.");
		Game::input5.changeEmpty(temp2);
		Game::input5.resetSaved();
		Game::fileio.SaveData(books, users, "Data/data.store");
	}
}

/**
	Update game events such as key presses.
*/
void Game::updateEvents()
{
	sf::Event event;
	while (Game::window.pollEvent(event))
	{
		if (event.type == sf::Event::Closed)
			window.close();
		for (int i = 0; i < 100; i++)
			if (sf::Keyboard::isKeyPressed(sf::Keyboard::Key(i)))
				Game::keyPressed[i] = true;
			else
				Game::keyPressed[i] = false;
	}
}

/**
	Update the window size and mouse position.
*/
void Game::updateWindow()
{
	//Update Window Size
	Game::windowSize = Game::window.getSize();
	//Update Mouse
	Game::updateMouse();

}

/**
	Compare Books.
	
	Used for sorting the most relavent searches.

	@param a - void pointer to first element.
	@param b - void pointer to second element.
	@return a number that tells the sorting algorithm
	which Book is 'higher.'
*/
int compareBooks(const void * a, const void * b)
{
	return -1*((*(Book*)a).searchWeight - (*(Book*)b).searchWeight);
}

/**
	Compare USers.
	
	Used for sorting the most relavent searches.

	@param a - void pointer to first element.
	@param b - void pointer to second element.
	@return a number that tells the sorting algorithm
	which User is 'higher.'
*/
int compareUsers(const void * a, const void * b)
{
	return -1 * ((*(User*)a).searchWeight - (*(User*)b).searchWeight);
}

/**
	Find the largest substring of pattern in text.
	
	Algorithm used to compare search query to list
	elements.

	@param text - text to be searched for its relevance.
	@param pattern - search query.
	@return a number that tells the search algorithm
	the largest substring of pattern that appears in 
	text. This is the relevance of text to the search
	querty, pattern.
*/
int getLargestPatternFind(char text[200], char pattern[200])
{
	int currentCount = 0;
	int maxCount = 0;
	char a;
	char b;
	for (int i = 0; i < strlen(text); i++)
	{
		currentCount = 0;
		for (int j = 0; j < strlen(pattern); j++)
		{
			if (i + j >= strlen(text))
				break;
			a = text[i + j];
			if (a >= 'A'&&a <= 'Z')
				a += 'a' - 'A';
			b = pattern[j];
			if (b >= 'A'&&b <= 'Z')
				b += 'a' - 'A';
			if (a != b)
				break;
			currentCount++;
		}
		if (currentCount > maxCount)
			maxCount = currentCount;
	}
	return maxCount;
}

/**
	Find the index of the User in users with 
	the book of a certain id.
	
	@param id - id of book to be found.
	@return index of user with book id. -1 if
	no such user exists.
*/
int Game::indexOfUserWithBook(int id)
{
	for (int i = 0; i < Game::fileio.getNumUsers(); i++)
	{
		for (int j = 0; j < Game::users[i].numOut; j++)
		{
			if (Game::users[i].out[j] == id)
				return i;
		}
	}
	return -1;
}

/**
	Updates the Game Elements that make up
	the menu.

	Detects if there is a change in menu state.
*/
void Game::updateMenu()
{
	if (Game::menu[0].TextBoxs[0].getActive() == true || Game::menu[1].TextBoxs[0].getActive() == true)
	{
		if (mouseDownL == true)
		{
			Game::infoWindowActive = 0;
		}
	}
	Game::menuCurrent = -1;
	for (int i = 0; i < Game::menuSize; i++)
	{
		Game::menu[i].update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
		if (Game::menu[i].TextBoxs[0].getSelected())
		{
			Game::menuCurrent = i;
		}
		if (Game::menu[i].getActive() && Game::mouseDownL == true)
		{
			Game::selection = false;
			Game::infoWindowActive = 0;
			if (Game::menuCurrent == 0)
			{
				Game::mainSearch.title.changeText("Book Search");
				Game::updateMainSearch();
			}
			if (Game::menuCurrent == 1)
			{
				Game::mainSearch.title.changeText("User Search");
				Game::updateMainSearch();
			}
		}
	}
	if (Game::menuCurrent == -1)
	{
		Game::menuCurrent = Game::menuLast;
		Game::menu[Game::menuCurrent].TextBoxs[0].setSelected(true);
	}

	if (Game::menuCurrent != Game::menuLast)
	{
		if (Game::menuCurrent == 0)
		{
			Game::mainSearch.title.changeText("Book Search");
			Game::updateMainSearch();
		}
		if (Game::menuCurrent == 1)
		{
			Game::mainSearch.title.changeText("User Search");
			Game::updateMainSearch();
		}
		if (Game::menuCurrent == 2)
		{
			Game::input1.changeEmpty("Enter Title");
			Game::input2.changeEmpty("Enter Author");
			Game::input1.resetSaved();
			Game::input2.resetSaved();
			Game::input1.cleared();
			Game::input2.cleared();
		}
		if (Game::menuCurrent == 3)
		{
			Game::input1.changeEmpty("Enter Name");
			Game::input2.changeEmpty("User Type (Student/Faculty)");
			Game::input1.resetSaved();
			Game::input2.resetSaved();
			Game::input1.cleared();
			Game::input2.cleared();
		}
		if (Game::menuCurrent == 4)
		{
			int temp1;
			char temp2[200];
			char temp3[50];
			
			temp1 = -1;
			temp2[0] = '\0';
			temp3[0] = '\0';
			strcat_s(temp2, "Daily fine of overdue books (in cents): \0");
			_itoa_s(Game::fileio.fine, temp3, 10);
			strcat_s(temp2, temp3);
			strcat_s(temp2, "\n Enter a number from (0-100) to change.");
			Game::input1.changeEmpty(temp2);
			Game::input1.resetSaved();
				
			temp1 = -1;
			temp2[0] = '\0';
			temp3[0] = '\0';
			strcat_s(temp2, "Maxium number days Students can checkout books: \0");
			_itoa_s(Game::fileio.daysOutStudent, temp3, 10);
			strcat_s(temp2, temp3);
			strcat_s(temp2, "\n Enter a number from (7-28) to change.");
			Game::input2.changeEmpty(temp2);
			Game::input2.resetSaved();
			
				temp1 = -1;
				temp2[0] = '\0';
				temp3[0] = '\0';
				strcat_s(temp2, "Maxium number days Faculty can checkout books: \0");
				_itoa_s(Game::fileio.daysOutFaculty, temp3, 10);
				strcat_s(temp2, temp3);
				strcat_s(temp2, "\n Enter a number from (7-28) to change.");
				Game::input3.changeEmpty(temp2);
				Game::input3.resetSaved();
			
				temp1 = -1;
				temp2[0] = '\0';
				temp3[0] = '\0';
				strcat_s(temp2, "Maxium numberof books Students can checkout at once: \0");
				_itoa_s(Game::fileio.maxOutStudent, temp3, 10);
				strcat_s(temp2, temp3);
				strcat_s(temp2, "\n Enter a number from (1-15) to change.");
				Game::input4.changeEmpty(temp2);
				Game::input4.resetSaved();
			
				temp1 = -1;
				temp2[0] = '\0';
				temp3[0] = '\0';
				strcat_s(temp2, "Maxium numberof books Faculty can checkout at once: \0");
				_itoa_s(Game::fileio.maxOutFaculty, temp3, 10);
				strcat_s(temp2, temp3);
				strcat_s(temp2, "\n Enter a number from (1-15) to change.");
				Game::input5.changeEmpty(temp2);
				Game::input5.resetSaved();
		}
		if (Game::menuCurrent == 5)
		{
			Game::fileio.printReport(Game::books, Game::users, "report.txt");
			Game::setInfo("Report successfully generated.", 1);
			Game::menuCurrent = 0;
			Game::menuLast = 0;
			Game::menu[0].setSelectecd(true);
			Game::menu[0].TextBoxs[0].setActive(true);
			Game::menu[0].TextBoxs[0].setSelected(true);

			Game::menu[5].setSelectecd(false);
			Game::menu[5].TextBoxs[0].setActive(false);
			Game::menu[5].TextBoxs[0].setSelected(false);
		}
		if(Game::menuCurrent == 6)
			window.close();
		//cout << Game::menuLast << " -- > " << Game::menuCurrent << '\n';
	}
	Game::menuLast = Game::menuCurrent;
}
/**
	Updates the ChatList mainSearch.
	
	Uses the search query to fill the ChatList
	with the most relavent results.
*/
void Game::updateMainSearch()
{
	
	if (Game::menuCurrent == 0)
	{
		Book sorted[500];

		int numBooks = Game::fileio.getNumBooks();
		int temp;
		for (int i = 0; i < numBooks; i++)
		{
			sorted[i] = Book(Game::books[i].getTitle(), Game::books[i].getAuthor(), Game::books[i].getId());
			//cout << sorted[i].getTitle() << '\n';
			sorted[i].searchWeight = getLargestPatternFind(sorted[i].getTitle(), Game::bQuery);
			temp = getLargestPatternFind(sorted[i].getAuthor(), Game::bQuery);
			if (temp > sorted[i].searchWeight)
				sorted[i].searchWeight = temp;
			//cout << sorted[i].searchWeight << '\n';
		}
		qsort(sorted, numBooks, sizeof(Book), compareBooks);
		if (numBooks > 50)
			numBooks = 50;
		Game::mainSearch.current = 0;
		Game::mainSearch.numel = 0;
		char temp2[200];
		char temp3[15];
		int indexUser;
		for (int i = 0; i < numBooks; i++)
		{
			if (sorted[i].searchWeight <= 1)
				break;
			Game::mainSearch.chat[Game::mainSearch.current].ImageBox1.setTexture(&(Game::textures[0]));
			temp2[0] = '\0';
			strcat_s(temp2, "Title: \0");
			strcat_s(temp2, sorted[i].getTitle());
			strcat_s(temp2, " \nAuthor: \0");
			strcat_s(temp2, sorted[i].getAuthor());
			Game::mainSearch.chat[Game::mainSearch.current].TextBoxs[0].changeText(temp2);
			temp2[0] = '\0';
			strcat_s(temp2, "Id: \0");
			_itoa_s(sorted[i].getId(), temp3, 10);
			strcat_s(temp2, temp3);
			Game::mainSearch.chat[Game::mainSearch.current].TextBoxs[1].changeText(temp2);
			indexUser = Game::indexOfUserWithBook(sorted[i].getId());
			if(indexUser == -1)
				Game::mainSearch.chat[Game::mainSearch.current].TextBoxs[2].changeText("Checked In\0");
			else
			{
				temp2[0] = '\0';
				strcat_s(temp2, "Checked out by: \0");
				strcat_s(temp2, Game::users[indexUser].getName());
				Game::mainSearch.chat[Game::mainSearch.current].TextBoxs[2].changeText(temp2);
			}
			Game::mainSearch.current++;
			Game::mainSearch.numel++;
			if (Game::mainSearch.numel > Game::mainSearch.maxel)
				Game::mainSearch.numel = Game::mainSearch.maxel;
			Game::mainSearch.current %= Game::mainSearch.maxel;
		}
		Game::mainSearch.setJumpToTop();
		for (int i = 0; i < numBooks; i++)
		{
			//cout << sorted[i].getTitle() << '\n';
			//cout << sorted[i].searchWeight << '\n';
		}
	}
	if (Game::menuCurrent == 1)
	{
		User sorted[300];

		int numUsers = Game::fileio.getNumUsers();
		int temp;
		for (int i = 0; i < numUsers; i++)
		{
			sorted[i] = User(Game::users[i].getName(), Game::users[i].getId());
			sorted[i].numOut = Game::users[i].numOut;
			sorted[i].isStudent = Game::users[i].isStudent;
			sorted[i].fines = Game::users[i].fines;
			for (int j = 0; j < sorted[i].numOut; j++)
			{
				sorted[i].dateOut[j] = Game::users[i].dateOut[j];
				sorted[i].out[j] = Game::users[i].out[j];
			}
			//cout << sorted[i].getTitle() << '\n';
			sorted[i].searchWeight = getLargestPatternFind(sorted[i].getName(), Game::uQuery);
			//cout << sorted[i].searchWeight << '\n';
		}
		qsort(sorted, numUsers, sizeof(User), compareBooks);
		if (numUsers > 50)
			numUsers = 50;
		Game::mainSearch.current = 0;
		Game::mainSearch.numel = 0;
		char temp2[200];
		char temp3[15];
		for (int i = 0; i < numUsers; i++)
		{
			//if (sorted[i].searchWeight <= 1)
				//continue;
			Game::mainSearch.chat[Game::mainSearch.current].ImageBox1.setTexture(&(Game::textures[1]));
			temp2[0] = '\0';
			strcat_s(temp2, "Name: \0");
			strcat_s(temp2, sorted[i].getName());
			if(sorted[i].isStudent == true)
				strcat_s(temp2, "\nUser Type: Student\0");
			else
				strcat_s(temp2, "\nUser Type: Faculty\0");
			
			Game::mainSearch.chat[Game::mainSearch.current].TextBoxs[0].changeText(temp2);
			temp2[0] = '\0';
			temp3[0] = '\0';
			strcat_s(temp2, "Id: \0");
			_itoa_s(sorted[i].getId(), temp3, 10);
			strcat_s(temp2, temp3);
			Game::mainSearch.chat[Game::mainSearch.current].TextBoxs[1].changeText(temp2);
			temp2[0] = '\0';
			temp3[0] = '\0';
			strcat_s(temp2, "Books Out: \0");
			_itoa_s(sorted[i].numOut, temp3, 10);
			strcat_s(temp2, temp3);
			temp3[0] = '\0';
			strcat_s(temp2, "\t|\tFines: $\0");

			int maxDaysOut;
			int daysOut;
			if (sorted[i].isStudent == true)
				maxDaysOut = Game::fileio.daysOutStudent;
			else
				maxDaysOut = Game::fileio.daysOutFaculty;
			int totalFines = 0;
			for (int j = 0; j < sorted[i].numOut; j++)
			{
				daysOut = Game::fileio.getDays(sorted[i].dateOut[j]);
				if (maxDaysOut-daysOut < 0)
					totalFines += Game::fileio.fine*(daysOut-maxDaysOut);
			}
			totalFines+= sorted[i].fines;
			int dollars = totalFines / 100;
			int cents = totalFines % 100;
			temp3[0] = '\0';
			_itoa_s(dollars, temp3, 10);
			strcat_s(temp2, temp3);
			strcat_s(temp2, ".\0");
			temp3[0] = '\0';
			_itoa_s(cents, temp3, 10);
			if(cents<10)
				strcat_s(temp2, "0\0");
			strcat_s(temp2, temp3);
			Game::mainSearch.chat[Game::mainSearch.current].TextBoxs[2].changeText(temp2);
			Game::mainSearch.current++;
			Game::mainSearch.numel++;
			if (Game::mainSearch.numel > Game::mainSearch.maxel)
				Game::mainSearch.numel = Game::mainSearch.maxel;
			Game::mainSearch.current %= Game::mainSearch.maxel;
		}
		Game::mainSearch.setJumpToTop();
		for (int i = 0; i < numUsers; i++)
		{
			//cout << sorted[i].getName() << '\n';
			//cout << sorted[i].searchWeight << '\n';
		}
	}
	Game::mainSearch.update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps, Game::keyPressed[int(sf::Keyboard::Up)], Game::keyPressed[int(sf::Keyboard::Down)], Game::userInput.getSize().y, Game::mouseDist);
	Game::mainSearch.setJumpToTop();
	
}

/**
	Update mouse movements and mouse button presses.
*/
void Game::updateMouse()
{
	Game::mousePos = sf::Mouse::getPosition(Game::window);
	Game::window.setMouseCursorVisible(true);

	Game::mouseDownL = false;
	if (sf::Mouse::isButtonPressed(sf::Mouse::Left))
		Game::mouseDownL = true;
	if (Game::keyPressed[int(sf::Keyboard::Escape)])
		window.close();

	Game::mouseDist.x = Game::mousePos.x - Game::lastMousePos.x;
	Game::mouseDist.y = Game::mousePos.y - Game::lastMousePos.y;
	Game::lastMousePos = sf::Vector2i(Game::mousePos.x, Game::mousePos.y);
	//cout << "Confirmed: " << Game::mouseDist.x << " | " << Game::mouseDist.y << '\n';

	//Force mouse to center
	//sf::Vector2i test(Game::windowSize.x/2, Game::windowSize.y/2);
	//sf::Mouse::setPosition(test, Game::window);
}

/**
	Updates a string which keepts track of
	the globally typed keys.
*/
void Game::updateTyped()
{
	int wait1 = int(Game::fps/2);
	int wait2 = int(wait1/10);
	char characters[204] = "abcdefghijklmnopqrstuvwxyz0123456789__________[];,.'\/\\`=- __\t_____+-*/_____0123456789_______________ABCDEFGHIJKLMNOPQRSTUVWXYZ)!@#$%^&*(__________{}:<>\"?|~+_ ________+-*/ ____0123456789________________\0";
	"abcdefghijklmnopqrstuvwxyz0123456789__________[];,.'\/\\`=- _";
	int currentChar;
	for (int i = 0; i < 100; i++)
	{
		currentChar = i;
		if (Game::keyPressed[int(sf::Keyboard::LShift)] || Game::keyPressed[int(sf::Keyboard::RShift)])
			currentChar += 100;
		if (Game::keyPressed[i] == true)
		{
			//cout << "Id: " << currentChar << " Letter: " << characters[currentChar] << '\n';
			if ((Game::keyTimer[i] == 0 || Game::keyTimer[i] == 1) && characters[currentChar] != '_' && Game::numTyped < 199)
			{
				Game::typed[Game::numTyped] = characters[currentChar];
				Game::lastCharTyped = Game::typed[Game::numTyped];
				Game::numTyped++;
				Game::typed[Game::numTyped] = '\0';
				if (keyTimer[i] == 0)
					keyTimer[i] = wait1;
				else
					keyTimer[i] = wait2;
			}
			keyTimer[i]--;

		}
		else
			Game::keyTimer[i] = 0;
	}
	if (Game::keyPressed[int(sf::Keyboard::BackSpace)])
	{
		if (Game::keyTimer[100] == 0 || Game::keyTimer[100] == 1)
		{
			Game::numTyped -= 1;
			if (Game::numTyped < 0)
				Game::numTyped = 0;
			Game::typed[Game::numTyped] = '\0';
			if (keyTimer[100] == 0)
				keyTimer[100] = wait1;
			else
				keyTimer[100] = wait2;
		}
		keyTimer[100]--;
	}
	else
		Game::keyTimer[100] = 0;

	//Game::debugText[1].setString(Game::typed);
}

/**
	Tracks the rate at which the game is updating
	and drawing.
*/
void Game::updateFPS()
{
	sf::Time currentElapsedTime = Game::mainClock.getElapsedTime();
	sf::Time totalElapsedTime = currentElapsedTime- Game::prevElapsedTime;
	if (totalElapsedTime.asMilliseconds() > 1000)
	{
		Game::fps = Game::frameCount;
		Game::frameCount = 0;
		Game::prevElapsedTime = currentElapsedTime;
		char time[16];
		char fps[7];
		int seconds = currentElapsedTime.asSeconds();
		_itoa_s(seconds, time, 10);
		_itoa_s(Game::fps, fps, 10);
		strcat_s(time, " : ");
		strcat_s(time, fps);
		cout << time << '\n';
		Game::debugText[0].setString(time);
	}
	Game::frameCount++;
}
