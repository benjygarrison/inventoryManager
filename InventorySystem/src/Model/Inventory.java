package Model;
/**
 * @author ben garrison
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory 
{    
    private static final ObservableList<Product> productList = FXCollections.observableArrayList();
    private static int productCount = 0;
    private static final ObservableList<Part> partList = FXCollections.observableArrayList();
    private static int partCount = 0;
        
    public static ObservableList<Product> getProductList() 
    {
        return productList;
    }
    
    public static void addProduct(Product product) 
    {
        productList.add(product);
    }
        
    public static void removeProduct(Product product) 
    {
        productList.remove(product);
    }
     
    public static void updateProduct(int index, Product product) 
    {
        productList.set(index, product);
    }

    public static int getProductCount() 
    {
        productCount++;
        return productCount;
    }
        
    public static int lookupProduct(String searchItem) 
    {
        int i = 0;
        boolean inList = false;
               
        if(isInteger(searchItem)) 
        {
            for(Product product : productList) 
            {
                if(Integer.parseInt(searchItem) == product.getProductID()) 
                {
                    i++;
                    inList = true;
                }
            }
        }
        else 
        {
            for(Product product : productList) 
            {
                if(!searchItem.equals(product.getProductName())) 
                {
                } else {
                    i++;
                    inList = true;
                }
            }
        }
        if(inList) 
        {
            return i;
        }
        else 
        {
            System.out.println("No such product exists.");
            return -1;
        }
    }
      
    public static boolean isInteger(String input) 
    {
        try 
        {
            Integer.parseInt(input);
            return true;
        }
        catch(NumberFormatException e) 
        {
            return false;
        }
    }
    
    public static boolean productDeletionValidation(Product product) 
    {
        boolean exists = true;
        int productID = product.getProductID();
        //
        //I tried to use only enhanced for loops, but struggled to make one that 
        //worked effectively in this case, so I settled for a standard for loop.
        //
        for(int i=0; i < productList.size(); i++) 
        {
            if((productList.get(i).getProductID() == productID) 
            && (productList.get(i).getParts().isEmpty())) 
            {                               
                exists = false;
            }           
        }
        return exists;
    }
        
    public static int lookupPart(String searchItem) 
    {
        int i = 0;
        boolean inList = false;        
        
        if(isInteger(searchItem)) 
        {
            for(Part part : partList) 
            {
                if(Integer.parseInt(searchItem) == part.getPartID()) 
                {
                    i++;
                    inList = true;
                }
            }
        }
        else 
        {
            for(Part part : partList) 
            {
                searchItem = searchItem.toUpperCase();
                
                if(searchItem.equals(part.getPartName().toUpperCase())) 
                {
                    i++;
                    inList = true;
                }
            }
        }
        if(inList) 
        {
            return i;
        }
        else 
        {
            System.out.println("No such part exists.");
            return -1;
        }
    }   
       
    public static ObservableList<Part> getPartList() 
    {
        return partList;
    }
        
    public static void addPart(Part part) 
    {
        partList.add(part);
    }
       
    public static void deletePart(Part part) 
    {
        partList.remove(part);
    }
        
    public static void updatePart(int index, Part part) 
    {
        partList.set(index, part);
    }
        
    public static int getPartCount() 
    {
        partCount++;
        return partCount;
    }
        
    public static boolean partDeletionValidation(Part part) 
    {
        int i = 0;
        boolean inList = false;
        
        for(Product product : productList) 
        {
            if(product.getParts().contains(part)) 
            {
                i++;
                inList = true;
            }
        }
        return inList;
    }    
}