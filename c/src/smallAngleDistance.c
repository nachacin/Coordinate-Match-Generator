#include "smallAngleDistance.h"
#define RADIUS          6371230
#define SQUARE(a)       ((a) * (a))
#define MAX_DISCREP     100
#define RAD_PER_DEG     M_PI / 180
#define THRESHOLD       SQUARE(MAX_DISCREP / (RADIUS * RAD_PER_DEG))

/*
 * The main assumption here, is that a small angle approximation is sufficient
 * to compute a suitable comparison, and the expression can be manipulated to 
 * make it less computationally expensive as follows:
 * d = r * theta
 * theta = sqrt(((lat2 - lat1) PI / 180) ^ 2 + ((lon2 - lon1) * PI / 180) * cos(lat2 * PI / 180)) ^ 2)
 */

bool within_threshold(double lat1, double lon1, double lat2, double lon2) {
    double value;
    double cosval = cos(lat2 * RAD_PER_DEG);
    value = (lat2 - lat1) * (lat2 - lat1) +
            (lon2 - lon1) * (lon2 - lon1) * cosval * cosval;
    return (value < THRESHOLD);
}