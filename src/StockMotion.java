package src;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.Scene;
import javafx.stage.*;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;

/**
 * Created by joshuareno on 7/16/17.
 */

public class StockMotion extends Application{
    private static StockScreen stockScreen;
    private Controller controller;
    private MotionListener listener;
    private Scene scene;
    private Stage stage;

    /**
     * Starts the application
     * @param primaryStage
     */
    public void start(Stage primaryStage) {
        controller = new Controller();
        listener = new MotionListener();
        controller.addListener(listener);
        stockScreen = new StockScreen();
        stage = primaryStage;
        stage.setWidth(javafx.stage.Screen.getPrimary().getVisualBounds().getWidth());
        stage.setHeight(javafx.stage.Screen.getPrimary().getVisualBounds().getHeight());
        stage.setScene(startStockMotion());
        stage.show();
    }

    /**
     * Returns the StockScreen variable
     * @return StockScreen
     */
    public static StockScreen getStockScreen() {
        return stockScreen;
    }

    /**
     * Returns the scene after setting the actions of the buttons
     * @return Scene
     */
    public Scene startStockMotion() {
        stockScreen.getControlInformation().getAddStock().setOnMouseClicked((event) -> {
            TextInputDialog dialog = new TextInputDialog("Ticker Symbol");
            dialog.setTitle("Add Stock");
            dialog.setContentText("Enter ticker symbol");
            String string = dialog.showAndWait().get();
            try {
                stockScreen.getStockMenu().addStock(string); // this one
                StockScreen.setStock(stockScreen.getStockMenu().getStock(string));
                stockScreen.update();
            } catch (IOException | StockDoesNotExistException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ticker Symbol Error");
                alert.setContentText("The symbol you entered is not correct.");
                alert.showAndWait();
            }
            for (Button button: stockScreen.getStockMenu().getListOfButtons()) {
                int space = button.getText().indexOf(":");
                System.out.println(button.getText().substring(0, space));
                if (button.getText().substring(0, space).equals(string)) {
                    button.setOnMouseClicked((e) -> {
                        StockScreen.setStock(stockScreen.getStockMenu().getStock(string));
                        try {
                            stockScreen.update();
                        } catch (IOException | StockDoesNotExistException exception) {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Ticker Symbol Error");
                            alert.setContentText("The symbol you entered is not correct.");
                            alert.showAndWait();
                        }
                    });
                }
            }
        });
        stockScreen.getControlInformation().getDeleteStock().setOnMouseClicked((event) -> {
            TextInputDialog dialog = new TextInputDialog("Ticker Symbol");
            dialog.setTitle("Delete Stock");
            dialog.setContentText("Enter ticker symbol");
            String string = dialog.showAndWait().toString();
            try {
                stockScreen.getStockMenu().deleteStock(string);
                stockScreen.update();
            } catch (StockDoesNotExistException | IOException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ticker Symbol Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
        scene = new Scene(stockScreen);
        return scene;
    }

    /**
     * Launches args
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}