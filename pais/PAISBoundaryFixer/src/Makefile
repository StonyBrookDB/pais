CC = g++
CPPFLAGS = -I/usr/local/include 
LDFLAGS = -L/usr/local/lib

OBJS = BoundaryFix.o clipper.o

BoundaryFix: ${OBJS}
	${CC} -o BoundaryFix ${CPPFLAGS} ${OBJS} ${LDFLAGS}

BoundaryFix.o: BoundaryFix.cpp clipper.cpp clipper.h
	${CC} ${CPPFLAGS} -c BoundaryFix.cpp
	
clipper.o: clipper.cpp clipper.h
	${CC} ${CPPFLAGS} -c clipper.cpp		

clean:
	rm -f BoundaryFix clipper ${OBJS}
	@echo "all cleaned up!"
	
