// Server.cpp
#include "../include/Server.h"
#include <iostream>

Server::Server()
{
}

void Server::start()
{
    // app.loglevel(crow::LogLevel::Debug);
    CROW_ROUTE(app, "/receive_number").methods("POST"_method)([this](const crow::request &req)
                                                              {
        auto json_body = crow::json::load(req.body);
        if (!json_body) {
            return crow::response(400, "Invalid JSON");
        }

        int number = json_body["number"].i();
        number++;
        
        return crow::response(std::to_string(number)); });

    app.port(8080).multithreaded().run();
}

void Server::stop()
{
    app.stop();
}
