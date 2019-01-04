package Model;
/**
 * @author ben garrison
 */
import javafx.beans.property.*;

public abstract class Part 
{
    private final IntegerProperty partID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty max;
    private final IntegerProperty min;
    private final IntegerProperty inStock;
        
    public Part() 
    {
        partID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        max = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();    
        inStock = new SimpleIntegerProperty();
    }

    public IntegerProperty partIDInteger() 
    {
        return partID;
    }
    
    public StringProperty partNameString() 
    {
        return name;
    }

    public DoubleProperty partCostDouble() 
    {
        return price;
    }

    public IntegerProperty partMaxInteger() 
    {
        return max;
    }

    public IntegerProperty partMinInteger() 
    {
        return min;
    }

    public IntegerProperty partInStockInteger() 
    {
        return inStock;
    }
    
    public int getPartID() 
    {
        return this.partID.get();
    }    
    public void setPartID(int partID) 
    {
        this.partID.set(partID);
    }
        
    public String getPartName() 
    {
        return this.name.get();
    }    
    public void setPartName(String name) 
    {
        this.name.set(name);
    }
    
    public double getPartPrice() 
    {
        return this.price.get();
    }
    public void setPartPrice(double price) 
    {
        this.price.set(price);
    }
        
    public int getPartMax() 
    {
        return this.max.get();
    }    
    public void setPartMax(int max) 
    {
        this.max.set(max);
    }

    public int getPartMin() 
    {
        return this.min.get();
    }    
    public void setPartMin(int min) 
    {
        this.min.set(min);
    }
        
    public int getPartInStock() 
    {
        return this.inStock.get();
    }   
    public void setPartInStock(int inStock) 
    {
        this.inStock.set(inStock);
    }          
}
