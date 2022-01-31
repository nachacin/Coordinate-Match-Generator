package com.nestorchacin.coordinates;

import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JSONcoorParser {
    public List<Coordinates> readJsonStream(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return readCoorArray(reader);
        }
    }

    public List<Coordinates> readCoorArray(JsonReader reader) throws IOException {
        List<Coordinates> coordinatesList = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            coordinatesList.add(readCoordinates(reader));
        }
        reader.endArray();
        return coordinatesList;
    }

    public Coordinates readCoordinates(JsonReader reader) throws IOException {
        int id = -1;
        double latitude = 400;
        double longitude = 400;

        reader.beginObject();
        while (reader.hasNext()) {
            String attribute = reader.nextName();
            switch (attribute) {
                case "Id":
                    id = reader.nextInt();
                    break;
                case "Latitude":
                    latitude = reader.nextDouble();
                    break;
                case "Longitude":
                    longitude = reader.nextDouble();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Coordinates(id, latitude, longitude, Source.JSON);
    }
}


