#ifndef LINKEDLISTQUEUE_H
#define LINKEDLISTQUEUE_H

#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>

typedef enum {
    J_SOURCE,
    C_SOURCE,
} source_t;

typedef enum {
    MATCHED,
    UNMATCHED,
} status_t;

typedef struct {
    int id;
    double latitude;
    double longitude;
    source_t source;
    status_t stat;
} coordinates_t;

struct coor_node {
    coordinates_t data;
    struct coor_node *link;
};

typedef struct coor_node coor_node_t;

void enqueue(int anId, double aLat, double aLon, source_t); // Function used to insert the element into the queue
bool dequeue(); // Function used to delete the elememt from the queue
coor_node_t* dequeue_transfer();
void display_queue(); // Function used to display all the elements in the queue according to FIFO rule
bool queue_empty();

#endif