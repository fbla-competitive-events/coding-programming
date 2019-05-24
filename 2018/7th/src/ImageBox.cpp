#include <SFML/Graphics.hpp>
#include "ImageBox.h"
#include <iostream>
using namespace std;

/**
	Constructor.

	@param pos - pos of ImageBox.
	@param size - size of ImageBox.
	@param mainTexture - image to be drawn.
*/
ImageBox::ImageBox(sf::Vector2f pos, sf::Vector2f size, sf::Texture* mainTexture)
{
	ImageBox::pos = pos;
	ImageBox::size = size;
	ImageBox:: border = 0;

	ImageBox::mainTexture = mainTexture;
	ImageBox::alpha = 255;
}

/**
	Update the ImageBox. The ImageBox changes
	based on user interaction with the program,
	which are stored as variables and passed into 
	the function.

	@param mousePos - position of mouse.
*/
void ImageBox::update(sf::Vector2i mousePos)
{
	if (ImageBox::pos.x < mousePos.x && ImageBox::pos.y < mousePos.y && ImageBox::pos.x + ImageBox::size.x > mousePos.x && ImageBox::pos.y + ImageBox::size.y > mousePos.y)
	{
		ImageBox::active = true;
	}
	else
		ImageBox::active = false;
}

/**
	Provides functionality for optimally scaling 
	an image in a give constraint of space while
	still keeping the same aspect ratio.

	@param dim - current dimentions of image.
	@param space - space available to display.
	image.
	@return - new dementions.
*/
sf::Vector2f getScaled(sf::Vector2u dim, sf::Vector2i space)
{
	float smallerRatio;
	smallerRatio = float(space.x) / dim.x;
	if (float(space.y)/ dim.y < smallerRatio)
		smallerRatio = float(space.y) / dim.y;
	return sf::Vector2f(smallerRatio, smallerRatio);
}

/**
	Draw the TextBox.

	@param window - window to render onto.
*/
void ImageBox::draw(sf::RenderWindow* window)
{
	ImageBox::colorBack.a = ImageBox::alpha;
	ImageBox::back.setSize(ImageBox::size);
	ImageBox::back.setOutlineColor(ImageBox::colorBack);
	ImageBox::back.setOutlineThickness(0);
	ImageBox::back.setFillColor(ImageBox::colorBack);
	ImageBox::back.setPosition(ImageBox::pos);
	window->draw(ImageBox::back);

	// Create a sprite
	ImageBox::colorImage.a = ImageBox::alpha;
	sf::Sprite sprite;
	sprite.setTexture(*(ImageBox::mainTexture));
	//sprite.setTextureRect(sf::IntRect(10, 10, 1000, 1000));
	sf::Vector2f scaleFactor = getScaled(ImageBox::mainTexture->getSize(), sf::Vector2i(ImageBox::size.x - ImageBox::border * 2, ImageBox::size.y - ImageBox::border * 2));
	sf::Vector2f scale(scaleFactor);
	sprite.setScale(scale);
	sprite.setColor(ImageBox::colorImage);
	sf::Vector2u dim = ImageBox::mainTexture->getSize();
	sf::Vector2f newSize(dim.x*scaleFactor.x, dim.y*scaleFactor.y);
	sprite.setPosition((ImageBox::size.x - (newSize.x))/2 + ImageBox::pos.x, (ImageBox::size.y - (newSize.y)) / 2 + ImageBox::pos.y);
	//Selected bubble
	if (ImageBox::active == true)
	{
		ImageBox::back.setSize(ImageBox::size);
		ImageBox::back.setOutlineColor(sf::Color(255, 255, 255, 15));
		ImageBox::back.setOutlineThickness(0);
		ImageBox::back.setFillColor(sf::Color(255, 255, 255, 15));
		ImageBox::back.setPosition(ImageBox::pos);
		window->draw(ImageBox::back);
	}
	// Draw it
	window->draw(sprite);
}

/**
	Initialize colors.

	@param colorBack - color of back box.
	@param colorImage - color of image.
*/
void ImageBox::setColor(sf::Color colorBack, sf::Color colorImage)
{
	ImageBox::colorBack = colorBack;
	ImageBox::colorImage = colorImage;
}

/**
	Accessors and mutators.
*/
void ImageBox::setPos(sf::Vector2f pos)
{
	ImageBox::pos = pos;
}
void ImageBox::setSize(sf::Vector2f size)
{
	ImageBox::size = size;
}
void ImageBox::setBorder(int border)
{
	ImageBox::border = border;
}
sf::Vector2f ImageBox::getPos()
{
	return ImageBox::pos;
}
sf::Vector2f ImageBox::getSize()
{
	return ImageBox::size;
}
void ImageBox::setAlpha(int alpha)
{
	ImageBox::alpha = alpha;
}
void ImageBox::setActive(bool active)
{
	ImageBox::active = active;
}
bool ImageBox::getActive()
{
	return ImageBox::active;
}
void ImageBox::setTexture(sf::Texture* mainTexture)
{
	ImageBox::mainTexture = mainTexture;
}

