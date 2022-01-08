#include "linkedList.h"

// Helper function to print a given linked list
void printList(coor_node_t* head)
{
    coor_node_t* ptr = head;
    printf("\nPrinting entire linked list: \n");
    while (ptr) {
        printf("Element %d from %s List\n", ptr->data.id, (ptr->data.source == J_SOURCE)? "JSON":"CSV");
        printf("Latitude: %lf\nLongitude: %lf\n", ptr->data.latitude , ptr->data.longitude);
        printf("Status: %s\n", (ptr->data.stat == MATCHED)? "Matched":"Unmatched");
        printf(" \\/ \n");
        ptr = ptr->link;
    }
 
    printf("NULL\n");
}
 
// Function to add a new node at the tail end of the list instead of its head
void appendNode(coor_node_t** head, int anId, double aLat, double aLon, source_t src)
{
    coor_node_t* current = *head;
    coor_node_t* node = (coor_node_t*)malloc(sizeof(coor_node_t));
    node->data.id = anId;
    node->data.latitude = aLat;
    node->data.longitude = aLon;
    node->data.source = src;
    node->data.stat = UNMATCHED;
    node->link = NULL;
 
    // special case for length 0
    if (current == NULL) {
        *head = node;
    }
    else {
        // locate the last node
        while (current->link != NULL) {
            current = current->link;
        }
 
        current->link = node;
    }
}
// Returns true if values either c_id and j_id have been updated
bool rake_delete(coor_node_t** head, int *c_id, int *j_id) {
    coor_node_t *temp = *head;
    if (*head == NULL) {
        return false;
    } else if ((*head)->link != NULL) {
        *c_id = ((*head)->data.source == C_SOURCE)? (*head)->data.id : -1;
        *j_id = ((*head)->data.source == J_SOURCE)? (*head)->data.id : -1;
        *head = (*head)->link;
        free(temp);
        return true;
    } else {
        *c_id = ((*head)->data.source == C_SOURCE)? (*head)->data.id : -1;
        *j_id = ((*head)->data.source == J_SOURCE)? (*head)->data.id : -1;
        *head = NULL;
        free(temp);
        return true;
    }
}

bool deleteNode(coor_node_t** head,int target_id, source_t src) {
    coor_node_t* current = *head;
    coor_node_t* previous = NULL;
    do {
        if ((current->data.id == target_id) && (current->data.source == src)) {
            current->data.stat = MATCHED;
            break;
        } else {
            previous = current;
            current = current->link;
        }
    } while(current != NULL);
    if (previous == NULL) {
        *head = current->link;
        free(current);
        printf("Match found, reassigning head pointer\n");
        return true;
    } else if (current != NULL) {
        previous->link = current->link;
        free(current);
        printf("Match found, linking previous to next\n");
        return true;
    } else {
        printf("No match found\n");
        return false;
    }
    printf("Error! Undefined results obtained\n");
    return false; // No matched found
}
/*
 * The function dequeue_transfer() and depending on the returned values,
 * the following occurs:
 * if dequeue_transfer returns NULL, meaning the queue is empty, the linked list
 * remains unchanged.
 * if dequeue_transfer returns a pointer-to-coor_node_t object, it will compare its
 * contained coordinates to all other coor_node_t objects in the linked list. If a 
 * comparison returns true as defined by the within_threshold function, the id 
 * members are written to addresses c_id and j_id, respectively. If no matches are
 * found, the coor_node_t object obtained from queue is appended to the end of the
 * linked list.
 */
bool appendORmatch(coor_node_t** head, int *c_id, int *j_id) {
    coor_node_t* current = *head;
    coor_node_t* previous = NULL;
    coor_node_t* received = dequeue_transfer();
    if (received == NULL) {
        return false;
    } else {
        if (current == NULL) {
            *head = received;
            return false;
        } else {
            do {
                if (within_threshold(current->data.latitude,
                                     current->data.longitude,
                                     received->data.latitude,
                                     received->data.longitude)) {
                    break;
                } else {
                    previous = current;
                    current = current->link;
                }
            } while (current != NULL);
            // Handle case: first element is a match
            if (previous == NULL) {
                *c_id = (current->data.source == C_SOURCE)? current->data.id: received->data.id;
                *j_id = (current->data.source == J_SOURCE)? current->data.id: received->data.id;
                *head = current->link;
                free(current);
                free(received);
                return true;
            // Handle case: second to last is a match               
            } else if (current != NULL) {
                *c_id = (current->data.source == C_SOURCE)? current->data.id: received->data.id;
                *j_id = (current->data.source == J_SOURCE)? current->data.id: received->data.id;
                previous->link = current->link;
                free(current);
                free(received);
                return true;
            // Handle case: no matches
            } else {
                previous->link = received;
                return false;
            }
        printf("appendORmatchError! Undefined results obtained\n");
        return false;
        }
    }
}

bool appendExistingNode(coor_node_t** head) {
    coor_node_t* current = *head;
    coor_node_t* node = dequeue_transfer();
    if (current == NULL) {
        if (node == NULL) {
            printf("Queue was found empty, list remains empty\n");
            return false;
        } else {
            printf("Adding first element to list\n");
            *head = node;
            return true;
        }
    } else {
        if (node == NULL) {
            printf("Queue was found empty, list unchanged\n");
            return false;
        } else {
            // Locate last node
            while (current->link != NULL) {
                current = current->link;
            }
            current->link = node;
            printf("\nAdded another element to list\n");
            return true;
        }
    }
}