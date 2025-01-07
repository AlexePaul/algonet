#ifndef TESTRUNNER_H
#define TESTRUNNER_H

#include "Database.h"
#include <queue>
#include <thread>

class TestRunner {

public:
  TestRunner();
  static void addToQueue(int problemID);
  void run();

private:
  static std::queue<int> solutionQueue;
  static bool runTest(const std::string &, const std::string &);
  Database *db;
};

#endif