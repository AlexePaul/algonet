# Set the minimum required version of CMake
cmake_minimum_required(VERSION 3.10)

# Define the project name
project(AlgoNetGrader)

# Set C++ standard
set(CMAKE_CXX_STANDARD 11)

# Specify the include directories
include_directories(include /usr/include/crow)  # Add the Crow library include path as well

# Add all source files (from the src directory)
add_executable( grader
    main.cpp
    src/Server.cpp
)

# Link any libraries you need (e.g., pthread for multithreading)
target_link_libraries(grader pthread)
