#include "../include/TestRunner.h"
#include "../include/Database.h"
#include <fstream>
#include <future>
#include <iostream>
#include <mutex>
#include <queue>
#include <thread>

TestRunner::TestRunner() { db = new Database(); }

std::queue<int> TestRunner::solutionQueue;
std::mutex TestRunner::queueMutex;

void TestRunner::addToQueue(int solutionId) { solutionQueue.push(solutionId); }

std::string trimOutput(const std::string &str) {
  std::string trimmed = str;
  trimmed.erase(trimmed.find_last_not_of(" \n\r\t") +
                1); // Remove trailing spaces, tabs, and newlines
  return trimmed;
}

bool TestRunner::runTest(const std::string &input,
                         const std::string &expectedOutput) {
  // Prepare the command to run the solution and pass input through a pipe
  std::string command = "echo \"" + input + "\" | ./solution";
  std::string output;

  // Open a pipe to run the command
  FILE *fp = popen(command.c_str(), "r");
  if (fp == nullptr) {
    std::cerr << "Failed to run command\n";
    return false;
  }

  // Read the output from the program
  char buf[1024];
  while (fgets(buf, sizeof(buf), fp) != NULL) {
    output += buf;
  }
  pclose(fp);

  // Trim output to remove extra spaces, newlines, etc.
  output = trimOutput(output);

  // Compare the output with the expected output
  /*
  if (output != expectedOutput) {
    std::cout << " Expected: " << expectedOutput << "\nGot: " << output
              << "\nOn input: " << input << "\n";
  }
  */

  return output == expectedOutput;
}

void TestRunner::runTests(int solutionId) {

  // Getting the code from the database
  std::string code;
  try {
    db->getCode(solutionId, code);
  } catch (const std::exception &e) {
    std::cerr << e.what() << '\n';
    return;
  }

  // Writing the code to a file
  std::ofstream cppFile("solution.cpp");
  cppFile << code;
  cppFile.close();

  // Compiling the code
  system("g++ -std=c++17 -o solution solution.cpp");

  // Fetch tests for this solution ID
  std::vector<std::pair<std::string, std::string>> tests;
  db->getTests(solutionId, tests);

  // Run tests in parallel
  std::vector<std::future<bool>> testFutures;
  for (const auto &test : tests) {
    testFutures.push_back(std::async(std::launch::async, TestRunner::runTest,
                                     test.first, test.second));
  }

  // Collect results
  int passedTests = 0;
  for (auto &future : testFutures) {
    if (future.get()) { // Retrieve the result of each test
      passedTests++;
    }
  }

  // Display test summary
  // std::cout << "Solution ID " << solutionId << ": Passed " << passedTests <<
  // "/"
  //<< tests.size() << " tests.\n";
}

void TestRunner::main() {
  while (true) {
    std::unique_lock<std::mutex> lock{queueMutex};
    bool empty = solutionQueue.empty();
    lock.unlock();

    while (!empty) {

      lock.lock();
      int solutionId = solutionQueue.front();
      solutionQueue.pop();
      lock.unlock();

      runTests(solutionId);

      lock.lock();
      empty = solutionQueue.empty();
      lock.unlock();
    }
    std::this_thread::sleep_for(std::chrono::seconds(1));
  }
}