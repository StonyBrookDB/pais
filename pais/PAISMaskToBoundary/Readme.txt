Binary file - To compile:
   install cmake
   install the boost library
   install opencv (you can use the attached opencvinstall.sh to install opencv automatically!)
   edit the src/CMakeLists.txt line 6-> adjust the path for the include boost library, such as ~/Downloads/boost_1_48_0 , where boost is installed/included.
  Run the following in src directory
   cmake .
   make

Usage - to run:
   ./processMask mask_file
  or (to perform boundary fixing/correction)
    ./processMask mask_file 1

(The image boundary is output to standard output, so redirecting output is recommended).

Example:
      ./processMask examplemask.jpg
   or ./processMask examplemask.jpg 1
