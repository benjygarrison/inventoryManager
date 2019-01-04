package Model;
/**
 * @author ben garrison
 */
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product 
{     
    private final IntegerProperty productID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty max;
    private final IntegerProperty min;
    private final IntegerProperty inStock;
    
    private static ObservableList<Part> parts = FXCollections.observableArrayList();  
    
    public Product() 
    {
        productID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        inStock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }

    public IntegerProperty productIDInteger() 
    {
        return productID;
    }

    public StringProperty productNameString() 
    {
        return name;
    }

    public DoubleProperty productPriceDouble() 
    {
        return price;
    }

     public IntegerProperty productMaxInteger() 
    {
        return max;
    }

    public IntegerProperty productMinInteger() 
    {
        return min;
    }
  
    public IntegerProperty productInStockInteger() 
    {
        return inStock;
    }
    
    public int getProductID() 
    {
        return this.productID.get();
    }
    public void setProductID(int productID) 
    {
        this.productID.set(productID);
    }

    public String getProductName() 
    {
        return this.name.get();
    }
    public void setProductName(String name) 
    {
        this.name.set(name);
    }
    
    public double getProductPrice() 
    {
        return this.price.get();
    }
    public void setProductPrice(double price) 
    {
        this.price.set(price);
    }

    public int getProductMax() 
    {
        return this.max.get();
    }
    public void setProductMax(int max) 
    {
        this.max.set(max);
    }
   
    public int getProductMin() 
    {
        return this.min.get();
    }
    public void setProductMin(int min) 
    {
        this.min.set(min);
    }  
    
    public int getProductInStock() 
    {
        return this.inStock.get();
    }
    public void setProductInStock(int inStock) 
    {
        this.inStock.set(inStock);
    }
    
    public ObservableList getParts() 
    {
        return parts;
    }
    public void setParts(ObservableList<Part> parts) 
    {
        Product.parts = parts;
    }

    
}

