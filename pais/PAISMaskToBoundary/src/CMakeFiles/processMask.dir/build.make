# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 2.8

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src

# Include any dependencies generated for this target.
include CMakeFiles/processMask.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/processMask.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/processMask.dir/flags.make

CMakeFiles/processMask.dir/processMask.cpp.o: CMakeFiles/processMask.dir/flags.make
CMakeFiles/processMask.dir/processMask.cpp.o: processMask.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object CMakeFiles/processMask.dir/processMask.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/processMask.dir/processMask.cpp.o -c /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/processMask.cpp

CMakeFiles/processMask.dir/processMask.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/processMask.dir/processMask.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/processMask.cpp > CMakeFiles/processMask.dir/processMask.cpp.i

CMakeFiles/processMask.dir/processMask.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/processMask.dir/processMask.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/processMask.cpp -o CMakeFiles/processMask.dir/processMask.cpp.s

CMakeFiles/processMask.dir/processMask.cpp.o.requires:
.PHONY : CMakeFiles/processMask.dir/processMask.cpp.o.requires

CMakeFiles/processMask.dir/processMask.cpp.o.provides: CMakeFiles/processMask.dir/processMask.cpp.o.requires
	$(MAKE) -f CMakeFiles/processMask.dir/build.make CMakeFiles/processMask.dir/processMask.cpp.o.provides.build
.PHONY : CMakeFiles/processMask.dir/processMask.cpp.o.provides

CMakeFiles/processMask.dir/processMask.cpp.o.provides.build: CMakeFiles/processMask.dir/processMask.cpp.o

CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o: CMakeFiles/processMask.dir/flags.make
CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o: BoundaryFixFunction.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/CMakeFiles $(CMAKE_PROGRESS_2)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o -c /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/BoundaryFixFunction.cpp

CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/BoundaryFixFunction.cpp > CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.i

CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/BoundaryFixFunction.cpp -o CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.s

CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o.requires:
.PHONY : CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o.requires

CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o.provides: CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o.requires
	$(MAKE) -f CMakeFiles/processMask.dir/build.make CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o.provides.build
.PHONY : CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o.provides

CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o.provides.build: CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o

CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o: CMakeFiles/processMask.dir/flags.make
CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o: BoundaryFixFunction2.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/CMakeFiles $(CMAKE_PROGRESS_3)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o -c /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/BoundaryFixFunction2.cpp

CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/BoundaryFixFunction2.cpp > CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.i

CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/BoundaryFixFunction2.cpp -o CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.s

CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o.requires:
.PHONY : CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o.requires

CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o.provides: CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o.requires
	$(MAKE) -f CMakeFiles/processMask.dir/build.make CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o.provides.build
.PHONY : CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o.provides

CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o.provides.build: CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o

CMakeFiles/processMask.dir/clipper.cpp.o: CMakeFiles/processMask.dir/flags.make
CMakeFiles/processMask.dir/clipper.cpp.o: clipper.cpp
	$(CMAKE_COMMAND) -E cmake_progress_report /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/CMakeFiles $(CMAKE_PROGRESS_4)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building CXX object CMakeFiles/processMask.dir/clipper.cpp.o"
	/usr/bin/c++   $(CXX_DEFINES) $(CXX_FLAGS) -o CMakeFiles/processMask.dir/clipper.cpp.o -c /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/clipper.cpp

CMakeFiles/processMask.dir/clipper.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/processMask.dir/clipper.cpp.i"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -E /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/clipper.cpp > CMakeFiles/processMask.dir/clipper.cpp.i

CMakeFiles/processMask.dir/clipper.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/processMask.dir/clipper.cpp.s"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_FLAGS) -S /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/clipper.cpp -o CMakeFiles/processMask.dir/clipper.cpp.s

CMakeFiles/processMask.dir/clipper.cpp.o.requires:
.PHONY : CMakeFiles/processMask.dir/clipper.cpp.o.requires

CMakeFiles/processMask.dir/clipper.cpp.o.provides: CMakeFiles/processMask.dir/clipper.cpp.o.requires
	$(MAKE) -f CMakeFiles/processMask.dir/build.make CMakeFiles/processMask.dir/clipper.cpp.o.provides.build
.PHONY : CMakeFiles/processMask.dir/clipper.cpp.o.provides

CMakeFiles/processMask.dir/clipper.cpp.o.provides.build: CMakeFiles/processMask.dir/clipper.cpp.o

# Object files for target processMask
processMask_OBJECTS = \
"CMakeFiles/processMask.dir/processMask.cpp.o" \
"CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o" \
"CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o" \
"CMakeFiles/processMask.dir/clipper.cpp.o"

# External object files for target processMask
processMask_EXTERNAL_OBJECTS =

processMask: CMakeFiles/processMask.dir/processMask.cpp.o
processMask: CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o
processMask: CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o
processMask: CMakeFiles/processMask.dir/clipper.cpp.o
processMask: /usr/local/lib/libopencv_calib3d.so
processMask: /usr/local/lib/libopencv_contrib.so
processMask: /usr/local/lib/libopencv_core.so
processMask: /usr/local/lib/libopencv_features2d.so
processMask: /usr/local/lib/libopencv_flann.so
processMask: /usr/local/lib/libopencv_gpu.so
processMask: /usr/local/lib/libopencv_highgui.so
processMask: /usr/local/lib/libopencv_imgproc.so
processMask: /usr/local/lib/libopencv_legacy.so
processMask: /usr/local/lib/libopencv_ml.so
processMask: /usr/local/lib/libopencv_nonfree.so
processMask: /usr/local/lib/libopencv_objdetect.so
processMask: /usr/local/lib/libopencv_photo.so
processMask: /usr/local/lib/libopencv_stitching.so
processMask: /usr/local/lib/libopencv_superres.so
processMask: /usr/local/lib/libopencv_ts.so
processMask: /usr/local/lib/libopencv_video.so
processMask: /usr/local/lib/libopencv_videostab.so
processMask: CMakeFiles/processMask.dir/build.make
processMask: CMakeFiles/processMask.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking CXX executable processMask"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/processMask.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/processMask.dir/build: processMask
.PHONY : CMakeFiles/processMask.dir/build

CMakeFiles/processMask.dir/requires: CMakeFiles/processMask.dir/processMask.cpp.o.requires
CMakeFiles/processMask.dir/requires: CMakeFiles/processMask.dir/BoundaryFixFunction.cpp.o.requires
CMakeFiles/processMask.dir/requires: CMakeFiles/processMask.dir/BoundaryFixFunction2.cpp.o.requires
CMakeFiles/processMask.dir/requires: CMakeFiles/processMask.dir/clipper.cpp.o.requires
.PHONY : CMakeFiles/processMask.dir/requires

CMakeFiles/processMask.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/processMask.dir/cmake_clean.cmake
.PHONY : CMakeFiles/processMask.dir/clean

CMakeFiles/processMask.dir/depend:
	cd /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src /home/hoang/Projects/pais/pais/PAISMaskToBoundary/src/CMakeFiles/processMask.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/processMask.dir/depend

