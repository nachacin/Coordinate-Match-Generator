#ifndef SMALL_ANGLE_DISTANCE_H
#define SMALL_ANGLE_DISTNACE_H

#include <math.h>
#include <stdbool.h>
#include <stdio.h>
/**
 * Returns true if calculated distance is less than distance 
 * defined by THRESHOLD constant in smallAngleDistance.c
 */
bool within_threshold(double lat1, double lon1, double lat2, double lon2);

#endif