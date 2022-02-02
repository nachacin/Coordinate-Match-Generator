#include "smallAngleDistance.h"
#define RADIUS          6371230
#define SQUARE(a)       ((a) * (a))
#define MAX_DISCREP     100
#define RAD_PER_DEG     M_PI / 180
#define THRESHOLD       SQUARE(MAX_DISCREP / (RADIUS * RAD_PER_DEG))

/**
* <p>The main assumption here, is that a small angle approximation is sufficient to compute a suitable comparison, and
* the expression can be manipulated to make it less computationally expensive as follows:</p>
*
* <p>Assuming the Earth is a perfect sphere, the shortest distance between two points on the surface will lie along a
* great circle around the sphere (a circle created by the intersection of the sphere and plane that passes through
* its center point. Thus,</p>
*
* <p>d = r * theta .....  (1)</p>
*
* <p>where theta is the smallest angle between the two points, AKA the central angle. This angle can be approximated
* by the following equation using the planar approximation (Assuming the objects lie on flat surface since they are
* so close together):</p>
*
* <p>theta = sqrt(((lat2 - lat1) * (PI / 180)) ^ 2 + ((lon2 - lon1) * (PI / 180) * cos(lat2 * PI / 180)) ^ 2) .... (2)</p>
*
* <p>substituting for theta in (2) with (1) and squaring both sides,</p>
*
* <p>(d /( r * PI / 180))^2 = (lat2 - lat1) ^ 2 + ((lon2 - lon1) * cos(lat2 * PI / 180)) ^ 2</p>
*
* <p>And since d and r are given as constants, the left-hand side becomes a threshold to
* determine the return value.</p>
*
* @param coordinate
* @return True if coordinates are within threshold, False otherwise.
*/

bool within_threshold(double lat1, double lon1, double lat2, double lon2) {
    double value;
    double cosval = cos(lat2 * RAD_PER_DEG);
    value = (lat2 - lat1) * (lat2 - lat1) +
            (lon2 - lon1) * (lon2 - lon1) * cosval * cosval;
    return (value < THRESHOLD);
}