package com.nestorchacin.coordinates;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

public class CSVcoorParser implements Runnable {
    private static final String ID_NAME = "Id";
    private static final String LAT_NAME = "Latitude";
    private static final String LON_NAME = "Longitude";
    private final String fileToParse;
    private BlockingQueue<Coordinates> queue;

    public CSVcoorParser(String fileToParse, BlockingQueue<Coordinates> queue)  {
        this.fileToParse = fileToParse;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            File csvFile = new File(fileToParse);
            CSVParser csvParser = CSVParser.parse(csvFile, StandardCharsets.UTF_8, CSVFormat.EXCEL.withHeader());
            Iterator<CSVRecord> csvIterator = csvParser.iterator();
            Coordinates coordinate;
            CSVRecord record;
            while (csvIterator.hasNext()) {
                try {
                    record = csvIterator.next();
                    coordinate = new Coordinates(
                            Integer.parseInt(record.get(ID_NAME)),
                            Double.parseDouble(record.get(LAT_NAME)),
                            Double.parseDouble(record.get(LON_NAME)),
                            Source.CSV);

                    queue.put(coordinate);
                    System.out.println(Thread.currentThread().getName() + " added \"" + coordinate + "\" to queue, queue size: " + queue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            csvParser.close();
            System.out.println(Thread.currentThread().getName()+" finished");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
