#pragma once
#include "TextBox.h"

/**
	Game Element: TextField
	Must be initialized, updated, and drawn.

	Definition of a TextField. A TextField 
	is used to store, read, and display user
	input.
*/
class textField
{
private:

	char stringC[200];
	char savedC[200];
	bool blink;
	int blinkCoolDown;
	bool isActive;
	bool growTop;
	bool allignBottom;
	int prevLength;
	char emptyC[200];
	sf::Color colorBack;
	sf::Color colorCursor;
	sf::Color colorText;
	int regAlpha;
	int originalPos;
	int allignY;
	sf::Vector2f pos;
	sf::Vector2f size;
	TextBox textDrawer;
	sf::Text stringT;
	int fontSize;
	int textBorder;
	sf::Font mainFont;
	sf::RectangleShape back;
	sf::RectangleShape underline;
	int underlineSize;
	sf::RectangleShape cursor;
	bool forceEnter;
	bool clear;
	int alpha;

public:

	textField() {};
	textField(sf::Vector2f pos, sf::Vector2f size, char emptyC[200]);
	void setColor(sf::Color colorBack, sf::Color colorCursor, sf::Color colorText);
	void setFont(sf::Font mainFont);
	void update(sf::Vector2i mousePos, bool mouseDownL, int* numTyped, char typed[200], bool enterPressed, int fps);
	void draw(sf::RenderWindow* window);

	void resetSaved();
	void changeEmpty(char typed[200]);
	char* getSaved();
	char* getEmpty();
	void setPos(sf::Vector2f pos);
	void setSize(sf::Vector2f size);
	void setBorder(int border);
	void setFontSize(int fontSize);
	sf::Vector2f getPos();
	sf::Vector2f getSize();
	void setAlpha(int alpha);
	void setGrowTop(int growTop);
	void setAllignBottom(int allignBottom);
	void setAllignY(int allignY);
	void forcedEnter();
	void cleared();
};

