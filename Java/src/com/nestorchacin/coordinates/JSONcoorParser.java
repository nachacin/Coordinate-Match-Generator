package com.nestorchacin.coordinates;

import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class JSONcoorParser implements Runnable {
    private String fileToParse;
    private BlockingQueue<Coordinates> queue;
    private JsonReader reader;

    public JSONcoorParser() {
    }

    public JSONcoorParser(String fileToParse, BlockingQueue<Coordinates> queue) {
        this.fileToParse = fileToParse;
        this.queue = queue;
    }

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

    @Override
    public void run() {
        try {
            InputStream input = new FileInputStream(fileToParse);
            this.reader = new JsonReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            this.reader.beginArray();
            Coordinates coordinate;
            while(reader.hasNext()) {
                try {
                    coordinate = readCoordinates(this.reader);
                    queue.put(coordinate);
                    System.out.println(Thread.currentThread().getName() + " added \"" + coordinate + "\" to queue, queue size: " + queue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.reader.endArray();
            input.close();
            System.out.println(Thread.currentThread().getName()+" finished");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


