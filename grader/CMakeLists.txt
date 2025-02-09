# Set the minimum required version of CMake
cmake_minimum_required(VERSION 3.10)

# Define the project name
project(AlgoNetGrader)

# Set C++ standard (use C++17 as required by libpqxx)
set(CMAKE_CXX_STANDARD 17)

# Find and include libpqxx using pkg-config
find_package(PkgConfig REQUIRED)
pkg_check_modules(PQXX REQUIRED libpqxx)

# Specify the include directories
include_directories(include /usr/include/crow ${PQXX_INCLUDE_DIRS})

# Add all source files (from the src directory)
add_executable(grader
    main.cpp
    src/Server.cpp
    src/TestRunner.cpp
    src/Database.cpp
)

# Link libraries using pkg-config
target_link_libraries(grader PRIVATE pthread ${PQXX_LIBRARIES})
