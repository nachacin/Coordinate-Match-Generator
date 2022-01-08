#include "coordinatesCSVparser.h"

bool test_csv_format(FILE *csv_file){
    char *VALID_STRING = "Id,Latitude,Longitude\n";
    char heading[32];
    fgets(heading, 32, csv_file);
    if (strcmp(VALID_STRING, heading) == 0) {
        return true;
    } else {
        printf("test_csv_format: Error! CSV format invalid!\n");
        return false;
    }
}

void parse_coor_csv(FILE *csv_file) {
    int id;
    double lat;
    double lon;
    // Test the file format at beginning of the file
    while(fscanf(csv_file, "%d,%lf,%lf", &id, &lat, &lon) == 3) {
        enqueue(id, lat,lon, C_SOURCE);
    }
}