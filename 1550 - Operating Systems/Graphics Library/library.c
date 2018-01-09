/*
 *	Alec Trievel
 *	CS 1550 2:30
 *	Project   #1
*/

/* header files needed for all the required syscalls */
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <unistd.h>
#include <sys/select.h>
#include <sys/time.h>
#include <sys/types.h>
#include <linux/fb.h>
#include <termios.h>
#include "iso_font.h"

/* defining constants and useful variables */
#define ESC_CODE "\33[2J"
#define ESC_BYTES 8	//string length of  7 + '\0' = 8 bytes
#define SEC_MULTIPLIER 1000000
typedef unsigned short color_t;
int fb = 0;
void* memo;
unsigned long xSize, ySize, screensize, length;
struct fb_var_screeninfo vinfo;
struct fb_fix_screeninfo finfo;

/* function prototypes */
void init_graphics();
void exit_graphics();
void clear_screen();
char getkey();
void sleep_ms(long ms);
void draw_pixel(int x, int y, color_t color);
void draw_line(int x1, int y1, int x2, int y2, color_t c);
void draw_text(int x, int y, const char *text, color_t c);
void draw_char(int x, int y, const char character, color_t c);
color_t create_color(int r, int g, int b);

/*
int main()
{
	color_t aColor = create_color(9, 30, 31);
	init_graphics();
	char aChar = get_key();
	exit_graphics();
	printf("\n%c\n", aChar);
	return 0;
}
 */
void init_graphics()
{
	//help from stack overflow
	struct termios term;

	fb = open("/dev/fb0", O_RDWR);
	ioctl(fb, FBIOGET_FSCREENINFO, &finfo);
	ioctl(fb, FBIOGET_VSCREENINFO, &vinfo);

	xSize = vinfo.xres_virtual;
	ySize = vinfo.yres_virtual;
	screensize = xSize * ySize * vinfo.bits_per_pixel / 8;
	//end help from stack overflow

	//code from recitation
	memo = mmap(NULL, screensize, PROT_READ|PROT_WRITE, 
	MAP_SHARED, fb, 0);
	ioctl(1, TCGETS, &term);
	term.c_lflag &= ~ICANON;
	term.c_lflag &= ~ECHO;
	ioctl(1, TCSETS, &term);
	//end code from recitation
}

void exit_graphics()
{
	struct termios term;
	//code from recitation
	int err = ioctl(1, TCGETS, &term);
	term.c_lflag |= ECHO;
	term.c_lflag |= ICANON;
	err = ioctl(1, TCSETS, &term);
	munmap(memo, screensize);
	close(fb);
	//end code from recitation
}
 
void clear_screen()
{
	write(STDOUT_FILENO, ESC_CODE, ESC_BYTES); 
}

void sleep_ms(long ms)
{
	struct timespec req;
	req.tv_sec = 0;
	req.tv_nsec = ms * SEC_MULTIPLIER;
	nanosleep(&req, (struct timespec *)NULL); 
}

char getkey()
{
	fd_set rfds;
	struct timeval tv;
	int retval;
	char readChar;
	/*
	if(FD_ISSET(0, &rfds))
	{
		FD_ZERO(&rfds);
	}
	*/
	FD_ZERO(&rfds);
	FD_SET(STDIN_FILENO, &rfds);

	tv.tv_sec = 10;
	tv.tv_usec = 0;

	retval = select(STDIN_FILENO+1, &rfds, NULL, NULL, &tv);
	if(retval > 0)
	{
		read(STDIN_FILENO+1, &readChar, sizeof(readChar));
	}

	return readChar;
}

void draw_pixel(int x, int y, color_t color)
{
	if(y < 0 || x < 0 || y > ySize || x > xSize)
	{
		return;
	}

	int location = (x + vinfo.xoffset) * (vinfo.bits_per_pixel/8) + 
	(y + vinfo.yoffset) * finfo.line_length;
	*((unsigned short int*)(memo + location)) = color; 
}

void draw_line(int x1, int y1, int x2, int y2, color_t c)
{
        //help from rosettacode
        int dx = x2 - x1;
        int dy = y2 - y1;
        if(dx < 0)
                dx = -dx;
        if(dy < 0)
                dy = -dy;
        int sx = x1 < x2 ? 1 : -1;
        int sy = x1 < x2 ? 1 : -1;
        int err = (dx > dy ? dx : -dy)/2, e2;

        for(;;)
        {
                draw_pixel(x1, y1, c);
                if(x1 == x2 && y1 == y2) break;
                e2 = err;
                if(e2 > -dx) {err -=dy; x1 += sx;}
                if(e2 < dy) {err += dx; y1 += sy;}
        }
	//end help from rosettacode
} 

void draw_text(int x, int y, const char* text, color_t c)
{
	int i = 0;

	while(text[i] != '\0')
	{
		draw_char(x, y, text[i], c);
	}
}

void draw_char(int x, int y, const char character, color_t c)
{
	 
}
color_t create_color(int r, int g, int b)
{
	return r <<11 | g << 5 | b;
}
