package lab6;

import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    AnchorPane cellGrid;
    Rectangle rectangle = new Rectangle();
    int i;
    int j;
    int state = 0;      //0 - dead; 1-4 - alive, faction number
    int newState = 0;   //will be set to state on render
    List<Cell> neighbours = new ArrayList<>();

    Cell(int i, int j, AnchorPane cellGrid, int size, Controller controller) {
        this.cellGrid = cellGrid;
        this.i = i;
        this.j = j;

        int strokeWidth = 1;

        rectangle.setLayoutX(j * size);
        rectangle.setLayoutY(i * size);
        rectangle.setStroke(new Color(0.2, 0.2, 0.2, 1));   //Gray border
        rectangle.setFill(new Color(0, 0, 0, 1));           //Black fill(empty cell)
        rectangle.setStrokeWidth(strokeWidth);
        rectangle.setHeight(size - 2 * strokeWidth);
        rectangle.setWidth(size - 2 * strokeWidth);
        rectangle.setId("cell" + i + "_" + j);

        controller.addHandler(rectangle);

        cellGrid.getChildren().add(rectangle);
    }

    void initNeighbours(Grid grid) {
        if (i - 1 >= 0) {
            neighbours.add(grid.get(i - 1).get(j));     //up
        }

        if (i - 1 >= 0 && j + 1 < grid.size()) {
            neighbours.add(grid.get(i - 1).get(j + 1)); //up-right
        }

        if (j + 1 < grid.size()) {
            neighbours.add(grid.get(i).get(j + 1));     //right
        }

        if (i + 1 < grid.size() && j + 1 < grid.size()) {
            neighbours.add(grid.get(i + 1).get(j + 1)); //down-right
        }

        if (i + 1 < grid.size()) {
            neighbours.add(grid.get(i + 1).get(j));     //down
        }

        if (i + 1 < grid.size() && j - 1 >= 0) {
            neighbours.add(grid.get(i + 1).get(j - 1)); //down-left
        }

        if (j - 1 >= 0) {
            neighbours.add(grid.get(i).get(j - 1));     //left
        }

        if (i - 1 >= 0 && j - 1 >= 0) {
            neighbours.add(grid.get(i - 1).get(j - 1)); //up-left
        }
    }

    int aliveNeighbours() {
        int res = 0;

        for (Cell neighbour : neighbours) {
            if (neighbour.state != 0) {
                res++;
            }
        }

        return res;
    }

    void nextState() {
        state++;

        if (state > 4) {
            state = 0;
        }

        changeColor();
    }

    void updateState() {
        state = newState;
        changeColor();
    }

    void changeColor() {
        switch (state) {
            case 0:
                rectangle.setFill(new Color(0, 0, 0, 1));
                break;
            case 1:
                rectangle.setFill(new Color(0, 0, 1, 1));
                break;
            case 2:
                rectangle.setFill(new Color(0, 1, 0, 1));
                break;
            case 3:
                rectangle.setFill(new Color(1, 0, 0, 1));
                break;
            case 4:
                rectangle.setFill(new Color(1, 1, 0, 1));
                break;
        }
    }

    void removeFromGrid() {
        cellGrid.getChildren().remove(rectangle);
    }
}
