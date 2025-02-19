#ifndef DATABASE_H
#define DATABASE_H

#include <pqxx/pqxx>
#include <string>

class Database {
public:
  Database(std::string = "dbname=algonet user=admin password=admin1234 "
                         "host=78.97.69.149 port=6543");
  void connect();
  void disconnect();
  void getCode(int, std::string &);
  void getTests(int, std::vector<std::pair<std::string, std::string>> &);

private:
  const std::string connStr;
  pqxx::connection *conn;
};

#endif