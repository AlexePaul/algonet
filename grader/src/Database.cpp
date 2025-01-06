#include "../include/Database.h"

Database::Database(std::string connStr) : connStr(connStr) {
  // Connect to the database
  connect();
}

void Database::connect() { this->conn = new pqxx::connection(connStr); }

void Database::disconnect() { (*this->conn).disconnect(); }

void Database::getCode(int problemId, std::string &code) {
  pqxx::work W(*conn);
  pqxx::result R = W.exec("SELECT code FROM solutions WHERE id = " +
                          std::to_string(problemId));
  code = R[0][0].as<std::string>();
  W.commit();
}