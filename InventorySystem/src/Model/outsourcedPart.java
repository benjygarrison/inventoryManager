package Model;
/**
 * @author ben garrison
 */
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class outsourcedPart extends Part 
{
    private final StringProperty partCompanyName;

    public outsourcedPart() 
    {
        super();
        partCompanyName = new SimpleStringProperty();
    }

    public String getCompanyName() 
    {
        return this.partCompanyName.get();
    }

    public void setCompanyName(String partCompanyName) 
    {
        this.partCompanyName.set(partCompanyName);
    }
}
