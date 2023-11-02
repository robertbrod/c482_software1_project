package robbrod.inventorymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AddProductFormController is the controller class for the <code>add-product-form.fxml</code> scene.
 *
 * @author Robert Brod
 */
public class AddProductFormController implements Initializable {

    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Stores the most recent part clicked on
     */
    private Part highlightedPart = null;

    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableColumn<Part, Integer> partsTableIdCol;
    @FXML
    private TableColumn<Part, String> partsTableNameCol;
    @FXML
    private TableColumn<Part, Integer> partsTableStockCol;
    @FXML
    private TableColumn<Part, Double> partsTablePriceCol;

    @FXML
    private TableView<Part> associatedPartsTable;
    @FXML
    private TableColumn<Part, Integer> associatedPartsIdCol;
    @FXML
    private TableColumn<Part, String> associatedPartsNameCol;
    @FXML
    private TableColumn<Part, Integer> associatedPartsStockCol;
    @FXML
    private TableColumn<Part, Double> associatedPartsPriceCol;

    @FXML
    private TextField nameText;
    @FXML
    private TextField stockText;
    @FXML
    private TextField priceText;
    @FXML
    private TextField maxText;
    @FXML
    private TextField minText;
    @FXML
    private TextField partSearch;

    /**
     * Adds part to product associated parts. Checks to ensure part is not already added, and that there is a valid part selected
     * via mouse click.
     */
    public void partAddButtonAction(){
        Alert partAlreadyAdded = new Alert(AlertType.ERROR, "Part already added!");
        Alert noPartSelected = new Alert(AlertType.ERROR, "No part selected!");

        if(highlightedPart == null){
            noPartSelected.showAndWait();
        }else{
            for(Part part : associatedParts){
                if(part == highlightedPart){
                    partAlreadyAdded.showAndWait();
                    return;
                }
            }
            associatedParts.add(highlightedPart);
        }
    }

    /**
     * Modifies what data is displayed in <code>partsTable</code> TableView scene based on search value entered into associated <code>partSearch</code>> TextField.
     *
     * Shows all part data when set to blank.
     */
    public void partSearchAction(){
        ObservableList<Part> searchResults = FXCollections.observableArrayList();

        Alert invalidInput = new Alert(AlertType.ERROR, "Invalid input!");
        Alert partNotFound = new Alert(AlertType.ERROR, "Part not found!");

        String input = partSearch.getCharacters().toString();

        boolean isInt = true;
        int partId = 0;
        String partStr = null;

        try {
            partId = Integer.parseInt(input);
        } catch(Exception ignored){
            isInt = false;
        }

        if(!isInt){
            partStr = input;
        }

        if(partStr == null){
            if(partId < 0 || partId > 999){
                invalidInput.showAndWait();
            }else{
                Part part = InventoryManagerApplication.inventory.lookupPart(partId);
                if(part != null){
                    searchResults.add(part);
                    partsTable.setItems(searchResults);
                } else{
                    partNotFound.showAndWait();
                }
            }
        } else{
            searchResults = InventoryManagerApplication.inventory.lookupPart(partStr);
            if(searchResults.size() != 0){
                partsTable.setItems(searchResults);
            }else{
                partNotFound.showAndWait();
            }
        }

        if(input.equals("")){
            partsTable.setItems(InventoryManagerApplication.inventory.getAllParts());
        }
    }

    /**
     * Creates new Product object to be added to inventory. Test all user entered data for improper or blank values.
     *
     * @param actionEvent save button fire, used to reference primary stage
     */
    public void saveButtonAction(ActionEvent actionEvent){
        Alert invalidName = new Alert(AlertType.ERROR, "Invalid name!");
        Alert invalidInv = new Alert(AlertType.ERROR, "Invalid inventory value!");
        Alert invalidPrice = new Alert(AlertType.ERROR, "Invalid price value!");
        Alert invalidMax = new Alert(AlertType.ERROR, "Invalid max value!");
        Alert invalidMin = new Alert(AlertType.ERROR, "Invalid min value!");

        String name;
        int stock;
        double price;
        int max = -1;
        int min = -1;

        name = nameText.getText();
        if(name.equals("")){
            invalidName.showAndWait();
            return;
        }

        try{
            max = Integer.parseInt(maxText.getText());
        }catch(Exception e){
            invalidMax.showAndWait();
        }

        try{
            min = Integer.parseInt(minText.getText());
        }catch(Exception e){
            invalidMin.showAndWait();
        }

        if(min < 0){
            invalidMin.showAndWait();
            return;
        }

        if(min > max){
            invalidMin.showAndWait();
        }

        try{
            stock = Integer.parseInt(stockText.getText());
        }catch(Exception e){
            invalidInv.showAndWait();
            return;
        }

        if(stock < min || stock > max){
            invalidInv.showAndWait();
            return;
        }

        try{
            price = Double.parseDouble(priceText.getText());
        }catch(Exception e){
            invalidPrice.showAndWait();
            return;
        }

        if(price < 0){
            invalidPrice.showAndWait();
            return;
        }

        ArrayList<Part> associatedPartsArray = new ArrayList<>(associatedParts);

        Product product = new Product(name, price, stock, min, max);
        for(Part part : associatedPartsArray){
            product.addAssociatedPart(part);
        }

        MainFormController.passedProduct = product;

        try{
            returnToMainForm(actionEvent);
        }catch(Exception ignored){}
    }

    /**
     * Removes part from product associated parts. Checks to ensure there is a valid part selected via mouse click.
     */
    public void removeButtonAction(){
        Alert noPartSelected = new Alert(AlertType.ERROR, "No part selected!");
        Alert confirmation = new Alert(AlertType.CONFIRMATION, "Are you sure you want to remove associated part?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if(highlightedPart == null){
                noPartSelected.showAndWait();
                return;
            }

            for(Part part : associatedParts){
                if(part == highlightedPart){
                    associatedParts.remove(part);
                    return;
                }
            }
        }
    }

    /**
     * Returns user to <code>main-form.fxml</code> scene.
     *
     * @param actionEvent cancel button fire, used to reference primary stage
     */

    public void cancelButtonAction(ActionEvent actionEvent){
        try{
            returnToMainForm(actionEvent);
        }
        catch(Exception ignored){}
    }

    /**
     * Returns user to <code>main-form.fxml</code> scene.
     *
     * @param actionEvent cancel/save button fire, used to reference primary stage
     * @throws IOException necessary due to <code>.fxml</code> file being loaded from file
     */
    public void returnToMainForm(ActionEvent actionEvent) throws IOException{
        Parent mainFormParent = FXMLLoader.load(getClass().getResource("main-form.fxml"));
        Scene mainFormScene = new Scene(mainFormParent);

        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setScene(mainFormScene);
        stage.show();
    }

    /**
     * Connects all parts in inventory to <code>partsTable</code> TableView, and all associated parts of Product to <code>associatedPartsTable</code>
     * TableView. Handles user input via mouse click to select rows in either table and populates <code>highlightedPart</code> with that data.
     *
     * @param url location used to resolve relative paths for the root object
     * @param resourceBundle the resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsTableIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsTableNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsTableStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsTablePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTable.setItems(InventoryManagerApplication.inventory.getAllParts());

        associatedPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsTable.setItems(associatedParts);

        partsTable.setOnMouseClicked(mouseEvent -> {
            try{
                Node node = ((Node)mouseEvent.getTarget()).getParent();
                TableRow row;

                if(node instanceof TableRow){
                    row = (TableRow) node;
                }else{
                    row = (TableRow) node.getParent();
                }

                if(row.getItem() != null){
                    highlightedPart = (Part)row.getItem();
                }
            }catch(Exception ignored){}
        });
    }
}
