#include "linkedListQueue.h"
static coor_node_t *front;
static coor_node_t *rear;

static void print_coor_node(coor_node_t *node) {
    printf("Element %d from %s queue\n", node->data.id, (node->data.source == J_SOURCE)? "JSON":"CSV");
    printf("Latitude: %lf\nLongitude: %lf\n", node->data.latitude , node->data.longitude);
    printf("Status: %s\n", (node->data.stat == MATCHED)? "Matched":"Unmatched");
}

void enqueue(int anId, double aLat, double aLon, source_t src) {
    coor_node_t *temp;
    temp = (coor_node_t*)malloc(sizeof(coor_node_t));
    temp->data.id = anId;
    temp->data.latitude = aLat;
    temp->data.longitude = aLon;
    temp->data.source = src;
    temp->data.stat = UNMATCHED;
    temp->link = NULL;
    if (rear == NULL) {
        front = rear = temp;
    } else {
        rear->link = temp;
        rear = temp;
    }
}

bool dequeue() {
    coor_node_t *temp;
    temp = front;
    if (front == NULL) {
        printf("Queue underflow\n");
        front = rear = NULL;
        return false;
    } else {
        printf("\nDeleting the following: \n");
        print_coor_node(front);
        front = front->link;
        free(temp);
        return true;
    }
}

/* Transfers a coor_node_t element to the caller and unlinks the element
 * from the queue by setting its 'link' member to NULL
 */
coor_node_t* dequeue_transfer() {
    coor_node_t *temp;
    temp = front;
    if(front == NULL) {
        front = rear = NULL; // Definitively sets the queue to empty
    } else {
        front = front->link; // Reassign front pointer to the next in the queue
        temp->link = NULL; // Unlink the coor_node_t object from the queue
    }
    return temp;
}

void display_queue() {
    coor_node_t *temp;
    temp = front;
    int cnt = 0;
    if (front == NULL) {
        printf("Queue underflow\n");
    } else {
        printf("\nThe elements of the queue are:\n");
        while (temp) {
            print_coor_node(temp);
            temp = temp->link;
            cnt++;
        }
    }
}

bool queue_empty() {
    if (front == NULL) {
        return true;
    } else {
        return false;
    }
}