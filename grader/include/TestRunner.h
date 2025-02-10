#ifndef TESTRUNNER_H
#define TESTRUNNER_H

#include "Database.h"
#include <mutex>
#include <queue>
#include <thread>

class TestRunner {

public:
  TestRunner();
  static void addToQueue(int solutionId);
  void main();

private:
  static std::queue<int> solutionQueue;
  static std::mutex queueMutex;
  Database *db;
  static bool runTest(const std::string &, const std::string &);
  void runTests(int solutionId);
};

#endif