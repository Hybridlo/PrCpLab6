package lab6;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class WorkerThread implements Runnable {
    boolean once;
    int startX;
    int startY;
    int coverLength;
    Grid grid;
    CyclicBarrier barrier;

    WorkerThread(int startX, int startY, int coverLength, boolean once, Grid grid, CyclicBarrier barrier) {
        this.startX = startX;
        this.startY = startY;
        this.coverLength = coverLength;
        this.grid = grid;
        this.once = once;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            Cell currCell;

            for (int i = startX; i < startX + coverLength || i < grid.size(); i++) {
                for (int j = startY; j < startY + coverLength || j < grid.size(); j++) {
                    currCell = grid.get(i).get(j);
                    int neighbourAmount = currCell.aliveNeighbours();

                    if (currCell.state != 0) {
                        if (neighbourAmount < 2 || neighbourAmount > 3) {
                            currCell.newState = 0;
                            synchronized (grid.changingCells) {
                                grid.changingCells.add(currCell);
                            }
                        }
                    } else {
                        if (neighbourAmount == 3) {
                            currCell.newState = 1;
                            synchronized (grid.changingCells) {
                                grid.changingCells.add(currCell);
                            }
                        }
                    }
                }
            }

            try {
                barrier.await();
                Thread.sleep(333);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
