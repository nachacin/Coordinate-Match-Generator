package com.nestorchacin.coordinates;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String[] JSON_filenames = {
            "input-easy1.json",
            "input-easy2.json",
            "problem1.json",
            "problem2.json",
            "problem3.json"
    };
    private static final String [] CSV_filenames = {
            "input-easy1.csv",
            "input-easy2.csv",
            "problem1.csv",
            "problem2.csv",
            "problem3.csv"
    };

    public static void main(String[] args) {
        int selection = Integer.parseInt(args[0]) - 1;
        List<Coordinates> coors = new ArrayList<>();
        try {
            File csvData = new File(URLDecoder.decode(Main.class.getClassLoader().getResource(CSV_filenames[selection]).getPath(), StandardCharsets.UTF_8));
            CSVParser csv_parser = CSVParser.parse(csvData, StandardCharsets.UTF_8 ,CSVFormat.EXCEL.withHeader());
            for (CSVRecord record : csv_parser) {
                coors.add(new Coordinates(
                        Integer.parseInt(record.get("Id")),
                        Double.parseDouble(record.get("Latitude")),
                        Double.parseDouble(record.get("Longitude")),
                        Source.CSV));
            }
            csv_parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Coordinates coordinate: coors) {
            System.out.println(coordinate);
        }

        JSONcoorParser parser = new JSONcoorParser();
        try {
            InputStream input = new FileInputStream(URLDecoder.decode(Main.class.getClassLoader().getResource(JSON_filenames[selection]).getPath(), StandardCharsets.UTF_8));
            coors = parser.readJsonStream(input);
            input.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        for (Coordinates coordinate: coors) {
            System.out.println(coordinate);
        }
    }
}
