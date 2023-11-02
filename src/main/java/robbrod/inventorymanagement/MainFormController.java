package robbrod.inventorymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * MainFormController is the controller class for the <code>main-form.fxml</code> scene.
 *
 * @author Robert Brod
 */
public class MainFormController implements Initializable {

    /**
     * Part object used for passing data between <code>main-form.fxml</code> scene and either <code>add-part-form.fxml</code> scene or
     * <code>modify-part-form.fxml</code> scene
     */
    public static Part passedPart = null;

    /**
     * Part object used to maintain which Part object was clicked on last in <code>partsTable</code> TableView
     */
    public static Part highlightedPart = null;

    /**
     * Product object used for passing data between <code>main-form.fxml</code> scene and either <code>add-product-form.fxml</code> scene
     * or <code>modify-product-form.fxml</code> scene.
     */
    public static Product passedProduct = null;

    /**
     * Product object used to maintain which Product object was clicked on last in <code>productsTable</code> TableView
     */
    public static Product highlightedProduct = null;

    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInventoryLevelCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, Integer> productIdCol;
    @FXML
    private TableColumn<Product, String> productNameCol;
    @FXML
    private TableColumn<Product, Integer> productInventoryLevelCol;
    @FXML
    private TableColumn<Product, Double> productPriceCol;

    @FXML
    private TextField partSearch;
    @FXML
    private TextField productSearch;

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
     * Modifies what data is displayed in <code>productsTable</code> TableView scene based on search value entered into associated <code>productSearch</code>> TextField.
     *
     * Shows all product data when set to blank.
     */
    public void productSearchAction(){
        ObservableList<Product> searchResults = FXCollections.observableArrayList();

        Alert invalidInput = new Alert(AlertType.ERROR, "Invalid input!");
        Alert partNotFound = new Alert(AlertType.ERROR, "Product not found!");

        String input = productSearch.getCharacters().toString();

        boolean isInt = true;
        int productId = 0;
        String productStr = null;

        try {
            productId = Integer.parseInt(input);
        } catch(Exception ignored){
            isInt = false;
        }

        if(!isInt){
            productStr = input;
        }

        if(productStr == null){
            if(productId < 1000 || productId > 1999){
                invalidInput.showAndWait();
            }else{
                Product product = InventoryManagerApplication.inventory.lookupProduct(productId);
                if(product != null){
                    searchResults.add(product);
                    productsTable.setItems(searchResults);
                } else{
                    partNotFound.showAndWait();
                }
            }
        } else{
            searchResults = InventoryManagerApplication.inventory.lookupProduct(productStr);
            if(searchResults.size() != 0){
                productsTable.setItems(searchResults);
            }else{
                partNotFound.showAndWait();
            }
        }

        if(input.equals("")){
            productsTable.setItems(InventoryManagerApplication.inventory.getAllProducts());
        }
    }

    /**
     * Sends user to <code>add-part-form.fxml</code> scene.
     *
     * @param actionEvent save button fire, used to reference primary stage
     * @throws IOException necessary due to <code>.fxml</code> file being loaded from file
     */
    public void partAddButtonAction(ActionEvent actionEvent) throws IOException {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("add-part-form.fxml"));
        Scene addPartScene = new Scene(addPartParent);

        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setScene(addPartScene);
        stage.show();
    }

    /**
     * Closes application
     *
     * @param actionEvent exit button fire, used to reference primary stage
     */
    public void exitButtonAction(ActionEvent actionEvent){
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.close();

    }

    /**
     * Deletes selected/highlighted part from inventory.
     */
    public void partDeleteButtonAction(){
        Alert noPartSelected = new Alert(AlertType.ERROR, "No part selected!");
        Alert confirmation = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete part?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if(highlightedPart != null){
                if(!InventoryManagerApplication.inventory.deletePart(highlightedPart)){
                    noPartSelected.showAndWait();
                }
            }else{
                noPartSelected.showAndWait();
            }
        }
    }

    /**
     * Deletes selected/highlighted product from inventory.
     */
    public void productDeleteButtonAction(){
        Alert noProductSelected = new Alert(AlertType.ERROR, "No product selected!");
        Alert productHasAssociatedParts = new Alert(AlertType.ERROR, "Product has associated parts!");
        Alert confirmation = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete part?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            if(highlightedProduct != null){
                if(highlightedProduct.getAllAssociatedParts().size() != 0){
                    productHasAssociatedParts.showAndWait();
                }else{
                    if(!InventoryManagerApplication.inventory.deleteProduct(highlightedProduct)){
                        noProductSelected.showAndWait();
                    }
                }
            }else{
                noProductSelected.showAndWait();
            }
        }
    }

    /**
     * Sends user to <code>modify-part-form.fxml</code> scene and passes selected/highlighted Part object to modify, as well as that parts
     * index in inventory list via static variable modifications in respective controller class.
     *
     * @param actionEvent modify button fire, used to reference primary stage
     * @throws IOException necessary due to <code>.fxml</code> file being loaded from file
     */
    public void partModifyButtonAction(ActionEvent actionEvent) throws IOException{
        Alert noPartSelected = new Alert(AlertType.ERROR, "No part selected!");

        if(highlightedPart != null){
            ModifyPartFormController.passedPart = highlightedPart;
            ModifyPartFormController.passedPartIndex = InventoryManagerApplication.inventory.getAllParts().indexOf(highlightedPart);

            Parent modifyPartParent = FXMLLoader.load(getClass().getResource("modify-part-form.fxml"));
            Scene modifyPartScene = new Scene(modifyPartParent);

            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            stage.setScene(modifyPartScene);
            stage.show();
        }else{
            noPartSelected.showAndWait();
        }
    }

    /**
     * Sends user to <code>modify-product-form.fxml</code> scene and passes selected/highlighted Product object to modify, as well as that products
     * index in inventory list via static variable modifications in respective controller class.
     *
     * @param actionEvent modify button fire, used to reference primary stage
     * @throws IOException necessary due to <code>.fxml</code> file being loaded from file
     */
    public void productModifyButtonAction(ActionEvent actionEvent) throws IOException{
        Alert noProductSelected = new Alert(AlertType.ERROR, "No product selected!");

        if(highlightedProduct != null){
            ModifyProductFormController.passedProduct = highlightedProduct;
            ModifyProductFormController.passedProductIndex = InventoryManagerApplication.inventory.getAllProducts().indexOf((highlightedProduct));

            Parent modifyProductParent = FXMLLoader.load(getClass().getResource("modify-product-form.fxml"));
            Scene modifyProductScene = new Scene(modifyProductParent);

            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

            stage.setScene(modifyProductScene);
            stage.show();
        }else{
            noProductSelected.showAndWait();
        }
    }

    /**
     * Sends user to <code>add-product-form.fxml</code> scene.
     *
     * @param actionEvent save button fire, used to reference primary stage
     * @throws IOException necessary due to <code>.fxml</code> file being loaded from file
     */
    public void productAddButtonAction(ActionEvent actionEvent) throws IOException{
        Parent addProductParent = FXMLLoader.load(getClass().getResource("add-product-form.fxml"));
        Scene addProductScene = new Scene(addProductParent);

        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setScene(addProductScene);
        stage.show();
    }

    /**
     * Adds any Part and/or Product information passed from other controller classes. Connects all parts in inventory to <code>partsTable</code> TableView,
     * and all products in inventory to <code>productsTable</code> TableView. Handles user input via mouse click to select rows in either table and populates <code>highlightedPart</code> and
     * <code>highlightedProduct</code> respectively.
     *
     * @param url location used to resolve relative paths for the root object
     * @param resourceBundle the resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(passedPart != null){
            InventoryManagerApplication.inventory.addPart(passedPart);
            passedPart = null;
        }

        if(passedProduct != null){
            InventoryManagerApplication.inventory.addProduct(passedProduct);
            passedProduct = null;
        }

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTable.setItems(InventoryManagerApplication.inventory.getAllParts());

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTable.setItems(InventoryManagerApplication.inventory.getAllProducts());

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

        productsTable.setOnMouseClicked(mouseEvent -> {
            try{
                Node node = ((Node)mouseEvent.getTarget()).getParent();
                TableRow row;

                if(node instanceof TableRow){
                    row = (TableRow) node;
                }else{
                    row = (TableRow) node.getParent();
                }

                if(row.getItem() != null){
                    highlightedProduct = (Product)row.getItem();
                }
            }catch(Exception ignored){}
        });
    }
}