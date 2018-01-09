/*
*		Alec Trievel
*		CS 1550 2:30 
*		Project 2
*		Requires: prodcon.c, sys.c, syscall_table.S, unistd.h
*/

#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <linux/unistd.h>
#include <sys/mman.h>

struct cs1550_sem
{
        int count;
        struct semNode *head;
        struct semNode *tail;
};

void cs1550_down(struct cs1550_sem *sem); 
void cs1550_up(struct cs1550_sem *sem); 
void initilaize_pointers(int buffSize);

#define CONTINUE  1

void *BASE_PTR;							//the shared memory for the threads created by fork()
struct cs1550_sem *empty;			//the empty buffer 
struct cs1550_sem *full;					//the full buffer
struct cs1550_sem *lock;				//the lock that acts as our mutex

void *SHARED_PTR;						//the sharred buffer memory
int *bufferLength_ptr;						//the length of the buffer 
int *bufferData_ptr;						//the data associated with the buffer
int *producer_ptr;							//pointer to the producer data
int *consumer_ptr;							//pointer to the consumer data

void main(int argc, char *argv[])
{
	int producers, consumers, bufferLength;
	int MAP_SIZE = sizeof(struct cs1550_sem) * 3;	//need memory slot for producers, consumers, and lock
	
	if(argc != 4)
	{
		fprintf(stderr, "Error, invlaid argument count.\n");
	}
	
	producers = atoi(argv[1]);
	consumers = atoi(argv[2]);
	bufferLength = atoi(argv[3]);
	int INT_MAP_SIZE = (bufferLength + 3) * sizeof(int);	

	 BASE_PTR = (void *) mmap(NULL, MAP_SIZE, PROT_READ|PROT_WRITE, MAP_SHARED|MAP_ANONYMOUS, 0, 0);		//mapped memory for the empty/full/"lock" semaphores to share
	 SHARED_PTR = (void *) mmap(NULL, INT_MAP_SIZE, PROT_READ|PROT_WRITE, MAP_SHARED|MAP_ANONYMOUS, 0, 0);	//mapped memory for the buffer to be accessed by the prods/cons 
	 
	 //below will initilaize the pointers with the correct amount of memory  
	 empty = (struct cs1550_sem*) BASE_PTR;
	 full = (struct cs1550_sem*) BASE_PTR + 1;
	 lock = (struct cs1550_sem*) BASE_PTR + 2;
	 bufferLength_ptr = (int *)  SHARED_PTR;
	 producer_ptr = (int *)  SHARED_PTR + 1;
	 consumer_ptr = (int *)  SHARED_PTR + 2;
	 bufferData_ptr = (int *)  SHARED_PTR + 3; 
	 initilaize_pointers(bufferLength);
	//end initilaization
	
	int counter = 0;
	
	//below is the implementation for the producer 
	for(counter = 0; counter < producers; counter++)
	{
		int pancake;
		pid_t pid;
		pid = fork();	//creates the new child process
		
		if(pid == 0)
		{
			while(CONTINUE)		//loops forever until interupt occurs
			{
				cs1550_down(empty);
				cs1550_down(lock);
				
				pancake = *producer_ptr;	//produces a new pancake
				bufferData_ptr[*bufferLength_ptr] = pancake; //sets the buffer data to the new pancake produced
				int producerNum = counter + 'A';	//ASCII offset of 65 so character is a letter
				printf("Chef %c Produced: Pancake%d\n",  producerNum, pancake);
				
				*producer_ptr = (*producer_ptr + 1) % *bufferLength_ptr;	//increments producer, makes sure the value is of acceptbale size by taking the remainder from the buffer length
				
				cs1550_up(lock);
				cs1550_up(empty);
			}
		}
	}
	
	//below is the implementation for the consumer 
	for(counter = 0; counter < consumers; counter++)
	{
		int pancake;
		pid_t pid;
		pid = fork();	//creates the new child process
		
		if(pid == 0)
		{
			while(CONTINUE)		//looks forever until interrupt occurs 
			{
				cs1550_down(full);
				cs1550_down(lock);
				
				pancake = bufferData_ptr[*consumer_ptr];	//takes a pancake from the buffer
				int consumerNum = counter + 'A';		//ASCII offset of 65 so character is a letter
				printf("Consumer %c Consumed: Pancake%d\n",  consumerNum, pancake);
				
				*consumer_ptr = (*consumer_ptr + 1) % *bufferLength_ptr; //increments consumer, makes sure the value is of acceptbale size by taking the remainder from the buffer size 
				
				cs1550_up(lock);
				cs1550_up(full);
			}
		}
	}
	
	wait(NULL);		//waits for the child processes to finish before exiting prematurely by mistake
	
}

//starts all pointers at the starting location for the specified memory
void initilaize_pointers(int buffSize)
{
	empty -> count = 0;		//empty starts off empty (haha)
	full -> count = buffSize;	//full is how many possible are remaining in the buffer
	lock -> count = 1;		//our lock in class starts at 1
	
	//below are null becuase there is nothing in the lists yet
	empty -> head = NULL;
	full -> head = NULL;
	lock -> head = NULL;
	
	empty -> tail = NULL;
	full -> tail = NULL;
	lock -> tail = NULL;
}

//both syscalls are taken from Mohammad's GitHub skelton codes
void cs1550_down(struct cs1550_sem *sem) 
{
     syscall(__NR_sys_cs1550_down, sem);
}

void cs1550_up(struct cs1550_sem *sem) 
{
     syscall(__NR_sys_cs1550_up, sem);
}

