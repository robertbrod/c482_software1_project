package robbrod.inventorymanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * ModifyPartFormController is the controller class for the <code>modify-part-form.fxml</code> scene.
 *
 * @author Robert Brod
 */
public class ModifyPartFormController implements Initializable {

    /**
     * receives passed part from MainFormController to modify
     */
    public static Part passedPart = null;

    /**
     * receives passed part index from MainFormController
     */
    public static int passedPartIndex = -1;

    @FXML
    private TextField idText;
    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outsourcedRadio;
    @FXML
    private Label companyOrMachineLabel;
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
    private TextField companyOrMachineText;

    /**
     * Switches selection status of outsourced radio button when user selects inhouse radio button.
     * Also changes label on correlating text field.
     */
    public void inHouseAction(){
        outsourcedRadio.setSelected(false);
        companyOrMachineLabel.setText("Machine ID");
    }

    /**
     * Switches selection status of inhouse radio button when user selects outsourced radio button.
     * Also changes label on correlating text field.
     */
    public void outsourcedAction(){
        inHouseRadio.setSelected(false);
        companyOrMachineLabel.setText("Company Name");
    }

    /**
     * Creates new Part object and replaces the old Part object located at passedPartIndex. Tests all user entered data for improper or blank values.
     *
     * @param actionEvent save button fire, used to reference primary stage
     */
    public void saveButtonAction(ActionEvent actionEvent){
        Alert invalidName = new Alert(Alert.AlertType.ERROR, "Invalid name!");
        Alert invalidStock = new Alert(Alert.AlertType.ERROR, "Invalid inventory value!");
        Alert invalidPrice = new Alert(Alert.AlertType.ERROR, "Invalid price!");
        Alert invalidMax = new Alert(Alert.AlertType.ERROR, "Invalid max value!");
        Alert invalidMin = new Alert(Alert.AlertType.ERROR, "Invalid min value!");
        Alert invalidMachineId = new Alert(Alert.AlertType.ERROR, "Invalid machine ID!");
        Alert invalidCompanyName = new Alert(Alert.AlertType.ERROR, "Invalid company name!");

        String name;
        int stock;
        double price;
        int max;
        int min;
        int machineId;
        String companyName;

        name = nameText.getText();
        if(name.equals("")){
            invalidName.showAndWait();
            return;
        }

        try{
            max = Integer.parseInt(maxText.getText());
        }catch(Exception e){
            invalidMax.showAndWait();
            return;
        }

        if(max < 0){
            invalidMax.showAndWait();
            return;
        }

        try{
            min = Integer.parseInt(minText.getText());
        }catch(Exception e){
            invalidMin.showAndWait();
            return;
        }

        if(min < 0 || min > max){
            invalidMin.showAndWait();
            return;
        }

        try{
            stock = Integer.parseInt(stockText.getText());
        }catch(Exception e){
            invalidStock.showAndWait();
            return;
        }

        if(stock > max || stock < min){
            invalidStock.showAndWait();
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

        if(inHouseRadio.isSelected()){
            try{
                machineId = Integer.parseInt(companyOrMachineText.getText());
            }catch(Exception e){
                invalidMachineId.showAndWait();
                return;
            }

            if(machineId < 0){
                invalidMachineId.showAndWait();
                return;
            }

            if(passedPart instanceof InHouse){
                ((InHouse)passedPart).setMachineId(machineId);
                passedPart.setName(name);
                passedPart.setMax(max);
                passedPart.setMin(min);
                passedPart.setStock(stock);
                passedPart.setPrice(price);
            }else{
                Part part = new InHouse(name, price, stock, min, max, machineId);
                part.setId(passedPart.getId());
                InventoryManagerApplication.inventory.updatePart(passedPartIndex, part);
            }
        }else{
            companyName = companyOrMachineText.getText();
            if(companyName.equals("")){
                invalidCompanyName.showAndWait();
                return;
            }

            if(passedPart instanceof Outsourced){
                ((Outsourced)passedPart).setCompanyName(companyName);
                passedPart.setName(name);
                passedPart.setMax(max);
                passedPart.setMin(min);
                passedPart.setStock(stock);
                passedPart.setPrice(price);
            }else{
                Part part = new Outsourced(name, price, stock, min, max, companyName);
                part.setId(passedPart.getId());
                InventoryManagerApplication.inventory.updatePart(passedPartIndex, part);
            }
        }

        try{
            returnToMainForm(actionEvent);
        }catch(Exception ignored){}
    }

    /**
     * Returns user to <code>main-form.fxml</code> scene.
     *
     * @param actionEvent cancel button fire, used to reference primary stage
     */
    public void cancelButtonAction(ActionEvent actionEvent){
        try{
            returnToMainForm(actionEvent);
        }catch(Exception ignored){}
    }

    /**
     * Returns user to <code>main-form.fxml</code> scene.
     *
     * @param actionEvent save/cancel button fire, used to reference primary stage
     * @throws IOException necessary due to <code>.fxml</code> file being loaded from file
     */
    public void returnToMainForm(ActionEvent actionEvent) throws IOException {
        Parent mainFormParent = FXMLLoader.load(getClass().getResource("main-form.fxml"));
        Scene mainFormScene = new Scene(mainFormParent);

        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setScene(mainFormScene);
        stage.show();
    }

    /**
     * Populates TextFields with passed Part object data. Selects appropriate radio button based on object type and sets appropriate
     * machine id/company name TextField label.
     *
     * @param url location used to resolve relative paths for the root object
     * @param resourceBundle the resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idText.setText(Integer.toString(passedPart.getId()));
        nameText.setText(passedPart.getName());
        stockText.setText(Integer.toString(passedPart.getStock()));
        priceText.setText(Double.toString(passedPart.getPrice()));
        maxText.setText(Integer.toString(passedPart.getMax()));
        minText.setText(Integer.toString(passedPart.getMin()));

        if(passedPart instanceof InHouse){
            companyOrMachineLabel.setText("Machine ID");
            inHouseRadio.setSelected(true);
            outsourcedRadio.setSelected(false);
            companyOrMachineText.setText(Integer.toString(((InHouse) passedPart).getMachineId()));
        }else{
            companyOrMachineLabel.setText("Company Name");
            inHouseRadio.setSelected(false);
            outsourcedRadio.setSelected(true);
            companyOrMachineText.setText(((Outsourced) passedPart).getCompanyName());
        }
    }
}
