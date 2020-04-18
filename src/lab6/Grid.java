package lab6;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    List<List<Cell>> cells = new ArrayList<>();
    Scene scene;
    AnchorPane cellGrid;
    List<Cell> changingCells = new ArrayList<>();
    Controller controller;

    Grid(int size, Scene scene, Controller controller) {
        this.scene = scene;
        this.cellGrid = (AnchorPane) scene.lookup("#cellGrid");
        this.controller = controller;
        this.setSize(size);
    }

    private void setSize(int newSize){
        int rectangleSize = Math.floorDiv(720, newSize);

        for (int i = 0; i < newSize; i++) {
            cells.add(new ArrayList<>());

            for (int j = 0; j < newSize; j++){
                cells.get(i).add(new Cell(i, j, cellGrid, rectangleSize, controller));
            }
        }

        for (List<Cell> rowCells : cells) {
            for (Cell cell : rowCells) {
                cell.initNeighbours(this);
            }
        }
    }

    void removeFromGrid() {
        for (List<Cell> rowCells : cells) {
            for (Cell cell : rowCells) {
                cell.removeFromGrid();
            }
        }
    }

    List<Cell> get(int i) {
        return cells.get(i);
    }

    int size() {
        return cells.size();
    }
}
