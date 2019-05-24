#include <SFML/Graphics.hpp>
#include "textField.h"
#include <iostream>

using namespace std;
/**
	Constructor.

	@param pos - pos of TextField.
	@param size - size of TextField.
	@param emptyC - string to display when TextField is empty.
*/
textField::textField(sf::Vector2f pos, sf::Vector2f size, char emptyC[200])
{
	textField::blink = true;
	textField::blinkCoolDown = 0;
	textField::pos = pos;
	textField::size = size;
	textField::isActive = false;
	textField::textBorder = 10;
	textField::fontSize = 30;
	textField::forceEnter = false;
	textField::underlineSize = 3;
	strcpy_s(textField::stringC, "\0");
	textField::prevLength = 0;
	strcpy_s(textField::savedC, "\0");

	int i = 0;
	while (emptyC[i] != '\0')
	{
		textField::emptyC[i] = emptyC[i];
		i++;
	}
	textField::emptyC[i] = '\0';
	textField::alpha = 255;

	textField::growTop = true;
	textField::allignBottom = false;
	textField::allignY = 0;

	textField::textDrawer = TextBox(textField::pos, textField::pos, "");
	textField::textDrawer.setAlpha(textField::alpha);
	textField::textDrawer.setNoBack(true);
	textField::textDrawer.setBorder(textField::textBorder);
	textField::textDrawer.setFontSize(textField::fontSize);

	originalPos = textField::size.y;
}

/**
	Initialize colors.

	@param colorBack - color of back box.
	@param colorCursor - color of blinking cursor.
	@param colorText - color of text.
*/
void textField::setColor(sf::Color colorBack, sf::Color colorCursor, sf::Color colorText)
{
	textField::colorBack = colorBack;
	textField::colorCursor = colorCursor;
	textField::colorText = colorText;
	textField::regAlpha = textField::colorText.a;

	textField::textDrawer.setColor(sf::Color(255,255,255,255), textField::colorText);
}

/**
	Initialize font.

	@param font - font.
*/
void textField::setFont(sf::Font mainFont)
{
	textField::mainFont = mainFont;

	textField::textDrawer.setFont(textField::mainFont);
}

/**
	Draw the TextField.

	@param window - window to render onto.
*/
void textField::draw(sf::RenderWindow* window)
{
	textField::colorBack.a = textField::alpha;
	textField::back.setSize(textField::size);
	textField::back.setOutlineColor(textField::colorBack);
	textField::back.setOutlineThickness(0);
	textField::back.setFillColor(textField::colorBack);
	textField::back.setPosition(textField::pos);
	window->draw(textField::back);


	textField::colorBack.a = textField::alpha;
	textField::underline.setSize(sf::Vector2f(textField::size.x, textField::underlineSize));
	textField::underline.setOutlineColor(textField::colorCursor);
	textField::underline.setOutlineThickness(0);
	textField::underline.setFillColor(textField::colorCursor);
	textField::underline.setPosition(sf::Vector2f(textField::pos.x, textField::pos.y + textField::size.y- textField::underlineSize));
	window->draw(textField::underline);

	textField::stringT.setFont(textField::mainFont);
	
	//Regular print
	if (blink == true)
	{
		char temp[202];
		strcpy_s(temp, textField::stringC);
		temp[strlen(textField::stringC)] = '|';
		temp[strlen(textField::stringC) + 1] = '\0';


		textField::textDrawer.setAlpha(textField::alpha);
		textField::textDrawer.changeText(temp);
		if (strlen(textField::stringC) == 0)
		{
			strcat_s(temp, textField::emptyC);
			textField::textDrawer.changeText(temp);
			textField::textDrawer.setAlpha(textField::alpha * 2.0 / 3.0);
		}
	}
	else
	{
		char temp[202];
		strcpy_s(temp, textField::stringC);
		temp[strlen(textField::stringC)] = ' ';
		temp[strlen(textField::stringC) + 1] = '\0';


		textField::textDrawer.setAlpha(textField::alpha);
		textField::textDrawer.changeText(temp);
		if (strlen(textField::stringC) == 0)
		{
			strcat_s(temp, textField::emptyC);
			textField::textDrawer.changeText(temp);
			textField::textDrawer.setAlpha(textField::alpha * 2.0 / 3.0);
		}
	}

	textField::textDrawer.draw(window);

	
}

/**
	Update the TextField. The TextField changes
	based on user interaction with the program,
	which are stored as variables and passed into 
	the function.

	If currently active, the TextField reads the 
	globally updated string of user input to display
	the text the user is typing.

	@param mousePos - position of mouse.
	@param mouseDownL - state of the left mouse button.
	@param numTyped - length of the string storing the 
	globaly typed characters.
	@param typed - string containing the globaly typed 
	characters.
	@param enterPressed - state of the 'enter' key.
	@param fps - The number of frames per second that
	the game is updating and drawing. Used to time
	animations.
*/
void textField::update(sf::Vector2i mousePos, bool mouseDownL, int* numTyped, char typed[200], bool enterPressed, int fps)
{
	textField::textDrawer.setSize(sf::Vector2f(textField::size.x, 0));
	textField::textDrawer.update(mousePos, mouseDownL);

	int newSize = textField::textDrawer.getSize().y + textField::underlineSize + textField::textBorder / 2;
	if (growTop == false)
		textField::size.y = textField::textDrawer.getSize().y + textField::underlineSize + textField::textBorder/2;
	else
	{
		textField::pos.y -= (newSize - textField::size.y);
		textField::size.y = textField::textDrawer.getSize().y + textField::underlineSize + textField::textBorder / 2;
	}
	if (textField::allignBottom)
	{
		textField::pos = sf::Vector2f(textField::pos.x, textField::allignY - textField::size.y);
	}
	textField::textDrawer.setPos(textField::pos);

	if (mouseDownL == true)
	{

		if (textField::pos.x < mousePos.x && textField::pos.y < mousePos.y && textField::pos.x + textField::size.x > mousePos.x && textField::pos.y + textField::size.y > mousePos.y)
		{
			if (textField::isActive == false)
			{
				for (int i = 0; i < strlen(textField::stringC); i++)
					typed[i] = textField::stringC[i];
				typed[strlen(textField::stringC)] = '\0';
				*numTyped = strlen(textField::stringC);
			}
			textField::isActive = true;
		}
		else
		{
			if (textField::isActive == true)
			{
				
			}
			textField::isActive = false;
		}
	}
	if (textField::isActive == true)
	{
		//copy
		int i = 0;
		while (typed[i] != '\0')
		{
			textField::stringC[i] = typed[i];
			i++;
		}
		textField::stringC[i] = '\0';
		//
		int wait = int((fps/3)*2);
		if (textField::blinkCoolDown == 0)
		{
			blink = (blink == false);
			textField::blinkCoolDown = wait;
		}
		if (textField::prevLength != strlen(textField::stringC))
		{
			blink = true;
			textField::blinkCoolDown = wait;
		}
		textField::prevLength = strlen(textField::stringC);
		textField::blinkCoolDown--;
		if (enterPressed == true && strlen(textField::stringC) != 0)
		{
			textField::forceEnter = false;
			strcpy_s(textField::savedC, textField::stringC);
			textField::savedC[strlen(textField::stringC)] = '\0';
			//cout << savedC << '\n';
			textField::stringC[0] = '\0';
			//cout << savedC << '\n';
			typed[0] = '\0';
			*numTyped = 0;
			//cout << savedC << '\n';
		}
	}
	else
		blink = false;

	if (textField::forceEnter == true && strlen(textField::stringC) == 0)
		textField::forceEnter = false;
	if (textField::forceEnter == true && strlen(textField::stringC) != 0)
	{
		textField::forceEnter = false;
		strcpy_s(textField::savedC, textField::stringC);
		textField::savedC[strlen(textField::stringC)] = '\0';
		//cout << savedC << '\n';
		textField::stringC[0] = '\0';
		//cout << savedC << '\n';
		typed[0] = '\0';
		*numTyped = 0;
		//cout << savedC << '\n';
	}
	if (textField::clear)
	{
		textField::stringC[0] = '\0';
		typed[0] = '\0';
		*numTyped = 0;
		textField::clear = false;
	}
	textField::textDrawer.setSelected(false);
}
void textField::resetSaved()
{
	textField::savedC[0] = '\0';
}
void textField::changeEmpty(char typed[200])
{
	int i = 0;
	while (typed[i] != '\0')
	{
		textField::emptyC[i] = typed[i];
		i++;
	}
	textField::emptyC[i] = '\0';
}

/**
	Accessors and mutators.
*/
char* textField::getSaved()
{
	return textField::savedC;
}
void textField::setPos(sf::Vector2f pos)
{
	textField::pos = pos;
	originalPos = textField::size.y;
}
void textField::setSize(sf::Vector2f size)
{
	textField::size = size;
}
void textField::setBorder(int border)
{
	textField::textBorder = border;
	textField::textDrawer.setBorder(textField::textBorder);
}
void textField::setFontSize(int fontSize)
{
	textField::fontSize = fontSize;
	textField::textDrawer.setFontSize(textField::fontSize);
}
sf::Vector2f textField::getPos()
{
	return textField::pos;
}
sf::Vector2f textField::getSize()
{
	return textField::size;
}
void textField::setAlpha(int alpha)
{
	textField::alpha = alpha;
}
void textField::setGrowTop(int growTop)
{
	textField::growTop = growTop;
}
void textField::setAllignBottom(int allignBottom)
{
	textField::allignBottom = allignBottom;
}
void textField::setAllignY(int allignY)
{
	textField::allignY = allignY;
	textField::allignBottom = true;
}
void textField::forcedEnter()
{
	textField::forceEnter = true;
}
char* textField::getEmpty()
{
	return textField::emptyC;
}
void textField::cleared()
{
	textField::clear = true;
}
