#include "outputGenerator.h"
#define INIT_SIZE 128

dynamic_array_t init_array() {
    output_record_t* arr = NULL;
    arr = malloc(INIT_SIZE * sizeof(output_record_t));
    if (arr == NULL) {
        printf("init_array: malloc failed\n");
        exit(EXIT_FAILURE);
    }
    dynamic_array_t d_array;
    d_array.first = arr;
    d_array.len = 0;
    d_array.size = INIT_SIZE;
    return d_array;
}

void add_record(dynamic_array_t* d_array, int CSV_id, int JSON_id) {
    output_record_t record;
    record.csv_id = CSV_id;
    record.json_id = JSON_id;
    if ( d_array->len < d_array->size) {
        d_array->first[d_array->len] = record;
        d_array->len++;
    } else {
        d_array->first = realloc(d_array->first, 2 * d_array->size);
        if (d_array->first == NULL) {
            printf("add_record: realloc failed\n");
            exit(EXIT_FAILURE);
        }
        d_array->size = 2 * d_array->size;
        d_array->first[d_array->len] = record;
        d_array->len++;
    }
}

bool write_record(FILE* out_fp, output_record_t record){
    fprintf(out_fp, "%d:%d\n", record.csv_id, record.json_id);
    return true;
};