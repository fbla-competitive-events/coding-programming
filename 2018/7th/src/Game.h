#pragma once
#include "textField.h"
#include "ChatList.h"
#include "ImageBox.h"
#include "TextBox.h"
#include "Box.h"
#include "DataHandler.h"
#include "Book.h"
#include "User.h"

/**
	Definition of the Game class. A game 
	must be initialized, updated, and run.
*/
class Game
{
private:

	const int MAX_BOOKS = 1000;
	sf::Vector2i mousePos;
	sf::Vector2f mouseDist;
	sf::Vector2i lastMousePos;
	bool mouseDownL;
	sf::Vector2u windowSize;
	bool keyPressed[100];
	sf::Text debugText[10];
	sf::Font mainFont;
	int fontSize;
	sf::Clock mainClock;
	sf::Time prevElapsedTime;
	int frameCount;
	int fps;
	int border;
	char typed[200];
	int numTyped;
	char lastCharTyped;
	int keyTimer[101];
	char bQuery[200];
	char uQuery[200];
	int infoBoxCoolDown;
	int menuSize = 7;
	int menuCurrent;
	int menuLast;
	bool lastBState;
	int infoWindowActive;
	int indexWindowElement;
	int chatIndexWindowElement;
	int transitionPeriod;
	bool selection;
	int indexSelected;
	int titleSpace;
	int fullyStarted;
	Book books[500];
	User users[300];

	//Game Elements
	textField userInput;
	ChatList mainSearch;
	ChatList bookDisplay;
	Box Title;
	Box Info;
	textField input1;
	textField input2;
	textField input3;
	textField input4;
	textField input5;
	Box button1;
	Box button2;
	Box windowText[5];
	Box menu[7];
	DataHandler fileio;
	sf::Texture textures[2];
	//

	void updateEvents();
	void updateWindow();
	void updateTyped();
	void updateFPS();
	void updateMouse();
	void update();
	void draw(); 
	void drawDebugText();

public:

	sf::RenderWindow window;
	Game() {};
	void init();
	void run();

	//For Game Elements
	void initMainSearch();
	void initBookDisplay();
	void updateMenu();
	void updateMainSearch();
	void updateSearchClick();
	void updateInfoWindowUser();
	void updateInfoWindowBook();
	void drawInfoWindowUser();
	void drawInfoWindowBook();
	int indexOfUserWithBook(int id);
	Book getBookWithId(int id);
	void drawForm();
	void updateForm();
	void drawSettings();
	void updateSettings();
	void updateInfo();
	void drawInfo();
	void setInfo(char text[200], int type);
	void drawBorder();
	//
};