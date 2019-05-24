#pragma once
#include "Box.h"
#include "TextBox.h"

/**
	Game Element: ChatList
	Must be initialized, updated, and drawn.

	Definition of a ChatList. A ChatList
	is used to display and store a list of
	Boxes.
*/
class ChatList
{
private:

	bool isActive;
	bool jumpToTop;
	float scrollHeight;
	float scrollVel;
	int border;
	int spacing;
	sf::Color colorBack;
	sf::Vector2f pos;
	sf::Vector2f size;
	sf::RectangleShape back;
	int alpha;
	int fontSize;
	sf::RectangleShape scrollBar;
	sf::Color barColor;
	int barY;
	int barH;
	bool dissapear;

public:

	Box chat[50];
	int current;
	int numel;
	int maxel;
	TextBox title;

	ChatList() {};
	ChatList(sf::Vector2f pos, sf::Vector2f size, int border, int spacing, char title[200]);
	void setColor(sf::Color colorBack, sf::Color barColor);
	void setFont(sf::Font mainFont);
	void update(sf::Vector2i mousePos, bool mouseDownL, int* numTyped, char typed[200], bool enterPressed, int fps, bool up, bool down, int inputY, sf::Vector2f mouseDist);
	void initTitle(sf::Font mainFont, int fontSize, sf::Color colorBack, sf::Color colorText);
	void draw(sf::RenderWindow* window);
	void setAlpha(int alpha);
	sf::Vector2f getPos();
	sf::Vector2f getSize();
	void setPos(sf::Vector2f pos);
	void setSize(sf::Vector2f size);
	char* getTitleText();
	void setJumpToTop();
	void setDissapear(bool dissapear);
};

