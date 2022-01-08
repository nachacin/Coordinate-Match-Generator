#include "coordinatesJSONparser.h"

bool parse_coor_json(FILE *json_file) {
    int n = 1;
    char entry[128];
    int id;
    double lat;
    double lon;
    
    char opening = fgetc(json_file);
    if (opening == '[') {
        while (n != EOF) {
            n = fscanf(json_file, " %127[^}]}", entry);
            if (entry[0] != ']') {
                sscanf(entry, " %*[^\"]\"Id\":%*[^\"]\"%d\"%*[^\"]\"Latitude\":%lf%*[^\"]\"Longitude\":%lf", &id, &lat, &lon);
                enqueue(id, lat, lon, J_SOURCE);
            } else {
                return true;
            }
        }
        printf("Parser did not reach \"]\" at end of JSON array as intended");
        return false;
    } else {
        printf("Coordinate format in JSON file was invalid\n");
        return false;
    }
}