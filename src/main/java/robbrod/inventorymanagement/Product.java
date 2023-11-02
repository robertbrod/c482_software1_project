package robbrod.inventorymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Product handles storage, modification, and retrieval of product data
 *
 * @author Robert Brod
 */
public class Product {

    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Used to generate and maintain unique product id
     */
    public static int instance = 1000;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(String name, double price, int stock, int min, int max){
        instance++;
        this.id = instance;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setStock(int stock){
        this.stock = stock;
    }

    public void setMin(int min){
        this.min = min;
    }

    public void setMax(int max){
        this.max = max;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public double getPrice(){
        return price;
    }

    public int getStock(){
        return stock;
    }

    public int getMin(){
        return min;
    }

    public int getMax(){
        return max;
    }

    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Removes associated part from product and sets highlightedAssociatedPart/highlightedPart static variables to null to avoid runtime error
     * from MainFormController attempting to access this data after deletion.
     *
     * @param selectedAssociatedPart associated part to be deleted from product
     * @return returns true if deletion successful
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){

        associatedParts.remove(selectedAssociatedPart);
        ModifyProductFormController.highlightedAssociatedPart = null;
        ModifyProductFormController.highlightedPart = null;

        return true;
    }

    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
