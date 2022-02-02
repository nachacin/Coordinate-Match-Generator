package com.nestorchacin.coordinates;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CoordinatesPrinter implements Runnable {
    private BlockingQueue<List<Coordinates>> queue;

    public CoordinatesPrinter(BlockingQueue<List<Coordinates>> queue) {
        this.queue = queue;
    }

    public static void rake(LinkedList<Coordinates> unmatched) {
        for (Coordinates coordinate : unmatched) {
            if (coordinate.getSource() == Source.CSV) {
                if (coordinate.getStat() == CoorMatchStat.UNMATCHED) {
                    System.out.println(Integer.toString(coordinate.getId()) + " : -1");
                }
            } else { // coordinate source is JSON
                if (coordinate.getStat() == CoorMatchStat.UNMATCHED) {
                    System.out.println("-1 : " + Integer.toString(coordinate.getId()));
                }
            }
        }
    }

    public void run() {
        while(true) {
            List<Coordinates> coordinate = queue.poll();
            if (coordinate == null && !ThreadController.areReadersAlive()) {
                return;
            }
            if (coordinate != null) {
                System.out.println(Thread.currentThread().getName()+" processing coordinate: "+coordinate);
            }
        }
    }
}
