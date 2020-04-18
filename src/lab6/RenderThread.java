package lab6;

import java.util.List;

public class RenderThread implements Runnable {
    List<Cell> changingCells;

    RenderThread(List<Cell> changingCells) {
        this.changingCells = changingCells;
    }

    @Override
    public void run() {
        for (Cell cell : changingCells) {
            cell.updateState();
        }
        changingCells.clear();
    }
}
