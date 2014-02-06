#include "edu_emory_cci_imagedb_FileDataCollector_OpenSlideTools.h"
#include <jni.h>
#include <openslide.h>
#include <sys/types.h> 
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h> 
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <inttypes.h>
#include <stdint.h>

#include <dirent.h>
#include <string.h>
#include <stdbool.h>
#include <sys/time.h>
#include <ctype.h>
 
JNIEXPORT void JNICALL Java_edu_emory_cci_imagedb_FileDataCollector_OpenSlideTools_getThumbnailWSI(JNIEnv *env, jclass dummy, jstring filename, jstring tbname, jint length) {

	openslide_t *osr;
	int32_t layer_count;
	int64_t width, height, i, size, temp;
	uint32_t *dest, val; 
	char cmd[60], ppmheader[50];
	FILE *f1;
	const char *filename1 = (*env)->GetStringUTFChars(env, filename, NULL);
    const char *filename2 = (*env)->GetStringUTFChars(env, tbname, NULL);


	if (!openslide_can_open(filename1)) 
	{	
		printf("can't open image %s\n", filename);
		exit(1);
	}

	if ((osr = openslide_open (filename1))==NULL)
	{
		printf("open the whole slide image %s error\n", filename);
		exit(1);
	}
	layer_count = openslide_get_layer_count(osr);
	openslide_get_layer_dimensions(osr, layer_count-1, &width, &height);
	size=width*height*4;
	dest = (uint32_t *) malloc(size);
	
	/* dest should be holding the ARGB data of the smallest layer */
	openslide_read_region(osr, dest, 0, 0, layer_count-1, width, height);
	openslide_close(osr);
	if((f1 = fopen("testimg.ppm", "wb+"))==NULL) {
		free(dest);
		perror("open file failure");
		exit(1);
	}
	
	/* write ppm file header */
	fprintf(f1, "P6\n%" PRId64 " %" PRId64 "\n255\n", width, height);
			
	/* write binary RGB info */
	for (i = 0; i< width * height; i++) {
		val = dest[i];
		putc((val >> 16) & 0xFF, f1);
		putc((val >> 8) & 0xFF, f1);
		putc((val >> 0) & 0xFF, f1);
	}
	fclose(f1);
	free(dest);
	
	temp = width>height?width:height;
	temp /= length;
	width /= temp;
	height /= temp;
	printf("The generating thumbnail for %s is %ldx%ld pixels\n", filename1, width, height);
	sprintf(cmd, "convert testimg.ppm -resize %ldx%ld %s", width, height,filename2); 
	system(cmd);
	remove("testimg.ppm");
	(*env)->ReleaseStringUTFChars(env, filename, filename1);
	(*env)->ReleaseStringUTFChars(env, tbname, filename2);
	return;
}

JNIEXPORT void JNICALL Java_edu_emory_cci_imagedb_FileDataCollector_OpenSlideTools_getThumbnailTiled(JNIEnv *env, jclass dummy, jstring filename, jstring tbname, jint length, jint x, jint y,jint w, jint h) {

	openslide_t *osr;
	int32_t layer_count;
	int64_t width, height, i, size, temp,bwidth,bheight;
	uint32_t *dest, val; 
	char cmd[60], ppmheader[50],tmpfile[250];
	FILE *f1;
	const char *filename1 = (*env)->GetStringUTFChars(env, filename, NULL);
        const char *filename2 = (*env)->GetStringUTFChars(env, tbname, NULL);
        sprintf(tmpfile,"%s.ppm",filename2);
        printf("I will get thumbnail for this tiled image!\n");
	if (!openslide_can_open(filename1)) 
	{	
		printf("can't open image %s\n", filename1);
		exit(1);
	}

	if ((osr = openslide_open (filename1))==NULL)
	{
		printf("open the whole slide image %s error\n", filename1);
		exit(1);
	}
        layer_count = openslide_get_layer_count(osr);
        openslide_get_layer_dimensions(osr, 0, &bwidth, &bheight);
        int validlayer = 0;
        int temph,tempw;
        for(i = layer_count-1;i>=0;i--)
        {
        tempw=w;
        temph=h;
        validlayer = i;
	openslide_get_layer_dimensions(osr, validlayer, &width, &height);
/*
        if(x+w>bwidth)
		w=bwidth-x;
	if(y+h>bheight)
		h=bheight-y;
*/
        int rate = bwidth/width;

        tempw/=rate;
        temph/=rate;
	temp = tempw<temph?tempw:temph;//make sure that even the smaller one is larger than length
        if(temp>=length)//this layer can be used for extreact thumbnail
            break;
        printf("%d layer is not valide\n",i);
        }
        if(temp<length)//if the length is too large, even larger than the width or height, which means we can not get this one, just make it smaller
          length=temp;
        w=tempw;
        h=temph;

	size=w*h*4;
	dest = (uint32_t *) malloc(size);
	/* dest should be holding the ARGB data of the smallest layer */ 
	openslide_read_region(osr, dest, x, y, validlayer, w, h);
	openslide_close(osr);
	if((f1 = fopen(tmpfile, "wb+"))==NULL) {
		free(dest);
		perror("open file failure");
		exit(1);
	}
	
	/* write ppm file header */
	fprintf(f1, "P6\n%" PRId64 " %" PRId64 "\n255\n", w, h);
			
	/* write binary RGB info */
	for (i = 0; i< w * h; i++) {
		val = dest[i];
		putc((val >> 16) & 0xFF, f1);
		putc((val >> 8) & 0xFF, f1);
		putc((val >> 0) & 0xFF, f1);
	}
        //
	fclose(f1);
	free(dest);
	
	printf("%d,%d,%d,%d\n",w,h,length,temp);

        temp = w>h?w:h;
	temp /= length;
	w /= temp;
	h /= temp;
	printf("The generating thumbnail for %s is %ldx%ld pixels\n", filename1, w, h);
	sprintf(cmd, "convert %s -resize %ldx%ld %s", tmpfile,w, h,filename2); 
	system(cmd);
	remove(tmpfile);
	(*env)->ReleaseStringUTFChars(env, filename, filename1);
	(*env)->ReleaseStringUTFChars(env, tbname, filename2);
	return ;
}

 
JNIEXPORT void JNICALL JNICALL Java_edu_emory_cci_imagedb_FileDataCollector_OpenSlideTools_getDimension(JNIEnv *env, jclass dummy, jstring filename, jstring tmpname) {

	openslide_t *osr;
	int64_t width, height;
	FILE *f1;
	const char *filename1 = (*env)->GetStringUTFChars(env, filename, NULL);
        const char *filename2 = (*env)->GetStringUTFChars(env, tmpname, NULL);


	if (!openslide_can_open(filename1)) 
	{	
		printf("can't open image %s\n", filename);
		exit(1);
	}

	if ((osr = openslide_open (filename1))==NULL)
	{
		printf("open the whole slide image %s error\n", filename);
		exit(1);
	}
	openslide_get_layer_dimensions(osr,0,&width, &height);
	openslide_close(osr);
	if((f1 = fopen(filename2, "wb+"))==NULL) {
		perror("open file failure");
		exit(1);
	}
	fprintf(f1, "%d  %d", width, height);
	fclose(f1);
	
	(*env)->ReleaseStringUTFChars(env, filename, filename1);
	(*env)->ReleaseStringUTFChars(env, tmpname, filename2);
	return;
}


JNIEXPORT void JNICALL Java_edu_emory_cci_imagedb_FileDataCollector_OpenSlideTools_getRegionImage(JNIEnv *env, jclass dummy, jstring inFile, jstring outFile, jstring format, jdouble coordinateSX, jdouble coordinateSY, jdouble imageWidth, jdouble imageHeight)
{
	// Openslide 
	FILE *f;	// svs file pointer (input)
	FILE *f1;	// ppm file pointer
	FILE *f2;	// jpg file pointer (output)
	long lSize = 0;
	long len;
	
	const char * fformat1 = (*env)->GetStringUTFChars(env, format, NULL); // jpeg, jpg, gif, png
        char fformat[10];
        strcpy(fformat,fformat1);
	const char * fname = (*env)->GetStringUTFChars(env, inFile, NULL);	// svs file name
	char sfname[256];	// ppm file name
	const char * tfname = (*env)->GetStringUTFChars(env, outFile, NULL);	// output file name
	 
        printf("%s\n%s\n%s\n",fformat,fname,tfname);
	// coordinate variables
	int64_t w, h;
	int64_t SX, SY, EX, EY;
	int64_t width, height;
	
	SX = coordinateSX;
	SY = coordinateSY;
	width = imageWidth;
	height = imageHeight;
	
	EX = SX + width;
	EY = SY + height;
	
	if(SX < 0){
		SX = 0;
	}
	
	if(SY < 0){
		SY = 0;
	}
	
	// change file format into lower characters , also jpeg will be changed into jpg
	int i=0;
	int maxLen=0;
	int result=1;
	int init=1;
	
	maxLen = strlen(fformat);
	for(i=0; i < maxLen; i++)
	{
		fformat[i] = tolower( fformat[i] );
	}
	
	// jpeg check
	result = strncmp(fformat,"jpeg", 4);
	if(result==0)
	{
		strcpy(fformat, "jpg");
	}
	
	// assign temp directory & filename for reading and writing ppm & jpeg & png
	char tmpDir[256];	// directory path
	char tmpName[500];	// tilename + coordinate x + coordinate y + width + height
									
	sprintf(tmpName,"%s_%f_%f_%f_%f",outFile, coordinateSX, coordinateSY, imageWidth,imageHeight);
	
	strcpy(tmpDir,"/tmp/");
	
	// assign a full path for files 
	 int ii;
	 char c;
	 char name[11];
	 for (ii=0;ii<10;ii++)
	 {
		  c=rand()%26+65;
		  name[ii]=c;
	 }
         name[ii]='\0';
         strcpy(sfname,"");
	 sprintf(sfname,"/tmp/%s.pnm",name);
	// open svs file
	f = fopen (fname, "rb");
	
	if (f == NULL)
	{
		return;
	}
	else
	{
		// openslide check
		if(openslide_can_open(fname) == 0){
		return;
		}
		
		openslide_t *osr = openslide_open(fname);
		
		if(osr == NULL) {
			return;
		}
		
		// assign memory for writing binary information
		uint32_t *buf = malloc(width * height * 4);
		openslide_read_region(osr, buf, SX, SY, 0, width, height);
		
		// create an empty ppm file
		char cmd1[500];
		sprintf(cmd1,"echo > %s", sfname);
		system(cmd1);
		
		// write image binary info from butter to ppm file
		f1 = fopen(sfname, "rb+");
		
		if (f1 == NULL){
			free(buf);
			fclose(f);
			return;
		}
		else
		{
			// seek first file pointer
			fseek(f1, 0, SEEK_SET);
			
			// write header
			fprintf(f1, "P6\n%" PRId64 " %" PRId64 "\n255\n", width, height);
			
			// write binary RGB info
			for (int64_t i = 0; i< width * height; i++) {
				uint32_t val = buf[i];
				putc((val >> 16) & 0xFF, f1);
				putc((val >> 8) & 0xFF, f1);
				putc((val >> 0) & 0xFF, f1);
			}
			fclose(f1);
			fclose(f);
			
			// compare file format and execute system commands
			
			char cmd2[500];
                        sprintf(cmd2, "convert %s > %s", sfname, tfname);
/*
			init = 1;
			result = init;
			
			result = strncmp(fformat,"jpg",3);
			
			if(result == 0)
			{
				sprintf(cmd2, "pnmtojpeg %s > %s", sfname, tfname);
			}
			result = init;
			result = strncmp(fformat,"png",3);
			
			if(result == 0)
			{
				sprintf(cmd2, "pnmtopng %s > %s", sfname, tfname);
			}
			
			result = init;
			result = strncmp(fformat,"gif",3);
			
			if(result == 0)
			{
				sprintf(cmd2, "ppmtogif %s > %s", sfname, tfname);
			}

			if(result == 0)
			{
				sprintf(cmd2, "ppmquant 256 %s | ppmtogif > %s", sfname, tfname);
			
			}
*/
			system(cmd2);
			
		}// end of second else
              
    } // end of third else	
    remove(sfname);

    (*env)->ReleaseStringUTFChars(env, inFile, fname);
	(*env)->ReleaseStringUTFChars(env, outFile, tfname);
	(*env)->ReleaseStringUTFChars(env, format, fformat1);
	return;
}
