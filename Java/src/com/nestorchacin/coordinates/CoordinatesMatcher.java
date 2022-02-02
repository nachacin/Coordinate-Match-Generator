package com.nestorchacin.coordinates;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CoordinatesMatcher implements Runnable{
    private static final int QUEUE_SIZE = 5;
    private LinkedList<Coordinates> unmatched_CSV_coors;
    private LinkedList<Coordinates> unmatched_JSON_coors;
    private BlockingQueue<Coordinates> intakeQueue;
    private BlockingQueue<List<Coordinates>> matchQueue;

    public CoordinatesMatcher(BlockingQueue<Coordinates> intakeQueue) {
        this.intakeQueue = intakeQueue;
        this.unmatched_CSV_coors = new LinkedList<Coordinates>();
        this.unmatched_JSON_coors = new LinkedList<Coordinates>();
        this.matchQueue = new LinkedBlockingQueue<>(QUEUE_SIZE); // consider tweaking size here
    }

    public BlockingQueue<List<Coordinates>> getMatchQueue() {
        return matchQueue;
    }

    @Override
    public void run() {

        while(true) {
            Coordinates coordinate = intakeQueue.poll();
            if (coordinate == null && !ThreadController.areReadersAlive()) {
                System.out.println(Thread.currentThread().getName()+" finished");
                CoordinatesWriter.rake(unmatched_JSON_coors);
                CoordinatesWriter.rake(unmatched_CSV_coors);
                return;
            }
            if (coordinate != null) {
                if (coordinate.getSource() == Source.CSV) {
                    if (unmatched_JSON_coors.isEmpty()) {
                        unmatched_CSV_coors.add(coordinate);
                    } else {
                        for (Coordinates jCoordinate : unmatched_JSON_coors) {
                            if (coordinate.isMatch(jCoordinate)) {
                                List<Coordinates> match = Arrays.asList(coordinate, jCoordinate);
                                try {
                                    matchQueue.put(match);
                                    coordinate.setMatched();
                                    jCoordinate.setMatched();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                        // No matches found
                        unmatched_CSV_coors.add(coordinate);
                    }
                } else { // Coordinate source is JSON
                    if (unmatched_CSV_coors.isEmpty()) {
                        unmatched_JSON_coors.add(coordinate);
                    } else { // unmatched_CSV_coors has candidates
                        for (Coordinates csvCoordinate : unmatched_CSV_coors) {
                            if (coordinate.isMatch(csvCoordinate)) {
                                List<Coordinates> match = Arrays.asList(csvCoordinate, coordinate);
                                try {
                                    matchQueue.put(match);
                                    coordinate.setMatched();
                                    csvCoordinate.setMatched();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                        // No matches found
                        unmatched_JSON_coors.add(coordinate);
                    }
                }
            }
        }
    }
}
