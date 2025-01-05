#include "include/Server.h"
#include <iostream>

int main()
{
    std::cout << "Starting server..." << std::endl;
    Server server;
    server.start(); // Start the server
    return 0;
}
