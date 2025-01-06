#include "../include/TestRunner.h"
#include "../include/Database.h"
#include <iostream>
#include <thread>

TestRunner::TestRunner() { db = new Database(); }

std::queue<int> TestRunner::problemQueue;

void TestRunner::addToQueue(int problemID) {
  problemQueue.push(problemID);
  std::cout << "Added problem ID " << problemID << " to the queue" << std::endl;
  std::cout << "Queue size: " << problemQueue.size() << std::endl;
}

void TestRunner::run() {
  while (true) {
    while (!problemQueue.empty()) {
      int problemID = problemQueue.front();
      problemQueue.pop();

      std::string code;
      db->getCode(problemID, code);

      std::cout << "Code of problem ID: \n\n" << code << std::endl;
    }
    std::this_thread::sleep_for(std::chrono::seconds(1));
  }
}