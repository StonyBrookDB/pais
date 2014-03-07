#include <iostream>
#include <fstream>
#include <stdio.h>
#include <vector>
#include <string.h>
#include <unistd.h>
#include <iomanip>
#include <cstdlib> 

#include "BoundaryFix.h"

#include <opencv/highgui.h>
#include <opencv/cv.h>

using namespace std;
using namespace cv;
#define NO_BOUNDARY_FIX 0
#define BOUNDARY_FIX 1

string TAB = "\t";
string SEMI_COLON = ";";
string SPACE = " ";
string COMMA = ",";

void processBoundaryObject(stringstream &instream);

/* The first parameter is the filename,
 * the second parameter is optional, if there is a 2nd argument,
 *     the program will perform the boundary fixing/correction.
 */
int main (int argc, char **argv){
	string line;
	stringstream tmpInStream;
	stringstream tmpOutStream;
	stringstream finalOutStream;
	int option = NO_BOUNDARY_FIX;

	char *buffer;
	Mat inputImg; /* InputImg */
	if (argc == 3) {
		option = BOUNDARY_FIX;
	}
	
	/* Read the input mask */
	inputImg = imread(argv[1], CV_8UC1);
	if (inputImg.data > 0) {
		Mat temp = Mat::zeros(inputImg.size() + Size(2,2), inputImg.type());
		copyMakeBorder(inputImg, temp, 1, 1, 1, 1, BORDER_CONSTANT, 0);

		std::vector<std::vector<Point> > contours;
		std::vector<Vec4i> hierarchy;

		/* Find the contour / boundary of nuclei */
		findContours(temp, contours, hierarchy, CV_RETR_CCOMP, CV_CHAIN_APPROX_NONE);
		
		int counter = 1;
		if (contours.size() > 0) {
			for (int idx = 0; idx >= 0; idx = hierarchy[idx][0]) {
				tmpInStream << idx << ": ";
				for (unsigned int ptc = 0; ptc < contours[idx].size(); ++ptc) {
					tmpInStream << SPACE << contours[idx][ptc].x << COMMA <<  contours[idx][ptc].y;
				}
				tmpInStream << endl;
			}
			++counter;
		}
		/* Clear the stream (reset eof and etc.) */
		tmpInStream.clear();
		
		if (option == NO_BOUNDARY_FIX) {
			/* Perform no boundary fixing */
			while (tmpInStream.good()){
				getline(tmpInStream, line, '\n');
				cout << line << endl;
			}
		} else {
			processBoundaryFixing(tmpInStream, tmpOutStream);
			tmpOutStream.clear();
			/* Output the result from boundary fix 2 to std output */
			processBoundaryFixing2(tmpOutStream, finalOutStream);
			//finalOutStream.clear();
			/* Handle the boundary objects and output them to std output  */
			processBoundaryObject(finalOutStream);
		}
	}
	return 0;
}

/* Performs an intersection test with the inner tile and output to standard output */
void processBoundaryObject(stringstream &instream) {
	string line;
	int count = 1;
	while (instream.good()){
		getline(instream, line, '\n');
		if (!line.empty()) {
			cout << count++ << SEMI_COLON << SPACE << line << endl;
		}
	}
}
