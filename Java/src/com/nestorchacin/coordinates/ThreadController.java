package com.nestorchacin.coordinates;

import java.util.Collection;
import java.util.concurrent.BlockingDeque;

public class ThreadController {
    private static final int QUEUE_SIZE = 5;
    private static BlockingDeque<Coordinates> coorQueue;
    private static Thread CSV_read_thread;
    private static Thread JSON_read_thread;
    private static Thread coor_printer_thread;

    private static void createAndStartReaders() {

    }
}

