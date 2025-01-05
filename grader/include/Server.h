#ifndef SERVER_H
#define SERVER_H

#include "crow.h"

class Server
{
public:
    Server();
    void start();
    void stop();

private:
    crow::SimpleApp app;
};

#endif