#include <SFML/Graphics.hpp>
#include "Game.h"
#include <iostream>
using namespace std;

/**
	Initializes the Game and its Game Elements.
*/
void Game::init()
{
	
	Game::window.create(sf::VideoMode(200, 200), "WHRHS Library Database Manager", sf::Style::Fullscreen);
	//Game::window.create(sf::VideoMode(1200, 600), "WHRHS Library Database Manager");
	Game::window.setFramerateLimit(100);
	Game::mousePos.x = 0;
	Game::mousePos.y = 0;
	Game::lastMousePos.x = 0;
	Game::lastMousePos.y = 0;
	Game::mouseDownL = false;
	for (int i = 0; i < 100; i++)
		Game::keyPressed[i] = 0;
	Game::mainFont.loadFromFile("Images and Fonts/Montserrat-Regular.ttf");
	for (int i = 0; i < 10; i++)
		Game::debugText[i].setString("");
	Game::typed[0] = '\0';
	Game::lastCharTyped = '\0';
	Game::numTyped = 0;
	Game::bQuery[0] = '\0';
	Game::uQuery[0] = '\0';
	Game::border = 10;
	Game::mainClock = sf::Clock();
	Game::prevElapsedTime = Game::mainClock.getElapsedTime();
	Game::frameCount = 0;
	Game::fps = 0;

	Game::infoBoxCoolDown = 0;
	Game::lastBState = false;
	Game::infoWindowActive = 0;
	Game::transitionPeriod = 0;
	Game::selection = false;

	Game::fullyStarted = 300;

	Game::updateWindow();
	Game::titleSpace = Game::windowSize.y/10;
	Game::fontSize = 30 * (sqrt(windowSize.x*windowSize.y) / 1440);

	Game::fileio = DataHandler();
	Game::fileio.ReadData(Game::books, Game::users, "Data/data.store");
	Game::fileio.SaveData(Game::books, Game::users, "Data/data.store");
	Game::fileio.SaveData(Game::books, Game::users, "Data Backup/data2.store");

	//init search elements
	Game::mainSearch = ChatList(sf::Vector2f(Game::windowSize.x/8 +  Game::border, Game::border+Game::titleSpace), sf::Vector2f(Game::windowSize.x - Game::windowSize.x / 8 - 2 * Game::border, Game::windowSize.y - 2 * Game::border- Game::titleSpace), 20, 20, "Book Search");
	Game::mainSearch.setColor(sf::Color(35, 35, 35, 255), sf::Color(200, 200, 200, 255));
	Game::mainSearch.initTitle(Game::mainFont, Game::fontSize, sf::Color(50, 50, 50, 255), sf::Color(255, 255, 255, 255));
	Game::mainSearch.setDissapear(false);
	Game::initMainSearch();

	Game::userInput = textField(sf::Vector2f(windowSize.x/8 + Game::border, 0), sf::Vector2f(windowSize.x - windowSize.x / 8 - 2 * Game::border, 0), "Search Here");
	Game::userInput.setColor(sf::Color(50, 50, 50, 255), sf::Color(255, 255, 255, 255), sf::Color(255, 255, 255, 255));
	Game::userInput.setFont(Game::mainFont);
	Game::userInput.setAllignY(Game::mainSearch.getPos().y + Game::mainSearch.getSize().y);
	Game::userInput.setFontSize(Game::fontSize);

	Game::textures[0].loadFromFile("Images and Fonts/book5.png");
	Game::textures[0].setSmooth(true);
	Game::textures[1].loadFromFile("Images and Fonts/user.png");
	Game::textures[1].setSmooth(true);

	sf::Image icon;
	icon.loadFromFile("Images and Fonts/icon2.png");
	Game::window.setIcon(icon.getSize().x, icon.getSize().y, icon.getPixelsPtr());
	//

	//Init title
	Game::Title = Box(sf::Vector2f(windowSize.x / 8 + Game::border, Game::border), sf::Vector2f(Game::windowSize.x - Game::windowSize.x / 8 - 2*Game::border, Game::titleSpace - Game::border*2), 1);
	Game::Title.TextBoxs[0] = TextBox(sf::Vector2f(0, 0), sf::Vector2f(0, 0), "WHRHS Library Database Manager");
	Game::Title.TextBoxs[0].setColor(sf::Color(50, 50, 50, 255), sf::Color(255, 255, 255, 255), sf::Color(5, 5, 5, 255));
	Game::Title.TextBoxs[0].setSmartFill(false);
	Game::Title.TextBoxs[0].setCenter(true);
	Game::Title.TextBoxs[0].setFont(Game::mainFont);
	Game::Title.TextBoxs[0].setFontSize(35 * (sqrt(windowSize.x*windowSize.y) / 1440));
	//

	//InfoBox
	Game::Info = Box(sf::Vector2f(Game::windowSize.x/8 + 4*Game::border, 2*Game::border), sf::Vector2f(7*Game::windowSize.x / 8 - 8*Game::border, 150), 1);
	Game::Info.TextBoxs[0] = TextBox(sf::Vector2f(0, 0), sf::Vector2f(0, 0), "");
	Game::Info.TextBoxs[0].setColor(sf::Color(25, 25, 25, 200), sf::Color(255, 255, 255, 255), sf::Color(5, 5, 5, 255));
	Game::Info.TextBoxs[0].setSmartFill(false);
	Game::Info.TextBoxs[0].setCenter(true);
	Game::Info.TextBoxs[0].setFont(Game::mainFont);
	Game::Info.TextBoxs[0].setFontSize(25* (sqrt(windowSize.x*windowSize.y) / 1440));
	//

	//Init form, setting, and window elements
	Game::input1 = textField(sf::Vector2f(windowSize.x / 8 + 4*Game::border, 0), sf::Vector2f(windowSize.x - windowSize.x / 4 - 4 * Game::border, 0), "Search Here");
	Game::input1.setColor(sf::Color(50, 50, 50, 255), sf::Color(255, 255, 255, 255), sf::Color(255, 255, 255, 255));
	Game::input1.setFont(Game::mainFont);
	Game::input1.setAllignY(1*Game::windowSize.y/7+ Game::titleSpace+20);
	Game::input1.setFontSize(Game::fontSize);

	Game::input2 = textField(sf::Vector2f(windowSize.x / 8 + 4*Game::border, 0), sf::Vector2f(windowSize.x - windowSize.x / 4 - 4 * Game::border, 0), "Search Here");
	Game::input2.setColor(sf::Color(50, 50, 50, 255), sf::Color(255, 255, 255, 255), sf::Color(255, 255, 255, 255));
	Game::input2.setFont(Game::mainFont);
	Game::input2.setAllignY(2 * Game::windowSize.y / 7 + Game::titleSpace+20);
	Game::input2.setFontSize(Game::fontSize);

	Game::input3 = textField(sf::Vector2f(windowSize.x / 8 + 4 * Game::border, 0), sf::Vector2f(windowSize.x - windowSize.x / 4 - 4 * Game::border, 0), "Search Here");
	Game::input3.setColor(sf::Color(50, 50, 50, 255), sf::Color(255, 255, 255, 255), sf::Color(255, 255, 255, 255));
	Game::input3.setFont(Game::mainFont);
	Game::input3.setAllignY(3 * Game::windowSize.y / 7 + Game::titleSpace+20);
	Game::input3.setFontSize(Game::fontSize);

	Game::input4 = textField(sf::Vector2f(windowSize.x / 8 + 4 * Game::border, 0), sf::Vector2f(windowSize.x - windowSize.x / 4 - 4 * Game::border, 0), "Search Here");
	Game::input4.setColor(sf::Color(50, 50, 50, 255), sf::Color(255, 255, 255, 255), sf::Color(255, 255, 255, 255));
	Game::input4.setFont(Game::mainFont);
	Game::input4.setAllignY(4 * Game::windowSize.y / 7 + Game::titleSpace+20);
	Game::input4.setFontSize(Game::fontSize);

	Game::input5 = textField(sf::Vector2f(windowSize.x / 8 + 4 * Game::border, 0), sf::Vector2f(windowSize.x - windowSize.x / 4 - 4 * Game::border, 0), "Search Here");
	Game::input5.setColor(sf::Color(50, 50, 50, 255), sf::Color(255, 255, 255, 255), sf::Color(255, 255, 255, 255));
	Game::input5.setFont(Game::mainFont);
	Game::input5.setAllignY(5 * Game::windowSize.y / 7 + Game::titleSpace+20);
	Game::input5.setFontSize(Game::fontSize);

	
	Game::button1 = Box(sf::Vector2f(windowSize.x / 8 + 4 * Game::border, 250), sf::Vector2f(300,75), 4);
	Game::button1.TextBoxs[0] = TextBox(sf::Vector2f(0, 0), sf::Vector2f(0, 0), "Add");
	Game::button1.TextBoxs[0].setColor(sf::Color(50, 50, 50, 255), sf::Color(255, 255, 255, 255), sf::Color(255, 255, 255, 255));
	Game::button1.TextBoxs[0].setSmartFill(false);
	Game::button1.TextBoxs[0].setCenter(true);
	Game::button1.TextBoxs[0].setFont(Game::mainFont);
	Game::button1.TextBoxs[0].setBorder(20);
	Game::button1.TextBoxs[0].setOutlineSize(5);
	Game::button1.TextBoxs[0].setFontSize(Game::fontSize);

	Game::button2 = Box(sf::Vector2f(windowSize.x / 8 + 4 * Game::border, 250), sf::Vector2f(300, 75), 4);
	Game::button2.TextBoxs[0] = TextBox(sf::Vector2f(0, 0), sf::Vector2f(0, 0), "Add");
	Game::button2.TextBoxs[0].setColor(sf::Color(50, 50, 50, 255), sf::Color(255, 255, 255, 255), sf::Color(255, 255, 255, 255));
	Game::button2.TextBoxs[0].setSmartFill(false);
	Game::button2.TextBoxs[0].setCenter(true);
	Game::button2.TextBoxs[0].setFont(Game::mainFont);
	Game::button2.TextBoxs[0].setBorder(20);
	Game::button2.TextBoxs[0].setOutlineSize(5);
	Game::button2.TextBoxs[0].setFontSize(Game::fontSize);


	for (int i = 0; i < 5; i++)
	{
		Game::windowText[i] = Box(sf::Vector2f(0, (Game::windowSize.y / Game::menuSize)*i), sf::Vector2f(Game::windowSize.x / 8, (Game::windowSize.y / Game::menuSize)), 1);
		Game::windowText[i].TextBoxs[0] = TextBox(sf::Vector2f(0, 0), sf::Vector2f(0, 0), "");
		Game::windowText[i].TextBoxs[0].setColor(sf::Color(70, 70, 70, 255), sf::Color(255, 255, 255, 255), sf::Color(5, 5, 5, 255));
		Game::windowText[i].TextBoxs[0].setFont(Game::mainFont);
		Game::windowText[i].TextBoxs[0].setBorder(20);
		Game::windowText[i].TextBoxs[0].setFontSize(Game::fontSize);
	}

	Game::bookDisplay = ChatList(sf::Vector2f(windowSize.x / 8 + Game::border, Game::border), sf::Vector2f(windowSize.x - windowSize.x / 8 - 2 * Game::border, windowSize.y - 2 * Game::border), 20, 20, "Books Checked Out");
	Game::bookDisplay.setColor(sf::Color(35, 35, 35, 255), sf::Color(200, 200, 200, 255));
	Game::bookDisplay.initTitle(Game::mainFont, Game::fontSize, sf::Color(50, 50, 50, 255), sf::Color(255, 255, 255, 255));
	Game::bookDisplay.setDissapear(false);
	Game::initBookDisplay();
	//

	//Init the menu elements
	Game::menuCurrent = 0;
	Game::menuLast = 0;
	for (int i = 0; i < Game::menuSize; i++)
	{
		Game::menu[i] = Box(sf::Vector2f(0, (Game::windowSize.y/Game::menuSize)*i), sf::Vector2f(Game::windowSize.x / 8, (Game::windowSize.y / Game::menuSize)), 4);
		Game::menu[i].TextBoxs[0] = TextBox(sf::Vector2f(0, 0), sf::Vector2f(0, 0), "");
		Game::menu[i].TextBoxs[0].setColor(sf::Color(50, 50, 50, 255), sf::Color(255, 255, 255, 255), sf::Color(5, 5, 5, 255));
		Game::menu[i].TextBoxs[0].setSmartFill(false);
		Game::menu[i].TextBoxs[0].setCenter(true);
		Game::menu[i].TextBoxs[0].setFont(Game::mainFont);
		Game::menu[i].TextBoxs[0].setBorder(20);
		Game::menu[i].TextBoxs[0].setOutlineSize(Game::border);
		Game::menu[i].TextBoxs[0].setFontSize(Game::fontSize);
	}
	Game::menu[0].TextBoxs[0].setSelected(true);
	Game::menu[0].TextBoxs[0].changeText("Book Search");
	Game::menu[1].TextBoxs[0].changeText("User Search");
	Game::menu[2].TextBoxs[0].changeText("Add Book");
	Game::menu[3].TextBoxs[0].changeText("Add User");
	Game::menu[4].TextBoxs[0].changeText("Settings");
	Game::menu[5].TextBoxs[0].changeText("Print Report");
	Game::menu[6].TextBoxs[0].changeText("Exit");
	//

	Game::setInfo("Welcome! Try searching for a book below!", 0);
}

/**
	Initializes Game Element: mainSearch. 
*/
void Game::initMainSearch()
{
	for (int i = 0; i < 50; i++)
	{
		Game::mainSearch.chat[Game::mainSearch.current] = Box(sf::Vector2f(0, 0), sf::Vector2f(0, 0), 5);
		for (int j = 0; j < 3; j++)
		{
			Game::mainSearch.chat[Game::mainSearch.current].TextBoxs[j] = TextBox(sf::Vector2f(0, 0), sf::Vector2f(0, 0), "Hello\0");
			Game::mainSearch.chat[Game::mainSearch.current].TextBoxs[j].setColor(sf::Color(70, 70, 70, 255), sf::Color(255, 255, 255, 255));
			Game::mainSearch.chat[Game::mainSearch.current].TextBoxs[j].setFont(Game::mainFont);
			Game::mainSearch.chat[Game::mainSearch.current].TextBoxs[j].setBorder(20);
			Game::mainSearch.chat[Game::mainSearch.current].TextBoxs[j].setFontSize(Game::fontSize);
		}
		Game::mainSearch.chat[Game::mainSearch.current].ImageBox1 = ImageBox(sf::Vector2f(50, 50), sf::Vector2f(100, 100), &(Game::textures[0]));
		Game::mainSearch.chat[Game::mainSearch.current].ImageBox1.setBorder(5);
		Game::mainSearch.chat[Game::mainSearch.current].ImageBox1.setColor(sf::Color(75, 75, 75, 255), sf::Color(255, 255, 255, 255));

		Game::mainSearch.current++;
		Game::mainSearch.numel++;
	}
	Game::updateMainSearch();
}

/**
	Initializes Game Element: bookDisplay.
*/
void Game::initBookDisplay()
{
	for (int i = 0; i < 50; i++)
	{
		Game::bookDisplay.chat[Game::bookDisplay.current] = Box(sf::Vector2f(0, 0), sf::Vector2f(0, 0), 5);
		for (int j = 0; j < 3; j++)
		{
			Game::bookDisplay.chat[Game::bookDisplay.current].TextBoxs[j] = TextBox(sf::Vector2f(0, 0), sf::Vector2f(0, 0), "\0");
			Game::bookDisplay.chat[Game::bookDisplay.current].TextBoxs[j].setColor(sf::Color(70, 70, 70, 255), sf::Color(255, 255, 255, 255));
			Game::bookDisplay.chat[Game::bookDisplay.current].TextBoxs[j].setFont(Game::mainFont);
			Game::bookDisplay.chat[Game::bookDisplay.current].TextBoxs[j].setBorder(20);
			Game::bookDisplay.chat[Game::bookDisplay.current].TextBoxs[j].setFontSize(Game::fontSize);
		}
		Game::bookDisplay.chat[Game::bookDisplay.current].ImageBox1 = ImageBox(sf::Vector2f(50, 50), sf::Vector2f(100, 100), &(Game::textures[0]));
		Game::bookDisplay.chat[Game::bookDisplay.current].ImageBox1.setBorder(5);
		Game::bookDisplay.chat[Game::bookDisplay.current].ImageBox1.setColor(sf::Color(75, 75, 75, 255), sf::Color(255, 255, 255, 255));

		Game::bookDisplay.current++;
		Game::bookDisplay.numel++;
	}
}

/**
	Main game loop. Updates and Draws Game.
*/
void Game::run()
{
	while (window.isOpen())
	{
		Game::update();
		Game::draw();
	}
}