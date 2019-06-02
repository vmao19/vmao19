// QuickSort
// Victor Mao
// July 1st, 2018

// Instructions:
// Pass 'G' to generate 100 random integers
// Pass up to 100 integers to sort

#include <iostream>
#include <string.h>
#include <cstdlib>
#include <ctime>
using namespace std;

void quickSort(int *, int *, int *);
void swap(int *, int *);
void printList(int *, int);

int main(int argc, char* argv[])
{
	if (strcmp(argv[1], "G") == 0)
	{
		srand(time(NULL));
		for (int i = 0; i<100; i++) {
			cout << rand() % 1000 << " ";
		}
	}
	else
	{
		int size = min(argc - 1, 100);
		int p[100];

		for (int i = 0; i<size; i++) {
			p[i] = atoi(argv[i + 1]);
		}

		int *start = p;
		int *end = start + size - 1;
		quickSort(p, start, end);
		printList(p, size);
	}

	return 0;
}

void quickSort(int *list, int *start, int *end) {
	if (start != end) {
		int *i = start;
		int *j = end;

		while (i < j) {
			while (i < j) {
				if (*j < *i) {
					swap(i, j);
					i++;
					break;
				}
				else {
					j--;
				}
			}
			while (i < j) {
				if (*i > *j) {
					swap(i, j);
					j--;
					break;
				}
				else {
					i++;
				}
			}
		}

		if (j == start)
			quickSort(list, ++j, end);
		else if (j == end)
			quickSort(list, start, --j);
		else {
			quickSort(list, start, --i);
			quickSort(list, ++j, end);
		}
	}
}

void swap(int *first, int *second) {
	int temp;
	temp = *first;
	*first = *second;
	*second = temp;
}

void printList(int *element, int size) {
	for (int i = 0; i<size; i++) {
		cout << *element << " ";
		element++;
	}
}