package robbrod.inventorymanagement;

/**
 * Abstract class used to store, modify, and retrieve part data.
 *
 * @author Robert Brod
 */
public abstract class Part {

    /**
     * Used to generate and maintain unique part id
     */
    public static int instance = 0;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Part(String name, double price, int stock, int min, int max){
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
}
