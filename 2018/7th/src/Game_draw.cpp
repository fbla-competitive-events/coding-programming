#include <SFML/Graphics.hpp>
#include "Game.h"
#include <iostream>
using namespace std;

/**
	Main draw function. Controls the drawing
	of main Game Elements.

	Draws different Game Elements depending
	on the state of the Game.
*/
void Game::draw()
{
	Game::window.clear(sf::Color(5,5,5,255));
	
	//Game Elements//
	if (Game::menuCurrent == 0 || Game::menuCurrent == 1)
	{
		if (Game::infoWindowActive == 0)
		{
			Game::mainSearch.draw(&(Game::window));
			Game::userInput.draw(&(Game::window));
		}
		if (Game::infoWindowActive == 1)
		{
			Game::drawInfoWindowBook();
		}
		if (Game::infoWindowActive == 2)
		{
			Game::drawInfoWindowUser();
		}
	}
	if (Game::menuCurrent == 2|| Game::menuCurrent == 3)
	{
		Game::drawForm();
	}
	if (Game::menuCurrent == 4)
	{
		Game::drawSettings();
	}

	for (int i = 0; i < Game::menuSize; i++)
	{
		Game::menu[i].draw(&(Game::window));
	}
	//
	

	Game::drawBorder();
	Game::Title.draw(&(Game::window));
	Game::drawInfo();
	//Game::drawDebugText();
	Game::window.display();
}

/**
	Draws the Game Element: Info
*/
void Game::drawInfo()
{
	if (Game::infoBoxCoolDown > 0)
	{
		Game::Info.draw(&(Game::window));
	}
}

/**
	Draws a collection Game Elements
	to display the Book information
	window.
*/
void Game::drawInfoWindowBook()
{
	sf::RectangleShape back;
	back.setPosition(sf::Vector2f(windowSize.x / 8 + Game::border, Game::border+ Game::titleSpace));
	back.setOutlineColor(sf::Color(35, 35, 35, 255));
	back.setOutlineThickness(0);
	back.setFillColor(sf::Color(35, 35, 35, 255));
	back.setSize(sf::Vector2f(windowSize.x - windowSize.x / 8 - 2 * Game::border, windowSize.y - 2 * Game::border- Game::titleSpace));
	window.draw(back);

	sf::Vector2f oldPos = Game::mainSearch.chat[Game::chatIndexWindowElement].getPos();
	sf::Vector2f oldSize = Game::mainSearch.chat[Game::chatIndexWindowElement].getSize();
	Game::mainSearch.chat[Game::chatIndexWindowElement].setPos(sf::Vector2f(windowSize.x / 8 + 3 * Game::border, 3*Game::border+ Game::titleSpace));
	Game::mainSearch.chat[Game::chatIndexWindowElement].setSize(sf::Vector2f(windowSize.x - windowSize.x / 8 - 6 * Game::border, oldSize.y));
	Game::mainSearch.chat[Game::chatIndexWindowElement].setAlpha(255);
	Game::mainSearch.chat[Game::chatIndexWindowElement].update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
	Game::mainSearch.chat[Game::chatIndexWindowElement].draw(&(Game::window));
	Game::mainSearch.chat[Game::chatIndexWindowElement].setPos(oldPos);
	Game::mainSearch.chat[Game::chatIndexWindowElement].setSize(oldSize);

	Game::windowText[0].draw(&(Game::window));

	Game::button1.draw(&(Game::window));
	Game::button2.draw(&(Game::window));
}

/**
	Draws a collection Game Elements
	to display the User information
	window.
*/
void Game::drawInfoWindowUser()
{
	sf::RectangleShape back;
	back.setPosition(sf::Vector2f(windowSize.x / 8 + Game::border, Game::border+ Game::titleSpace));
	back.setOutlineColor(sf::Color(35, 35, 35, 255));
	back.setOutlineThickness(0);
	back.setFillColor(sf::Color(35, 35, 35, 255));
	back.setSize(sf::Vector2f(windowSize.x - windowSize.x / 8 - 2 * Game::border, windowSize.y - 2 * Game::border- Game::titleSpace));
	window.draw(back);

	Game::bookDisplay.draw(&(Game::window));

	back.setPosition(sf::Vector2f(windowSize.x / 8 + Game::border, Game::border + Game::titleSpace));
	back.setOutlineColor(sf::Color(35, 35, 35, 255));
	back.setOutlineThickness(0);
	back.setFillColor(sf::Color(35, 35, 35, 255));
	back.setSize(sf::Vector2f(windowSize.x - windowSize.x / 8 - 2 * Game::border, windowSize.y - (windowSize.y-Game::bookDisplay.getPos().y) - Game::border*1 - Game::titleSpace));
	window.draw(back);

	

	sf::Vector2f oldPos = Game::mainSearch.chat[Game::chatIndexWindowElement].getPos();
	sf::Vector2f oldSize = Game::mainSearch.chat[Game::chatIndexWindowElement].getSize();
	Game::mainSearch.chat[Game::chatIndexWindowElement].setPos(sf::Vector2f(windowSize.x / 8 + 3 * Game::border, 3 * Game::border+ Game::titleSpace));
	Game::mainSearch.chat[Game::chatIndexWindowElement].setSize(sf::Vector2f(windowSize.x - windowSize.x / 8 - 6 * Game::border, oldSize.y));
	Game::mainSearch.chat[Game::chatIndexWindowElement].setAlpha(255);
	Game::mainSearch.chat[Game::chatIndexWindowElement].update(Game::mousePos, Game::mouseDownL, &(Game::numTyped), Game::typed, Game::keyPressed[int(sf::Keyboard::Return)], Game::fps);
	Game::mainSearch.chat[Game::chatIndexWindowElement].draw(&(Game::window));
	Game::mainSearch.chat[Game::chatIndexWindowElement].setPos(oldPos);
	Game::mainSearch.chat[Game::chatIndexWindowElement].setSize(oldSize);

	Game::button1.draw(&(Game::window));
	Game::button2.draw(&(Game::window));

	
}

/**
	Draws a collection Game Elements
	to display the 'Add User' and 
	'Add Book' window.
*/
void Game::drawForm()
{
	sf::RectangleShape back;
	back.setPosition(sf::Vector2f(windowSize.x / 8 + Game::border, Game::border + Game::titleSpace));
	back.setOutlineColor(sf::Color(35, 35, 35, 255));
	back.setOutlineThickness(0);
	back.setFillColor(sf::Color(35, 35, 35, 255));
	back.setSize(sf::Vector2f(windowSize.x - windowSize.x / 8 - 2 * Game::border, windowSize.y - 2 * Game::border- Game::titleSpace));
	window.draw(back);

	Game::input1.draw(&(Game::window));
	Game::input2.draw(&(Game::window));
	Game::button1.draw(&(Game::window));
}

/**
	Draws a collection Game Elements
	to display the 'Settings' window.
*/
void Game::drawSettings()
{
	sf::RectangleShape back;
	back.setPosition(sf::Vector2f(windowSize.x / 8 + Game::border, Game::border+ Game::titleSpace));
	back.setOutlineColor(sf::Color(35, 35, 35, 255));
	back.setOutlineThickness(0);
	back.setFillColor(sf::Color(35, 35, 35, 255));
	back.setSize(sf::Vector2f(windowSize.x - windowSize.x / 8 - 2 * Game::border, windowSize.y - 2 * Game::border- Game::titleSpace));
	window.draw(back);

	Game::input1.draw(&(Game::window));
	Game::input2.draw(&(Game::window));
	Game::input3.draw(&(Game::window));
	Game::input4.draw(&(Game::window));
	Game::input5.draw(&(Game::window));
}

/**
	Draws a black border around game
	to cover the drawing of unwanted
	Game Elements.
*/
void Game::drawBorder()
{
	sf::RectangleShape out;
	out.setSize(sf::Vector2f(Game::windowSize.x, Game::border));
	out.setOutlineColor(sf::Color(0, 0, 0, 255));
	out.setOutlineThickness(0);
	out.setFillColor(sf::Color(0, 0, 0, 255));
	out.setPosition(sf::Vector2f(0,0));
	Game::window.draw(out);

	out.setSize(sf::Vector2f(Game::windowSize.x - Game::windowSize.x/8, Game::titleSpace + Game::border));
	out.setOutlineColor(sf::Color(0, 0, 0, 255));
	out.setOutlineThickness(0);
	out.setFillColor(sf::Color(0, 0, 0, 255));
	out.setPosition(sf::Vector2f(Game::windowSize.x/8, 0));
	Game::window.draw(out);

	out.setSize(sf::Vector2f(Game::windowSize.x, Game::border));
	out.setOutlineColor(sf::Color(0, 0, 0, 255));
	out.setOutlineThickness(0);
	out.setFillColor(sf::Color(0, 0, 0, 255));
	out.setPosition(sf::Vector2f(0, Game::windowSize.y - Game::border));
	Game::window.draw(out);
}

/**
	Draws text meant to debug Game.
*/
void Game::drawDebugText()
{
	for (int i = 0; i < 10; i++)
	{
		Game::debugText[i].setFont(Game::mainFont);
		Game::debugText[i].setCharacterSize(20);
		Game::debugText[i].setFillColor(sf::Color(255, 255, 255));
		Game::debugText[i].setPosition(10, 10 + 25 * i);
		Game::window.draw(Game::debugText[i]);
	}
}