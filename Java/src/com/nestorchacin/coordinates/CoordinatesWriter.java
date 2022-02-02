package com.nestorchacin.coordinates;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CoordinatesWriter implements Runnable {
    private static final String OUTPUT_FILENAME = "output.txt";
    private BlockingQueue<List<Coordinates>> queue;

    public CoordinatesWriter(BlockingQueue<List<Coordinates>> queue) {
        this.queue = queue;
    }
    public static void rake(LinkedList<Coordinates> unmatched) {
        try {
            FileWriter fileWriter = new FileWriter(new File(OUTPUT_FILENAME), true);
            PrintWriter outputWriter = new PrintWriter(fileWriter);
            for (Coordinates coordinate : unmatched) {
                if (coordinate.getSource() == Source.CSV) {
                    if (coordinate.getStat() == CoorMatchStat.UNMATCHED) {
                        outputWriter.println(Integer.toString(coordinate.getId()) + ":-1");
                    }
                } else { // coordinate source is JSON
                    if (coordinate.getStat() == CoorMatchStat.UNMATCHED) {
                        outputWriter.println("-1:" + Integer.toString(coordinate.getId()));
                    }
                }
            }
            outputWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run () {
        try {
            PrintStream outputWriter = new PrintStream(new File(OUTPUT_FILENAME));
            while (true) {
                List<Coordinates> match = queue.poll();
                if (match == null && !ThreadController.areReadersAlive()) {
                    outputWriter.close();
                    System.out.println(Thread.currentThread().getName()+" has finished");
                    return;
                }
                if (match != null) {
                    outputWriter.println(Integer.toString(match.get(0).getId()) + ":" +Integer.toString(match.get(1).getId()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
