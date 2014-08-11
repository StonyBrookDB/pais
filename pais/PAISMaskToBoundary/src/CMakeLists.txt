cmake_minimum_required(VERSION 2.8)
project( testcv )
find_package( OpenCV REQUIRED )
find_package( Boost REQUIRED )

include_directories(common ~/Downloads/boost_1_48_0)
#include_directories(${Boost_INCLUDE_DIR})

add_executable( processMask
	processMask.cpp
	BoundaryFixFunction.cpp
	BoundaryFixFunction2.cpp
	clipper.cpp
)

target_link_libraries( processMask ${OpenCV_LIBS} )
