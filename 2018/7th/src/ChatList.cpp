#include <SFML/Graphics.hpp>
#include <iostream>
#include "ChatList.h"
using namespace std;

ChatList::ChatList(sf::Vector2f pos, sf::Vector2f size, int border, int spacing, char title[200])
{
	ChatList::pos = pos;
	ChatList::size = size;
	ChatList::isActive = false;
	ChatList::current = 0;
	ChatList::numel = 0;
	ChatList::border = border;
	ChatList::spacing = spacing;
	ChatList::maxel = 50;
	ChatList::scrollHeight = 0;
	ChatList::scrollVel = 0;
	ChatList::alpha = 255;
	ChatList::fontSize = 20;
	ChatList::dissapear = true;

	ChatList::title = TextBox(ChatList::pos, sf::Vector2f(ChatList::size.x, 0), title);
	ChatList::title.setAlpha(ChatList::alpha);
	ChatList::title.setBorder(ChatList::border);
	ChatList::title.setFontSize(ChatList::fontSize);
}
void ChatList::setColor(sf::Color colorBack, sf::Color barColor)
{
	ChatList::colorBack = colorBack;
	ChatList::barColor = barColor;
}
void ChatList::initTitle(sf::Font mainFont, int fontSize, sf::Color colorBack, sf::Color colorText)
{
	ChatList::title.setFont(mainFont);
	ChatList::title.setFontSize(fontSize);
	ChatList::title.setColor(colorBack, colorText);
}
void ChatList::draw(sf::RenderWindow* window)
{
	ChatList::back.setSize(ChatList::size);
	ChatList::back.setOutlineColor(ChatList::colorBack);
	ChatList::back.setOutlineThickness(0);
	ChatList::back.setFillColor(ChatList::colorBack);
	ChatList::back.setPosition(ChatList::pos);
	window->draw(ChatList::back);

	ChatList::scrollBar.setSize(sf::Vector2f(ChatList::border,ChatList::barH));
	ChatList::scrollBar.setOutlineColor(ChatList::barColor);
	ChatList::scrollBar.setOutlineThickness(0);
	ChatList::scrollBar.setFillColor(ChatList::barColor);
	ChatList::scrollBar.setPosition(sf::Vector2f(ChatList::pos.x+ ChatList::size.x - ChatList::border, ChatList::barY));
	window->draw(ChatList::scrollBar);

	int tolerance = 500;
	for (int i = 0; i < ChatList::numel; i++)
	{
		float ratio;
		if (ChatList::chat[i].getPos().y >= ChatList::pos.y - tolerance&& ChatList::chat[i].getPos().y + ChatList::chat[i].getSize().y <= ChatList::pos.y + ChatList::size.y+ tolerance)
		{
			
			ratio = abs(ChatList::chat[i].getPos().y + ChatList::chat[i].getSize().y - ChatList::pos.y - ChatList::size.y);
			if(ratio > abs(ChatList::chat[i].getPos().y - ChatList::pos.y))
				ratio = abs(ChatList::chat[i].getPos().y - ChatList::pos.y);

			//cout << "Total len: " << ChatList::size.y << ' ';
			//cout << "Dist: " <<ratio << ' ';
			if (dissapear == true)
			{
				ratio = ratio / (ChatList::size.y / 2);
				if (!(ChatList::chat[i].getPos().y >= ChatList::pos.y && ChatList::chat[i].getPos().y + ChatList::chat[i].getSize().y <= ChatList::pos.y + ChatList::size.y))
					continue;
				ratio = pow(ratio, 1.0 / 3.0);
				ratio *= 255;
			}
			else
			{
				ratio = ratio / (ChatList::size.y / 2);
				if (!(ChatList::chat[i].getPos().y >= ChatList::pos.y && ChatList::chat[i].getPos().y + ChatList::chat[i].getSize().y <= ChatList::pos.y + ChatList::size.y))
					ratio = 0.01;
				ratio = pow(ratio, 1.0 / 1.0);
				ratio *= 150;
				ratio += 105;
			}
			ChatList::chat[i].alpha = ratio;

			//cout << "Ratio: " << ratio << '\n';
			//ChatList::chat[i].alpha = 100;
			ChatList::chat[i].draw(window);
		}
	}

	ChatList::title.draw(window);
	
}
void ChatList::update(sf::Vector2i mousePos, bool mouseDownL, int* numTyped, char typed[200], bool enterPressed, int fps, bool up, bool down, int inputY, sf::Vector2f mouseDist)
{
	ChatList::title.update(mousePos, mouseDownL);

	if (mouseDownL == true || mouseDownL == false)
	{

		if (ChatList::pos.x < mousePos.x && ChatList::pos.y < mousePos.y && ChatList::pos.x + ChatList::size.x > mousePos.x && ChatList::pos.y + ChatList::size.y > mousePos.y)
		{
			if (ChatList::isActive == false)
			{

			}
			ChatList::isActive = true;
		}
		else
		{
			if (ChatList::isActive == true)
			{

			}
			ChatList::isActive = false;
		}
	}
	float speed = 2.0*300/fps;
	if (ChatList::isActive == true)
	{
		if(up == true)
			ChatList::scrollVel += speed;
		if (down == true)
			ChatList::scrollVel -= speed;
	}
	//Update Members
	int height = ChatList::pos.y + ChatList::size.y - ChatList::border -inputY;
	int height2 = 0;
	int startHeight = height;
	int modified;
	for (int i = 0; i < ChatList::numel; i++)
	{
		modified = (ChatList::current - 1 - i + 100 * ChatList::maxel) % ChatList::maxel;

		height -= ChatList::chat[modified].getSize().y + ChatList::spacing;
		height2 += ChatList::chat[modified].getSize().y + ChatList::spacing;;
		if (i == 0)
		{
			height += ChatList::spacing;
			height2 -= ChatList::spacing;
		}
			
		ChatList::chat[modified].setPos(sf::Vector2f(ChatList::pos.x + ChatList::border, height + ChatList::scrollHeight));
		ChatList::chat[modified].setSize(sf::Vector2f(ChatList::size.x - ChatList::border * 3, 100));
		ChatList::chat[modified].update(mousePos, mouseDownL, numTyped, typed, enterPressed, fps);
		
	}

	//update Scroll
	float maxVel = 5.0 * 300 / fps;
	ChatList::scrollVel *= 0.95;
	if (ChatList::scrollVel > maxVel)
		ChatList::scrollVel = maxVel;
	if (ChatList::scrollVel < -maxVel)
		ChatList::scrollVel = -maxVel;
	
	if (ChatList::scrollHeight < -1)
	{
		ChatList::scrollHeight -= (ChatList::scrollHeight - -1)*0.3;
		ChatList::scrollVel *= 0.5;

	}
	int approach = -(height-startHeight) - (ChatList::size.y - ChatList::border*2 - inputY) + ChatList::title.getSize().y;
	
	if (ChatList::scrollHeight > approach + 1)
	{
		ChatList::scrollHeight -= (ChatList::scrollHeight - (approach + 1))*0.3;
		ChatList::scrollVel *= 0.5;
	}
	if (abs(ChatList::scrollVel) < 0.25)
		ChatList::scrollVel = 0;

	
	ChatList::scrollHeight += ChatList::scrollVel;
	ChatList::scrollHeight = round(ChatList::scrollHeight);

	//update scroll bar
	int totalSize = ChatList::size.y - inputY - ChatList::title.getSize().y;
	int barSize;
	if (approach > 0)
	{
		barSize = totalSize*totalSize/(height2+10);
		if (barSize < 20)
			barSize = 20;
	}
	else
	{
		barSize = totalSize;
		ChatList::scrollVel = 0;
	}
	int totalMoveSpace = totalSize - barSize;
	int error = 20;
	sf::Vector2f barS(ChatList::border, ChatList::barH);
	sf::Vector2f barP(ChatList::pos.x + ChatList::size.x - ChatList::border, ChatList::barY);
	//mouseDownL && mousePos.y > barP.y - error && mousePos.y < barP.y + error + barS.y && 
	if (mouseDownL && mousePos.x > barP.x - error && mousePos.x < barP.x + error + barS.x)
	{
		if (totalMoveSpace != 0)
		{
			barY += mouseDist.y;
			if (barY > ChatList::pos.y + ChatList::title.getSize().y + totalMoveSpace)
				barY = ChatList::pos.y + ChatList::title.getSize().y + totalMoveSpace;
			if (barY < ChatList::pos.y + ChatList::title.getSize().y)
				barY = ChatList::pos.y + ChatList::title.getSize().y;

			float hold = (ChatList::barY - ChatList::pos.y - ChatList::title.getSize().y) / totalMoveSpace;
			ChatList::scrollHeight = (1 - hold) * approach;
		}
	}
	else
	{
		float ratioMoved = float(ChatList::scrollHeight) / approach;
		int distFromTop = (1 - ratioMoved) * totalMoveSpace;

		ChatList::barY = ChatList::pos.y + ChatList::title.getSize().y + distFromTop;
	}
	if (ChatList::jumpToTop == true)
	{
		if (totalMoveSpace != 0)
		{
			barY = ChatList::pos.y + ChatList::title.getSize().y;
			float hold = (ChatList::barY - ChatList::pos.y - ChatList::title.getSize().y) / totalMoveSpace;
			ChatList::scrollHeight = (1 - hold) * approach;
			ChatList::jumpToTop = false;
		}
		ChatList::scrollVel = 0;
	}
	ChatList::barH = barSize;
	ChatList::title.setSelected(false);
}
void ChatList::setPos(sf::Vector2f pos)
{
	ChatList::pos = pos;
	ChatList::title.setPos(ChatList::pos);
}
void ChatList::setSize(sf::Vector2f size)
{
	ChatList::size = size;
	ChatList::title.setSize(sf::Vector2f(ChatList::size.x, 0));
}
void ChatList::setAlpha(int alpha)
{
	ChatList::alpha = alpha;
}
sf::Vector2f ChatList::getPos()
{
	return ChatList::pos;
}
sf::Vector2f ChatList::getSize()
{
	return ChatList::size;
}
char* ChatList::getTitleText()
{
	return ChatList::title.getText();
}
void ChatList::setJumpToTop()
{
	ChatList::jumpToTop = true;
}
void ChatList::setDissapear(bool dissapear)
{
	ChatList::dissapear = dissapear;
}