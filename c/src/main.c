#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include "linkedListQueue.h"
#include "linkedList.h"
#include "coordinatesJSONparser.h"
#include "coordinatesCSVparser.h"
#include "outputGenerator.h"

const char* JSON_PATHNAMES[5] = {"data/easy1/input.json",
                                 "data/easy2/input.json", 
                                 "data/problem1/problem1.json", 
                                 "data/problem2/problem2.json", 
                                 "data/problem3/problem3.json"};

const char* CSV_PATHNAMES[5] = {"data/easy1/input.csv", 
                                "data/easy2/input.csv", 
                                "data/problem1/problem1.csv", 
                                "data/problem2/problem2.csv", 
                                "data/problem3/problem3.csv"};

const char* OUTPUT_PATHNAMES[5] = {"output/easy1_output.txt",
                                   "output/easy2_output.txt",
                                   "output/problem1_output.txt",
                                   "output/problem2_output.txt",
                                   "output/problem3_output.txt"};

int main(int argc, char *argv[]) {
    const char* json_pname;
    const char* csv_pname;
    const char* out_pname;
    if (argc == 1) {
        printf("Select dataset [1:5] to process: ");
        int selection = fgetc(stdin) - '0' - 1;
        json_pname = JSON_PATHNAMES[selection];
        csv_pname = CSV_PATHNAMES[selection];
        out_pname = OUTPUT_PATHNAMES[selection];
    } else if (argc == 3) {
        csv_pname = argv[1];
        json_pname = argv[2];
        out_pname = "output.txt";
    } else {
        printf("Error, entered wrong number of arguments. Please try again\n");
        exit(EXIT_FAILURE);
    }
    
    // Open and parse entire JSON file
    FILE *json_fp = fopen(json_pname, "r");
    if (json_fp == NULL) {
        printf("main: Error! could not open JSON file\n");
        exit(EXIT_FAILURE);
    }
    if (!parse_coor_json(json_fp)) {
        printf("main: Parsing of JSON file failed\n");
        exit(EXIT_FAILURE);
    }
    fclose(json_fp);
    
    // Open and parse entire CSV file
    FILE *csv_fp = fopen(csv_pname, "r");
    if (csv_fp == NULL) {
        printf("main: Error! could not open CSV file\n");
        exit(EXIT_FAILURE);
    }
    if (test_csv_format(csv_fp)){
        parse_coor_csv(csv_fp);
    }
    fclose(csv_fp);
    coor_node_t* head = NULL;
    int c_ID = 0;
    int j_ID = 0;
    dynamic_array_t output_array;
    output_array = init_array();
    while(!queue_empty()) {
        if (appendORmatch(&head, &c_ID, &j_ID)) {
            add_record(&output_array, c_ID, j_ID);
        }
    }
    while(rake_delete(&head, &c_ID, &j_ID)) {
        add_record(&output_array, c_ID, j_ID);
    }

    FILE *out_fp = fopen(out_pname, "w");
    if(csv_fp == NULL) {
        printf("main: Error! could not open txt file for writing\n");
        exit(EXIT_FAILURE);
    }
    int i;
    for (i = 0; i < output_array.len; i++) {
        write_record(out_fp, output_array.first[i]);
    }
    fclose(csv_fp);
    free(output_array.first);

    printf("\nSuccess! Generated output as a .txt file with pathname: ");
    printf("%s\n", out_pname);
    
    return(EXIT_SUCCESS);
}