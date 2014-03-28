#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <dirent.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <time.h>
#include <openslide.h>

#include <sys/types.h> 
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <inttypes.h>
#include <stdint.h>
#include <sys/time.h>
#include <ctype.h>
#include <string.h>
//gcc -I/usr/local/include/openslide -L/usr/local/lib -lopenslide -o tilegen tilegen.c

#define MAXIMAGENUM 500
int levelglobal = 1;
int tilesize = 256;

static pthread_cond_t cond = PTHREAD_COND_INITIALIZER;
static pthread_mutex_t image_mtx = PTHREAD_MUTEX_INITIALIZER;
static pthread_mutex_t thread_mtx = PTHREAD_MUTEX_INITIALIZER;
static pthread_mutex_t buf_mtx = PTHREAD_MUTEX_INITIALIZER;

static char images[MAXIMAGENUM][100];

int threadNum = 3;
int threads = 0;
int imagenum = 0;

char format[30];
char inputdir[256];
char outputdir[256];

bool check_fail(openslide_t *osr) {
  if (openslide_get_error(osr)) {
    printf("%s\n", openslide_get_error(osr));
    return false;
  }
  return true;
}

bool writeProperty(char *propertyfilename,int tilesize, int width,int height)
{
    FILE *propertyfile;
    if((propertyfile = fopen(propertyfilename, "wb+"))==NULL) {
      perror("open property file failed!\n");
      return false;
    }
    fprintf(propertyfile, "image({\n");
    fprintf(propertyfile, "  Image: {\n");
    fprintf(propertyfile, "     xmlns:    'http://schemas.microsoft.com/deepzoom/2008',\n");
    fprintf(propertyfile, "     Format:   'jpg',\n");
    fprintf(propertyfile, "     Overlap:  0,\n");
    fprintf(propertyfile, "     TileSize: %d,\n",tilesize);
    fprintf(propertyfile, "  Size:{\n");
    fprintf(propertyfile, "   Height: %d,\n",height);
    fprintf(propertyfile, "   Width:  %d\n",width);
    fprintf(propertyfile, "}\n");
    fprintf(propertyfile, "}\n");
    fprintf(propertyfile, "});\n");

    fclose(propertyfile);
    return true;
}

int writeTile(uint32_t *buf,int width,int height, int maxlayer,int shoffset, int layer, char *outputPath,bool mogrify)
{
  int i=0;
  int step = 1;
  uint32_t *curbuf;
  int curwidth,curheight,h,w,sh,sw,eh,ew;
  char tilepath[256];
  char tiledir[256];
  char cmd[1000];
  int count=0;
  int curcount = 0;
  for(step=1,i=0;i<maxlayer;step=step*2,i++)
  {
        curcount = 0;
        sprintf(tiledir,"%s/%d",outputPath,layer-i);
        curwidth = width/step;
        curheight = height/step;
      
        if(step==1)
          curbuf = buf;
        else
        {
          curbuf = (uint32_t *)malloc(curwidth*curheight*4);
        
          for (h=0; h< curheight; h++)
            for(w=0;w< curwidth;w++)
           {
            curbuf[w+curwidth*h] = buf[w*step+h*width*step];
           }
        }

        for(sh = 0;sh<curheight;sh+=tilesize)
         for(sw = 0;sw<curwidth;sw+=tilesize)
         {
           ew = sw+tilesize<=curwidth?sw+tilesize:curwidth;
           eh = sh+tilesize<=curheight?sh+tilesize:curheight;
           
           sprintf(tilepath,"%s/%d_%d.pnm",tiledir,sw/tilesize,sh/tilesize+shoffset/step);
           FILE *tilefp = fopen(tilepath, "w+");
           if (tilefp == NULL){
             printf("cannot open file %s\n",tilepath);
             continue;
           }
           //printf("%s\n",tilepath);
           fseek(tilefp, 0, SEEK_SET);
           fprintf(tilefp, "P6\n%d %d\n255\n", ew-sw, eh-sh);
           
           fflush(tilefp);
           for(h=sh;h<eh;h++)
             for(w=sw;w<ew;w++)
             {
              
               uint32_t val = curbuf[w+h*curwidth];
               putc((val >> 16) & 0xFF, tilefp);
               putc((val >> 8) & 0xFF, tilefp);
               putc((val >> 0) & 0xFF, tilefp);
               
             }
            fflush(tilefp);
            fclose(tilefp);
            count++;
            curcount++;
         }
         if(step!=1)
          free(curbuf);
        if(mogrify&&curcount!=0)
        {
         sprintf(cmd,"mogrify -format jpg %s/*.pnm",tiledir);
         system(cmd);
         sprintf(cmd,"rm %s/*.pnm",tiledir);
         system(cmd);
       }
   }
 return count;
}

void * convert(void *arg)
{

  int curimage;
  pthread_mutex_lock(&thread_mtx);
  int threadID = threads++;
  pthread_mutex_unlock(&thread_mtx);
  char imageName[256];
  char imagePath[256];
  char outputPath[256];
  char title[256];
  char propertyfilename[256];

  while(1)
  {
    int level = levelglobal;
    pthread_mutex_lock(&image_mtx);
    if(imagenum<0)
    {
          pthread_mutex_unlock(&image_mtx);
          break;
    }
    else 
    {
     curimage = imagenum--;
     pthread_mutex_unlock(&image_mtx);
    }

   
    strcpy(imageName,images[curimage]);
    int j=0;
    for(j=strlen(imageName);j>0;j--)
    {
      if(imageName[j]=='.')
        break;
    }
    strncpy(title,imageName,j);
    printf("%d working on %s|%d\n",threadID,title,curimage);
    sprintf(imagePath,"%s/%s",inputdir,imageName);

    //check file    
    if(openslide_can_open(imagePath) == 0){
      printf("wsi image %s not exists!\n",imagePath);
		  continue;
	  }
	  openslide_t *osr = openslide_open(imagePath);
	  if(osr == NULL) {
      printf("open wsi image file %s failed\n",imagePath);
			continue;
		}

    sprintf(outputPath,"%s/%s",outputdir,title);
    mkdir(outputPath,0777);
    
    sprintf(propertyfilename,"%s/image.js",outputPath);
    sprintf(outputPath,"%s/image_files",outputPath);
    mkdir(outputPath,0777);
    int step;
    int max;
    int64_t width64,height64;    
    int64_t nextwidth,nextheight;
    int width,height;
    
    uint32_t *buf;

    char tiledir[256];
    char cmd[2048];

    int layer = 0;
    int i=0;

    //read first level
	
	  //openslide_get_layer_dimensions(osr, level, &width64, &height64);
    openslide_get_level_dimensions(osr, level, &width64, &height64);
    width = width64;
    height = height64;
	  max = width>height?width:height;
    for(step=1;step <= max;step=step*2)
    {
       layer++;
       sprintf(tiledir,"%s/%d",outputPath,layer);
       mkdir(tiledir,0777);
    }

    if(!writeProperty(propertyfilename,tilesize,width,height))
      continue;

    bool failed = false;
    if(level==0)//level 0 should be read strip by strip
    {

     int shcount = 0;
     int heighttmp = 0;
     int expo = 1;
     for(i=0;i<level;i++)
     {
      expo=expo*4;
     }
     int count=0;
     int maxlayer = 0;
     int shoffset = 0;
     for(shcount=0;shcount*2*tilesize<=height;shcount++)
     {
       //printf("%d thread working on %d/%d \n",threadID,shcount*2*tilesize,height);
       heighttmp = (shcount+1)*2*tilesize>height?height-shcount*2*tilesize:2*tilesize;
       buf = (uint32_t *)malloc(width*heighttmp*4);
       openslide_read_region(osr,buf,0,shcount*2*tilesize*expo,level,width,heighttmp); 
       maxlayer = 2;
       shoffset = shcount*2;
       if(!check_fail(osr))
       {
        printf("skip this level 0!\n");
        sprintf(cmd,"rm -rf %s/%d",outputPath,layer);
        system(cmd);
        sprintf(cmd,"rm -rf %s/%d",outputPath,layer-1);
        system(cmd);
        failed = true;
        openslide_close(osr);
        osr = openslide_open(imagePath);
        free(buf);
        break;
       }
       count+= writeTile(buf,width,heighttmp, 2,shoffset, layer, outputPath,false);
       free(buf);
     
	     if(count>10000)
	     {
        //printf("%d thread need to mogrify!\n",threadID);
	      sprintf(cmd,"mogrify -format jpg %s/*/*.pnm",outputPath);
        system(cmd);
        sprintf(cmd,"rm %s/*/*.pnm",outputPath);
        system(cmd);
	      count = 0;
	     }
      }
	    if(!failed)
      {
      sprintf(cmd,"mogrify -format jpg %s/*/*.pnm",outputPath);
      system(cmd);
      sprintf(cmd,"rm %s/*/*.pnm",outputPath);
      system(cmd);

      }
      else
      {
      openslide_close(osr);
      osr = openslide_open(imagePath);
      }
      level++;
      //openslide_get_layer_dimensions(osr, level+1, &nextwidth, &nextheight);
      openslide_get_level_dimensions(osr, level, &nextwidth, &nextheight);
      max = nextwidth>nextheight?nextwidth:nextheight;
      width = nextwidth;
      height = nextheight;
      if(failed)
      {
        if(!writeProperty(propertyfilename,tilesize,width,height))
         continue;
      }
	    
    }

    for(step=1,layer=0;step <= max;step=step*2)
    {
       layer++;
    }

             
    buf = (uint32_t *)malloc(width * height * 4);
    openslide_read_region(osr, buf, 0, 0, level, width, height);
    openslide_close(osr);
    writeTile(buf,width,height, layer, 0, layer, outputPath,true);  
    free(buf);
  }

  pthread_mutex_lock(&thread_mtx);
  printf("thread %d finished job!\n",threadID);
  threads--;
  pthread_mutex_unlock(&thread_mtx); 
}



int main(int argc, char **args) {

    if(argc<3)return 0;
    
    pthread_attr_t attr;
    pthread_attr_init(&attr);
    pthread_attr_setscope(&attr,PTHREAD_SCOPE_SYSTEM);
    int i=0;
    
    strcpy(inputdir,args[1]);
    strcpy(outputdir,args[2]);
    threadNum = atoi(args[3]);
    strcpy(format,"jpg");
    tilesize = 256;
    levelglobal = atoi(args[4]);
    

    pthread_t tid[threadNum];
    struct dirent * ent = NULL;
    DIR *dir = opendir(inputdir);
    int imagenumber = threadNum;
    while((ent = readdir(dir))!=NULL)
    {
        if(ent->d_type==8)
         {
          strcpy(images[imagenum++],ent->d_name);
          printf("%s\n",ent->d_name);
          if(--imagenumber==0)break;
         }
    }
    imagenum--;
    for(i=0;i<threadNum;i++)
    {
     pthread_create(&tid[i],&attr,convert,NULL);
    }
    while(1)
    {
     sleep(1);
     pthread_mutex_lock(&thread_mtx);
     if(threads==0)break;
     pthread_mutex_unlock(&thread_mtx);
    }
    return EXIT_SUCCESS;
}

