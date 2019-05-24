#pragma once
#include "textField.h"
#include "ImageBox.h"
#include "TextBox.h"
/**
	Game Element: Box
	Must be initialized, updated, and drawn.

	Definition of a Box. A Box is a collection
	of other simpler Game Elements such as
	TextBoxes, TextFields, and ImageBoxes. 
	Boxes are used in ChatLists to store 
	individial elements.
*/
class Box
{
	sf::Vector2f pos;
	sf::Vector2f size;
	bool active;
	bool selected;

public:
	
	int format;
	TextBox TextBoxs[3];
	ImageBox ImageBox1;
	int alpha;

	Box(sf::Vector2f pos, sf::Vector2f size, int format);
	Box() {};
	void setFormat(int format);
	void setPos(sf::Vector2f pos);
	void setSize(sf::Vector2f size);
	sf::Vector2f getPos();
	sf::Vector2f getSize();
	void update(sf::Vector2i mousePos, bool mouseDownL, int* numTyped, char typed[200], bool enterPressed, int fps);
	void draw(sf::RenderWindow* window);
	void setAlpha(int alpha);
	void setActive(bool active);
	bool getActive();
	void setSelectecd(bool selected);
	bool getSelected();	
};

