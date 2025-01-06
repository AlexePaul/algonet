#include "include/Server.h"
#include "include/TestRunner.h"
#include <iostream>
#include <thread>

int main() {
  std::cout << "Starting server..." << std::endl;
  Server server;
  TestRunner testRunner;
  std::thread serverThread(&Server::start, &server);
  std::thread testRunnerThread(&TestRunner::run, &testRunner);
  serverThread.join();
  return 0;
}
