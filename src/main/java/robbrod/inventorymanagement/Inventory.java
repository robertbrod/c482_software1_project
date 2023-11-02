package robbrod.inventorymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory handles retrieval, modification, and storage of all Part and Product objects.
 *
 * @author Robert Brod
 */
public class Inventory {

    private final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public void addPart(Part newPart){
        allParts.add(newPart);
    }

    public void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    public Part lookupPart(int partId){
        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }

        return null;
    }

    public Product lookupProduct(int productId){
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }

        return null;
    }

    /**
     * @param partName partial/full part name
     * @return list of parts containing string data
     */
    public ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> searchResults = FXCollections.observableArrayList();

        for(Part part : allParts){
            if(part.getName().contains(partName)){
                searchResults.add(part);
            }

        }

        return searchResults;
    }

    /**
     * @param productName partial/full product name
     * @return list of products containing string data
     */
    public ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> searchResults = FXCollections.observableArrayList();

        for(Product product : allProducts){
            if(product.getName().contains(productName)){
                searchResults.add(product);
            }
        }

        return searchResults;
    }

    /**
     * @param index list index of part to be replaced
     * @param selectedPart new part to replace at index
     */
    public void updatePart(int index, Part selectedPart){
        InventoryManagerApplication.inventory.getAllParts().set(index, selectedPart);
    }

    /**
     * @param index list index of product to be replaced
     * @param selectedProduct new product to replace at index
     */
    public void updateProduct(int index, Product selectedProduct){
        InventoryManagerApplication.inventory.getAllProducts().set(index, selectedProduct);
    }

    /**
     * Deletes part from inventory and sets <code>highlightedPart</code> static variable to null to avoid runtime error from <code>MainFormController</code>
     * attempting to add a part to inventory that no longer exists.
     *
     * @param selectedPart part to be deleted
     * @return returns true if part was successfully removed
     */
    public boolean deletePart(Part selectedPart){
        for(Part part : allParts){
            if(part == selectedPart){
                allParts.remove(part);
                MainFormController.highlightedPart = null;
                return true;
            }
        }

        return false;
    }

    /**
     * Deletes product from inventory and sets  <code>highlightedProduct</code> static variable to null to avoid runtime error from <code>MainFormController</code>
     * attempting to add a product to inventory that no longer exists.
     *
     * @param selectedProduct product to be deleted
     * @return returns true if product was successfully removed
     */
    public boolean deleteProduct(Product selectedProduct){
        for(Product product : allProducts){
            if(product == selectedProduct){
                allProducts.remove(product);
                MainFormController.highlightedProduct = null;
                return true;
            }
        }

        return false;
    }

    public ObservableList<Part> getAllParts(){
        return allParts;
    }

    public ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
