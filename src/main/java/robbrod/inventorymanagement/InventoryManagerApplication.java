/**
 * FUTURE ENHANCEMENT
 * Connecting data to an actual database would obviously greatly improve the usability of the program as data would be stored
 * outside of program runtime.
 */

package robbrod.inventorymanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * InventoryManagerApplication is the class that creates the main stage data and launches the JavaFX application.
 *
 * @author Robert Brod
 */
public class InventoryManagerApplication extends Application {

    /**
     * static inventory object used to store and modify Product and Part object data throughout application
     */
    public static Inventory inventory = new Inventory();

    /**
     * Populates sample Part and Product object data, creates <a href="https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html">Stage</a>,
     * and loads <code>main-form.fxml</code> <a href="https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html">Scene</a>.
     *
     * @param stage primary stage for this application, onto which the application scene can be set
     * @throws IOException necessary due to <code>.fxml</code> file being loaded from file
     */
    @Override
    public void start(Stage stage) throws IOException {

        Part startingPart1 = new InHouse("Brakes", 15, 10, 0, 99, 1);
        Part startingPart2 = new InHouse("Wheel", 11, 16, 0, 99, 2);
        Part startingPart3 = new Outsourced("Seat", 15, 10, 0, 99, "Seats-r-us");

        inventory.addPart(startingPart1);
        inventory.addPart(startingPart2);
        inventory.addPart(startingPart3);

        Product startingProduct1 = new Product("Giant Bike", 299.99, 5, 0, 99);
        startingProduct1.addAssociatedPart(startingPart1);
        startingProduct1.addAssociatedPart(startingPart2);
        startingProduct1.addAssociatedPart(startingPart3);

        Product startingProduct2 = new Product("Tricycle", 99.99, 3, 0, 99);
        startingProduct2.addAssociatedPart(startingPart1);
        startingProduct2.addAssociatedPart(startingPart2);
        startingProduct2.addAssociatedPart(startingPart3);

        inventory.addProduct(startingProduct1);
        inventory.addProduct(startingProduct2);

        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagerApplication.class.getResource("main-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 840, 540);
        stage.setTitle("Inventory Management Application");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches standalone application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}