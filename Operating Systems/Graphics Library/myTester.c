#include <stdlib.h>
#include <stdio.h>
typedef unsigned short color_t;

void init_graphics();
void exit_graphics();
void clear_screen();
char getkey();
void sleep_ms(long ms);
void draw_pixel(int x, int y, color_t color);
void draw_line(int x1, int y1, int x2, int y2, color_t c);
void draw_text(int x, int y, const char *text, color_t c);
void draw_char(int x, int y, char character, color_t c);
color_t create_color(int r, int g, int b);

int main()
{
	clear_screen();
	printf("I cleared the screen for you using clear_screen().");
	sleep_ms(5000);
	
	printf("\nFirst, I will run init_graphics.\n");
	init_graphics();
	sleep_ms(8000);
	sleep_ms(8000);
	sleep_ms(8000);
	
	printf("Now, I will get a key stroke from you using"); 
	printf(" getkey() and return it to the \nscreen.");
	draw_pixel(0, 0, 0);
	sleep_ms(8000);
	printf("\nGo ahead and type any key.\n");
	char aChar = getkey();
	printf("Your keystroke was: %c.\n", aChar);
	sleep_ms(9000);
	sleep_ms(5000);
	clear_screen();
	
	printf("Next, I will draw some lines of various size "); 
	printf("and color and, I will also use \nsleep_ms() to slow ");
	printf("down the drawing speed.\n");
	color_t aColor = create_color(7, 20, 13);
	draw_line(0, 0, 200 ,300, aColor);
	sleep_ms(500);
	draw_line(50, 50, 300, 350, aColor - 1000);
	sleep_ms(500);
	draw_line(0, 25, 75, 350, create_color(10, 20, 1));
	sleep_ms(500);
	draw_line(200, 50, 600, 50, create_color(20, 0, 2));
	printf("Lastly, I will run exit_graphics().\n");
	exit_graphics();
	
	printf("As you can see, all my functions work properly ");
	printf("besides draw_text().\n");
	return 0;
}
