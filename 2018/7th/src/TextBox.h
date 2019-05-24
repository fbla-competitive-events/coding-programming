#pragma once

/**
	Game Element: TextBox
	Must be initialized, updated, and drawn.

	Definition of a TextBox. A TextBox
	is used to store and display text.
*/
class TextBox
{
private:

	char stringC[200];
	sf::Color colorBack;
	sf::Color colorOutline;
	sf::Color colorText;
	sf::Vector2f pos;
	sf::Vector2f size;
	sf::Text stringT;
	sf::RectangleShape back;
	sf::Font mainFont;
	int fontSize;
	int border;
	int outlineSize;
	bool flexibleLength;
	bool smartFill;
	bool center;
	bool active;
	bool selected;
	int alpha;
	bool noBack;

public:

	TextBox() {};
	TextBox(sf::Vector2f pos, sf::Vector2f size, char text[200]);
	void setColor(sf::Color colorBack, sf::Color colorText);
	void setColor(sf::Color colorBack, sf::Color colorText, sf::Color colorOutline);
	void setFont(sf::Font mainFont);
	void changeText(char text[200]);
	char* getText();
	void draw(sf::RenderWindow* window);
	void update(sf::Vector2i mousePos, bool mouseDownL);

	void setPos(sf::Vector2f pos);
	void setSize(sf::Vector2f size);
	void setBorder(int border);
	void setOutlineSize(int outlineSize);
	void setFontSize(int fontSize);
	void setLength(int length);
	void setSmartFill(bool smartFill);
	void setCenter(bool center);
	void setSelected(bool selected);

	int getFontSize();
	int getLength();
	sf::Vector2f getPos();
	sf::Vector2f getSize();
	bool getSelected();
	void setAlpha(int alpha);
	void setNoBack(int noBack);
	bool getActive();
	void setActive(bool active);
};

