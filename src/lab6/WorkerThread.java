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
                    int[] neighbourAmount = currCell.aliveNeighbours();

                    if (currCell.state != 0) {
                        if (neighbourAmount[currCell.state - 1] < 2 || neighbourAmount[currCell.state - 1] > 3) {
                            currCell.newState = 0;

                            synchronized (grid.changingCells) {
                                grid.changingCells.add(currCell);
                            }
                        }
                    } else {

                        for (int k = 0; k < neighbourAmount.length; k++) {
                            boolean isAnotherFaction = false;

                            if (neighbourAmount[k] == 3) {
                                for (int l = k + 1; l < neighbourAmount.length; l++) {
                                    if (neighbourAmount[l] == 3) {
                                        isAnotherFaction = true;    //if 2 factions can birth new cell, it doesn't birth
                                        break;
                                    }
                                }

                                if (isAnotherFaction) {
                                    break;
                                } else {
                                    currCell.newState = k + 1;      //1 - blue, 2 - green, etc

                                    synchronized (grid.changingCells) {
                                        grid.changingCells.add(currCell);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            try {
                barrier.await();
                if (!once) {
                    Thread.sleep(333);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            if (once) {
                break;
            }
        }
    }
}
