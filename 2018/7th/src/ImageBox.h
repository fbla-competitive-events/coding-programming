#pragma once

/**
	Game Element: ImageBox
	Must be initialized, updated, and drawn.

	Definition of a ImageBox. A ImageBox
	is used to manipulate and display images.
*/
class ImageBox
{
private:

	sf::Vector2f pos;
	sf::Vector2f size;
	int border;
	sf::Color colorBack;
	sf::Color colorImage;
	sf::RectangleShape back;
	sf::Texture* mainTexture;
	int alpha;
	bool active;

public:

	ImageBox() {};
	ImageBox(sf::Vector2f pos, sf::Vector2f size, sf::Texture* mainTexture);
	void update(sf::Vector2i mousePos);
	void draw(sf::RenderWindow* window);

	void setColor(sf::Color colorBack, sf::Color colorImage);
	void setPos(sf::Vector2f pos);
	void setSize(sf::Vector2f size);
	void setBorder(int border);
	sf::Vector2f getPos();
	sf::Vector2f getSize();
	void setAlpha(int alpha);
	bool getActive();
	void setActive(bool active);
	void setTexture(sf::Texture* mainTexture);
};