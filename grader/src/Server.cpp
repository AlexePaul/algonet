// Server.cpp
#include "../include/Server.h"
#include "../include/TestRunner.h"
#include <iostream>

Server::Server() { this->ConfigureCORS(); }

// might require some more work idk?
void Server::ConfigureCORS() {
  CROW_ROUTE(app, "/addToQueue")
  ([]() {
    crow::response res;
    res.add_header("Access-Control-Allow-Origin", "78.97.69.149");
    res.add_header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    res.add_header("Access-Control-Allow-Headers", "Content-Type");
    res.add_header("Content-Type", "application/json");
    res.write("Hello world!");
    res.end();
    return res;
  });
}

void Server::start() {
  // app.loglevel(crow::LogLevel::Debug);
  CROW_ROUTE(app, "/addToQueue")
      .methods("POST"_method)([this](const crow::request &req) {
        auto json_body = crow::json::load(req.body);
        if (!json_body) {
          return crow::response(400, "Invalid JSON");
        }

        int number = json_body["id"].i();
        TestRunner::addToQueue(number);

        return crow::response("Problem ID added to the queue");
      });

  app.port(9090).multithreaded().run();
}

void Server::stop() { app.stop(); }
