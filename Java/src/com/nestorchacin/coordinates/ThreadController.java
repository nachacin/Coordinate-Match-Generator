package com.nestorchacin.coordinates;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadController {
    private static final int QUEUE_SIZE = 5;
    private static BlockingQueue<Coordinates> coorQueue;
    private static CoordinatesMatcher coorMatcher;
    private static Thread CSV_read_thread;
    private static Thread JSON_read_thread;
    private static Thread matcher_thread;
    private static Thread coor_printer_thread;
    private static Thread coor_writer_thread;
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
        coorQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);
        createAndStartReaders(JSON_filenames[selection], CSV_filenames[selection]);
        createAndStartMatcher();
        createAndStartWriters(coorMatcher.getMatchQueue());

        try {
            CSV_read_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSON_read_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            matcher_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            coor_writer_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ThreadController Finished");
    }

    private static void createAndStartReaders(String JSON_filename, String CSV_filename) {
        JSONcoorParser jParser = new JSONcoorParser(URLDecoder.decode(ThreadController.class.getClassLoader().getResource(JSON_filename).getPath(), StandardCharsets.UTF_8), coorQueue);
        CSVcoorParser csvParser = new CSVcoorParser(URLDecoder.decode(Main.class.getClassLoader().getResource(CSV_filename).getPath(), StandardCharsets.UTF_8), coorQueue);
        JSON_read_thread = new Thread(jParser, "JSON-read");
        CSV_read_thread = new Thread(csvParser, "CSV-read");
        JSON_read_thread.start();
        CSV_read_thread.start();
    }
    private static void createAndStartMatcher() {
        coorMatcher = new CoordinatesMatcher(coorQueue);
        matcher_thread = new Thread(coorMatcher, "coor-matcher");
        matcher_thread.start();
    }
    private static void createAndStartPrinters(BlockingQueue<List<Coordinates>> matchQueue) {
        coor_printer_thread = new Thread(new CoordinatesPrinter(matchQueue), "Coor-Print");
        coor_printer_thread.start();
    }
    private static void createAndStartWriters(BlockingQueue<List<Coordinates>> matchQueue) {
        coor_writer_thread = new Thread(new CoordinatesWriter(matchQueue), "Coor-Write");
        coor_writer_thread.start();
    }

    public static boolean areReadersAlive() {
        if (CSV_read_thread.isAlive()) {
            return true;
        } else if (JSON_read_thread.isAlive()) {
            return true;
        } else {
            return false;
        }
    }
}

