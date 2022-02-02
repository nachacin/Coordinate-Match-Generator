package com.nestorchacin.coordinates;

import java.util.concurrent.BlockingQueue;

public class CoordinatePrinter implements Runnable {
    private BlockingQueue<Coordinates> queue;

    public CoordinatePrinter(BlockingQueue<Coordinates> queue) {
        this.queue = queue;
    }

    public void run() {
        while(true) {
            Coordinates coordinate = queue.poll();
            if (coordinate == null && !ThreadController.areReadersAlive()) {
                return;
            }
            if (coordinate != null) {
                System.out.println(Thread.currentThread().getName()+" processing coordinate: "+coordinate);
            }
        }
    }
}
