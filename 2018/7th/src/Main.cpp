#include <SFML/Graphics.hpp>
#include "Game.h"
//#pragma comment(lib, "sfml-network.lib")
#include <iostream>
//#include <SFML/Network.hpp>
using namespace std;

/**
	Controls operation of the program. The use of the 
	'Game' class allows for for the possibility for
	mutiple instances of the program to be run simultaneously 
	on a server.
*/
int main()
{	
	Game myGame;
	myGame.init();
	myGame.run();

	return 0;
}
