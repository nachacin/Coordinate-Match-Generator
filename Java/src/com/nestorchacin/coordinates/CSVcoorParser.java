package com.nestorchacin.coordinates;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class CSVcoorParser {
    private static final String ID_NAME = "Id";
    private static final String LAT_NAME = "Latitude";
    private static final String LON_NAME = "Longitude";
    private final File file;
    private final CSVParser parser;

    public CSVcoorParser(File CSVfile) throws IOException {
        this.file = CSVfile;
        this.parser = CSVParser.parse(CSVfile, StandardCharsets.UTF_8, CSVFormat.EXCEL.withHeader());
    }

//    public boolean parseCoor(LinkedList<Coordinates> coors){
//        for (CSVRecord record : parser) {
//            coors.add(new Coordinates(
//                    Integer.parseInt(record.get("Id")),
//                    Double.parseDouble(record.get("Latitude")),
//                    Double.parseDouble(record.get("Longitude")),
//                    Source.CSV));
//        }
//    }
}
