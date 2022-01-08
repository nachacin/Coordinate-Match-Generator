#ifndef COORDINATES_CSV_PARSER_H
#define COORDINATES_CSV_PARSER_H

#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include "linkedListQueue.h"

bool test_csv_format(FILE *csv_file);
void parse_coor_csv(FILE *csv_file);

#endif