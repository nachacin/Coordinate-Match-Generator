#ifndef OUTPUT_GENERATOR_H
#define OUTPUT_GENERATOR_H

#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>

typedef struct output_record {
    int csv_id;
    int json_id;
} output_record_t;

typedef struct dynamic_array {
    output_record_t * first;
    int len;
    int size;
} dynamic_array_t;

dynamic_array_t init_array();
void add_record(dynamic_array_t* d_array, int CSV_id, int JSON_id);
bool write_record(FILE* out_fp, output_record_t record);

#endif