#ifndef LINKEDLIST_H
#define LINKEDLIST_H

#include <stdio.h>
#include <stdlib.h>
#include "linkedListQueue.h"
#include "smallAngleDistance.h"

void printList(coor_node_t* head);
void appendNode(coor_node_t** head, int anId, double aLat, double aLon, source_t src);
bool rake_delete(coor_node_t** head, int *c_id, int *j_id);
bool deleteNode(coor_node_t** head,int target_id, source_t src);
bool appendExistingNode(coor_node_t** head);
bool appendORmatch(coor_node_t** head, int *c_id, int *j_id);

#endif