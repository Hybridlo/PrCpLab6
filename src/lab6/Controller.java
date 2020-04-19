package lab6;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class Controller {

    public TextField gridSizeTextField;
    public TextField threadAmountTextField;
    public Label threadAmountLabel;
    public Button startButton;
    public Button pauseButton;
    public Button stepButton;
    public Button clearAllButton;

    private Grid grid;
    private List<Thread> threads = new ArrayList<>();
    private int sqrtAmount = 2;

    public void resizeField() {
        if (grid != null) {
            grid.removeFromGrid();
        } else {
            startButton.setDisable(false);
            stepButton.setDisable(false);
            clearAllButton.setDisable(false);
        }
        grid = new Grid(Integer.parseInt(gridSizeTextField.getText()), gridSizeTextField.getScene(), this);

    }

    public void changeThreadAmount() {
        sqrtAmount = Integer.parseInt(threadAmountTextField.getText());
        threadAmountLabel.setText("^2 = " + sqrtAmount * sqrtAmount);
    }

    public void startLife() {
        startButton.setDisable(true);
        stepButton.setDisable(true);
        clearAllButton.setDisable(true);
        pauseButton.setDisable(false);

        launchThreads(false);
    }

    public void pauseLife() {
        pauseButton.setDisable(true);

        for (Thread thread : threads) {
            thread.interrupt();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        startButton.setDisable(false);
        stepButton.setDisable(false);
        clearAllButton.setDisable(false);
    }

    public void stepLife() {
        launchThreads(true);

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void launchThreads(boolean once) {
        CyclicBarrier barrier = new CyclicBarrier(sqrtAmount * sqrtAmount, new RenderThread(grid.changingCells));
        int coverLength = Math.floorDiv(grid.size(), sqrtAmount);

        for (int i = 0; i < sqrtAmount; i++) {
            for (int j = 0; j < sqrtAmount; j++) {
                Thread newThread = new Thread(
                        new WorkerThread(
                                i * coverLength,
                                j * coverLength,
                                coverLength,
                                once,
                                grid,
                                barrier
                        )
                );

                newThread.start();

                threads.add(newThread);
            }
        }
    }

    public void clearGrid() {
        grid.clearGrid();
    }

    void addHandler(Rectangle rectangle) {
        rectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, new RectClickHandler());
    }

    private class RectClickHandler implements EventHandler<Event> {
        @Override
        public void handle(Event evt) {
            if (startButton.isDisable()) {
                return;                     //don't allow change of color in simulation
            }

            MouseEvent me = (MouseEvent) evt;

            String id = ((Rectangle) evt.getSource()).getId();
            int delimIndex = id.indexOf('_');
            int i = Integer.parseInt(id.substring(4, delimIndex));  //4 is length of word "ceil" in id
            int j = Integer.parseInt(id.substring(delimIndex + 1));

            if (me.getButton() == MouseButton.PRIMARY) {     //on left click - next state
                grid.get(i).get(j).nextState();
            }

            if (me.getButton() == MouseButton.SECONDARY) {   //on right click - reset to dead
                grid.get(i).get(j).clearState();
            }
        }
    }
}
