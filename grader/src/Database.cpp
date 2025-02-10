#include "../include/Database.h"
#include <iostream>

Database::Database(std::string connStr) : connStr(connStr) {
  // Connect to the database
  connect();
}

void Database::connect() { this->conn = new pqxx::connection(connStr); }

void Database::disconnect() { (*this->conn).disconnect(); }

void Database::getCode(int solutionId, std::string &code) {
  pqxx::work W(*conn);
  pqxx::result R = W.exec("SELECT code FROM solutions WHERE id = " +
                          std::to_string(solutionId));
  if (R.empty()) {
    throw std::runtime_error("No code found for solutionId: " +
                             std::to_string(solutionId));
  }
  code = R[0][0].as<std::string>();
  W.commit();
}

void Database::getTests(
    int solutionId, std::vector<std::pair<std::string, std::string>> &tests) {
  pqxx::work W(*conn);
  pqxx::result R =
      W.exec("SELECT input, output FROM tests WHERE problem_id = (SELECT "
             "problem_id FROM solutions where id = " +
             std::to_string(solutionId) + ")");
  for (pqxx::result::const_iterator c = R.begin(); c != R.end(); ++c) {
    tests.push_back(
        std::make_pair(c[0].as<std::string>(), c[1].as<std::string>()));
  }
  W.commit();
}