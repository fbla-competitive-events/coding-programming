#include <SFML/Graphics.hpp>
#include "TextBox.h"
#include <iostream>
using namespace std;

/**
	Constructor.

	@param pos - pos of TextBox.
	@param size - size of TextBox.
	@param text - string to display.
*/
TextBox::TextBox(sf::Vector2f pos, sf::Vector2f size, char text[200])
{
	TextBox::pos = pos;
	TextBox::size = size;
	TextBox::fontSize = 30;
	TextBox::flexibleLength = true;
	TextBox::smartFill = true;
	TextBox::border = 10;
	TextBox::center = false;
	TextBox::outlineSize = 0;
	int i = 0;
	while (text[i] != '\0')
	{
		
		TextBox::stringC[i] = text[i];
		i++;
	}
	TextBox::stringC[i] = '\0';
	TextBox::alpha = 255;
	TextBox::noBack = false;
	TextBox::selected = false;
}

/**
	Initialize colors.

	@param colorBack - color of back box.
	@param colorText - color of text.
*/
void TextBox::setColor(sf::Color colorBack, sf::Color colorText)
{
	TextBox::colorBack = colorBack;
	TextBox::colorText = colorText;
	TextBox::colorOutline = colorBack;
}

/**
	Overloaded method: Initialize colors.

	@param colorBack - color of back box.
	@param colorText - color of text.
	@param colorOutline - color of the outline;
*/
void TextBox::setColor(sf::Color colorBack, sf::Color colorText, sf::Color colorOutline)
{
	TextBox::colorBack = colorBack;
	TextBox::colorText = colorText;
	TextBox::colorOutline = colorOutline;
}

/**
	Initialize font.

	@param font - font.
*/
void TextBox::setFont(sf::Font mainFont)
{
	TextBox::mainFont = mainFont;
	TextBox::stringT.setFont(TextBox::mainFont);
}

/**
	Update the TextBox. The TextBox changes
	based on user interaction with the program,
	which are stored as variables and passed into 
	the function.

	Includes functionality for centering the text, 
	auto-calculating the size of the box based on
	the length of the text, and parsing the text 
	into multiple lines.

	@param mousePos - position of mouse.
	@param mouseDownL - state of the left mouse button.
*/
void TextBox::update(sf::Vector2i mousePos, bool mouseDownL)
{
	//Do the parsing
	char temp[220] = "\0";
	int sizeAllowed = TextBox::size.x - 2 * TextBox::border;
	int numCharacters = strlen(TextBox::stringC);
	int tempFontSize = TextBox::fontSize;
	int tempLength = TextBox::size.y - 2 * TextBox::border;

	if (TextBox::flexibleLength == false && TextBox::smartFill == true)
	{
		tempFontSize = sqrt((tempLength*sizeAllowed) / (numCharacters));
		tempFontSize *= 1;
	}

	TextBox::stringT.setString(" ");
	TextBox::stringT.setCharacterSize(tempFontSize);
	TextBox::stringT.setFillColor(TextBox::colorText);
	TextBox::stringT.setPosition(TextBox::pos.x + TextBox::border, TextBox::pos.y + TextBox::border);

	char word[200] = "\0";
	char currentLine[200] = "\0";
	char otherTemp[200] = "\0";
	int wordLen = 0;
	int largestXPos = 0;
	for (int i = 0; i < numCharacters; i++)
	{
		if (TextBox::stringC[i] != ' ' && TextBox::stringC[i] != '\t')
		{
			word[wordLen] = TextBox::stringC[i];
			wordLen++;
			word[wordLen] = '\0';
		}
		else
		{
			char whiteSpace[3];
			whiteSpace[0] = TextBox::stringC[i];
			whiteSpace[1] = '\0';

			strcpy_s(otherTemp, currentLine);
			strcat_s(currentLine, word);

			TextBox::stringT.setString(currentLine);
			if (TextBox::stringT.findCharacterPos(strlen(currentLine)).x > largestXPos)
				largestXPos = TextBox::stringT.findCharacterPos(strlen(currentLine)).x;
			if (TextBox::stringT.findCharacterPos(strlen(currentLine)).x > TextBox::pos.x + TextBox::border + sizeAllowed)
			{

				strcat_s(temp, otherTemp);
				if (strlen(otherTemp)>0)
					strcat_s(temp, "\n");
				currentLine[0] = '\0';
				strcat_s(currentLine, word);
				strcat_s(currentLine, whiteSpace);
			}
			else
				strcat_s(currentLine, whiteSpace);
			word[0] = '\0';
			wordLen = 0;
		}
	}
	strcpy_s(otherTemp, currentLine);
	strcat_s(currentLine, word);

	TextBox::stringT.setString(currentLine);
	if (TextBox::stringT.findCharacterPos(strlen(currentLine)).x > TextBox::pos.x + TextBox::border + sizeAllowed)
	{

		strcat_s(temp, otherTemp);
		strcat_s(temp, "\n");
		currentLine[0] = '\0';
		strcat_s(currentLine, word);
		strcat_s(currentLine, " ");
	}
	else
		strcat_s(currentLine, " ");
	word[0] = '\0';
	wordLen = 0;

	strcat_s(temp, currentLine);
	currentLine[0] = '\0';

	TextBox::stringT.setString(temp);
	if (TextBox::flexibleLength == true && TextBox::smartFill == true)
	{
		tempLength = TextBox::stringT.findCharacterPos(strlen(temp)).y - TextBox::pos.y + tempFontSize + TextBox::border;
	}

	TextBox::stringT.setString(temp);
	if(smartFill == true)
		TextBox::size.y = tempLength;
	if (TextBox::center == true)
	{
		if (TextBox::stringT.findCharacterPos(strlen(temp)).x > largestXPos)
			largestXPos = TextBox::stringT.findCharacterPos(strlen(temp)).x;
		int openXSpace = TextBox::size.x - (largestXPos - stringT.getPosition().x);
		int openYSpace = TextBox::size.y - (TextBox::stringT.findCharacterPos(strlen(temp)).y - stringT.getPosition().y) - TextBox::fontSize;
		TextBox::stringT.setPosition(TextBox::pos.x + openXSpace/2, TextBox::pos.y + openYSpace/2);
	}
	if (TextBox::pos.x < mousePos.x && TextBox::pos.y < mousePos.y && TextBox::pos.x + TextBox::size.x > mousePos.x && TextBox::pos.y + TextBox::size.y > mousePos.y)
	{
		TextBox::active = true;
	}
	else
		TextBox::active = false;
	if (mouseDownL)
		if (TextBox::active)
			TextBox::selected = true;
		else
			TextBox::selected = false;

}

/**
	Draw the TextBox.

	@param window - window to render onto.
*/
void TextBox::draw(sf::RenderWindow* window)
{
	if (!noBack)
	{
		TextBox::colorBack.a = TextBox::alpha;
		TextBox::back.setSize(sf::Vector2f(TextBox::size.x - 2*outlineSize, TextBox::size.y - 2*outlineSize));
		TextBox::back.setOutlineColor(TextBox::colorOutline);
		TextBox::back.setOutlineThickness(TextBox::outlineSize);
		TextBox::back.setFillColor(TextBox::colorBack);
		TextBox::back.setPosition(sf::Vector2f(TextBox::pos.x + outlineSize, TextBox::pos.y + outlineSize));
		window->draw(TextBox::back);
	}
	if (TextBox::active == true)
	{
		TextBox::back.setSize(sf::Vector2f(TextBox::size.x - 2 * outlineSize, TextBox::size.y - 2 * outlineSize));
		TextBox::back.setOutlineColor(sf::Color(255,255,255, 15));
		TextBox::back.setOutlineThickness(TextBox::outlineSize);
		TextBox::back.setFillColor(sf::Color(255, 255, 255, 15));
		TextBox::back.setPosition(sf::Vector2f(TextBox::pos.x + outlineSize, TextBox::pos.y + outlineSize));
		window->draw(TextBox::back);
	}
	if (TextBox::selected == true)
	{
		TextBox::back.setSize(sf::Vector2f(TextBox::size.x - 2 * outlineSize, TextBox::size.y - 2 * outlineSize));
		TextBox::back.setOutlineColor(sf::Color(255, 255, 255, 0));
		TextBox::back.setOutlineThickness(TextBox::outlineSize);
		TextBox::back.setFillColor(sf::Color(255, 255, 255, 90));
		TextBox::back.setPosition(sf::Vector2f(TextBox::pos.x + outlineSize, TextBox::pos.y + outlineSize));
		window->draw(TextBox::back);
	}
	TextBox::size = sf::Vector2f(TextBox::size);

	TextBox::colorText.a = TextBox::alpha;
	TextBox::stringT.setFillColor(TextBox::colorText);
	window->draw(TextBox::stringT);
}

/**
	Accessors and mutators.
*/
void TextBox::changeText(char text[200])
{
	int i = 0;
	while (text[i] != '\0')
	{
		TextBox::stringC[i] = text[i];
		i++;
	}
	TextBox::stringC[i] = '\0';
}
char* TextBox::getText()
{
	return TextBox::stringC;
}
void TextBox::setPos(sf::Vector2f pos)
{
	TextBox::pos = pos;
}
void TextBox::setSize(sf::Vector2f size)
{
	TextBox::size = size;
}
void TextBox::setBorder(int border)
{
	TextBox::border = border;
}
void TextBox::setFontSize(int fontSize)
{
	TextBox::flexibleLength = true;
	TextBox::fontSize = fontSize;
}
void TextBox::setLength(int length)
{
	TextBox::flexibleLength = false;
	TextBox::size.y = length;
}
void TextBox::setSmartFill(bool smartFill)
{
	TextBox::smartFill = smartFill;
}
int TextBox::getFontSize()
{
	return TextBox::fontSize;
}
int TextBox::getLength()
{
	return TextBox::size.y;
}
sf::Vector2f TextBox::getPos()
{
	return TextBox::pos;
}
sf::Vector2f TextBox::getSize()
{
	return TextBox::size;
}
void TextBox::setAlpha(int alpha)
{
	TextBox::alpha = alpha;
}
void TextBox::setNoBack(int noBack)
{
	TextBox::noBack = noBack;
}
void TextBox::setCenter(bool center)
{
	TextBox::center = center;
}
void TextBox::setOutlineSize(int outlineSize)
{
	TextBox::outlineSize = outlineSize;
}
void TextBox::setSelected(bool selected)
{
	TextBox::selected = selected;
}
bool TextBox::getSelected()
{
	return TextBox::selected;
}
void TextBox::setActive(bool active)
{
	TextBox::active = active;
}
bool TextBox::getActive()
{
	return TextBox::active;
}
