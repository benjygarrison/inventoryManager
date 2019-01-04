package Model;
/**
 * @author benandhana
 */
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class InHousePart extends Part 
{    
    private final IntegerProperty machineIDPart;

    public InHousePart() 
    {
        super();
        machineIDPart = new SimpleIntegerProperty();
    }
    
    public int getMachineIDPart() 
    {
        return this.machineIDPart.get();
    }
    public void setMachineIDPart(int machineIDPart) 
    {
        this.machineIDPart.set(machineIDPart);
    }
}
