#include <SFML/Graphics.hpp>
#include "Box.h"
#include <iostream>
using namespace std;
Box::Box(sf::Vector2f pos, sf::Vector2f size, int format)
{
	Box::pos = pos;
	Box::size = size;
	Box::format = format;
	Box::alpha = 255;
}
void Box::setFormat(int format)
{
	Box::format = format;
}
void Box::update(sf::Vector2i mousePos, bool mouseDownL, int* numTyped, char typed[200], bool enterPressed, int fps)
{
	Box::active = false;
	Box::selected = false;
	int imageW = 100;
	switch (Box::format)
	{
	case 1:
		Box::TextBoxs[0].setPos(Box::pos);
		Box::TextBoxs[0].setSize(Box::size);
		Box::TextBoxs[0].update(mousePos, mouseDownL);
		Box::TextBoxs[0].setAlpha(Box::alpha);
		Box::size = sf::Vector2f(Box::size.x, Box::TextBoxs[0].getSize().y);
		Box::TextBoxs[0].setSelected(false);
		break;
	case 2:
		Box::ImageBox1.setPos(Box::pos);
		Box::ImageBox1.setSize(Box::size);
		Box::ImageBox1.setAlpha(Box::alpha);
		Box::ImageBox1.update(mousePos);
		break;
	case 3:
		/**
		Box::textField1.setPos(Box::pos);
		Box::textField1.setSize(Box::size);
		Box::textField1.setAlpha(Box::alpha);
		Box::textField1.update(mousePos, mouseDownL, numTyped, typed, enterPressed, fps);
		*/
		break;
	case 4: //menu element
		Box::TextBoxs[0].setPos(Box::pos);
		Box::TextBoxs[0].setSize(Box::size);
		Box::TextBoxs[0].update(mousePos, mouseDownL);
		Box::TextBoxs[0].setAlpha(Box::alpha);
		Box::size = sf::Vector2f(Box::size.x, Box::TextBoxs[0].getSize().y);
		if (Box::TextBoxs[0].getActive())
		{
			Box::active = true;
			if (mouseDownL)
				Box::selected = true;
		}
		break;
	case 5: //book
		Box::TextBoxs[0].setPos(sf::Vector2f(Box::pos.x + imageW, Box::pos.y));
		Box::TextBoxs[0].setSize(sf::Vector2f(Box::size.x*5/6- imageW -5, Box::size.y));
		Box::TextBoxs[0].update(mousePos, mouseDownL);
		Box::TextBoxs[0].setAlpha(Box::alpha);
		Box::TextBoxs[0].setSelected(false);

		Box::TextBoxs[1].setPos(sf::Vector2f(Box::TextBoxs[0].getSize().x+ Box::TextBoxs[0].getPos().x+5, Box::pos.y));
		Box::TextBoxs[1].setSize(sf::Vector2f(Box::size.x * 1 / 6, 0));
		Box::TextBoxs[1].setLength(Box::TextBoxs[0].getSize().y);
		Box::TextBoxs[1].setSmartFill(false);
		Box::TextBoxs[1].setCenter(true);
		Box::TextBoxs[1].update(mousePos, mouseDownL);
		Box::TextBoxs[1].setAlpha(Box::alpha);
		Box::TextBoxs[1].setSelected(false);

		Box::ImageBox1.setPos(Box::pos);
		Box::ImageBox1.setSize(sf::Vector2f(imageW -5, Box::TextBoxs[0].getSize().y));
		Box::ImageBox1.setAlpha(Box::alpha);
		Box::ImageBox1.update(mousePos);
		
		Box::TextBoxs[2].setPos(sf::Vector2f(Box::pos.x, Box::pos.y + Box::TextBoxs[0].getSize().y+5));
		Box::TextBoxs[2].setSize(sf::Vector2f(Box::size.x, 0));
		Box::TextBoxs[2].update(mousePos, mouseDownL);
		Box::TextBoxs[2].setAlpha(Box::alpha);
		Box::TextBoxs[2].setSelected(false);

		if (Box::TextBoxs[0].getActive() || Box::TextBoxs[1].getActive() || Box::TextBoxs[2].getActive() || Box::ImageBox1.getActive())
		{
			Box::TextBoxs[0].setActive(true);
			Box::TextBoxs[1].setActive(true);
			Box::TextBoxs[2].setActive(true);
			Box::ImageBox1.setActive(true);
			Box::active = true;
			if (mouseDownL)
				Box::selected = true;
		}

		Box::size = sf::Vector2f(Box::size.x, Box::TextBoxs[0].getSize().y + 5 + Box::TextBoxs[2].getSize().y);
		break;
	default:
		break;
	}
	
}
void Box::draw(sf::RenderWindow* window)
{
	switch (Box::format)
	{
	case 1:
		Box::TextBoxs[0].draw(window);
		break;
	case 2:
		Box::ImageBox1.draw(window);
		break;
	case 3:
		//Box::textField1.draw(window);
	case 4:
		Box::TextBoxs[0].draw(window);
		break;
	case 5:
		for(int i = 0; i <3; i++)
			Box::TextBoxs[i].draw(window);
		Box::ImageBox1.draw(window);
		break;
	default:
			break;
	}
}
void Box::setPos(sf::Vector2f pos)
{
	Box::pos = pos;
}
void Box::setSize(sf::Vector2f size)
{
	Box::size = size;
}
sf::Vector2f Box::getPos()
{
	return Box::pos;
}
sf::Vector2f Box::getSize()
{
	return Box::size;
}
void Box::setAlpha(int alpha)
{
	Box::alpha = alpha;
}
void Box::setActive(bool active)
{
	Box::active = active;
}
bool Box::getActive()
{
	return Box::active;
}
void Box::setSelectecd(bool selected)
{
	Box::selected = selected;
}
bool Box::getSelected()
{
	return Box::selected;
}
