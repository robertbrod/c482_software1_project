module robbrod.inventorymanagement {
    requires javafx.controls;
    requires javafx.fxml;


    opens robbrod.inventorymanagement to javafx.fxml;
    exports robbrod.inventorymanagement;
}