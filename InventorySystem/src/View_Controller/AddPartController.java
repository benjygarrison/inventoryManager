package View_Controller;
/**
 * FXML Controller class
 *
 * @author ben garrison
 */
import Model.InHousePart;
import Model.Inventory;
import Model.outsourcedPart;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPartController implements Initializable 
{    
    private int ID;
    private boolean inHouse;
    private String errorMessage = new String();
        
    @FXML private RadioButton inHouseRadioButton;
    @FXML private RadioButton outsourcedRadioButton;
    @FXML private Label addPartID;
    @FXML private TextField addPartName;
    @FXML private TextField addPartInventory;
    @FXML private TextField addPartPrice;
    @FXML private TextField addPartMinimum;
    @FXML private TextField addPartMaximum;
    @FXML private Label addPartDynamicLabel;
    @FXML private TextField addPartDynamicTextField;

    
    @FXML
    void inHouseRadioSelected(ActionEvent event) 
    {
        inHouse = true;
        addPartDynamicLabel.setText("Machine ID");
        addPartDynamicTextField.setPromptText("Machine ID");
        outsourcedRadioButton.setSelected(false);
    }
    
    @FXML
    void outsourcedRadioSelected(ActionEvent event) 
    {
        inHouse = false;
        addPartDynamicLabel.setText("Company Name");
        addPartDynamicTextField.setPromptText("Company Name");
        inHouseRadioButton.setSelected(false);
    }

    @FXML
    void addPartSave(ActionEvent event) throws IOException 
    {
        String partName = addPartName.getText();
        String partInv = addPartInventory.getText();
        String partPrice = addPartPrice.getText();
        String partMin = addPartMinimum.getText();
        String partMax = addPartMaximum.getText();
        String partDyn = addPartDynamicTextField.getText();

        try 
        {
            errorMessage = validPart(partName, Integer.parseInt(partMin),             
            Integer.parseInt(partMax), Integer.parseInt(partInv), 
            Double.parseDouble(partPrice), errorMessage);
            
            if(errorMessage.length() > 0) 
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Could not add Part");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                errorMessage = "";
            }
            else 
            {
                if (inHouse) 
                {
                    System.out.println("Part name: " + partName);
                    InHousePart inPart = new InHousePart();
                    inPart.setPartID(ID);
                    inPart.setPartName(partName);
                    inPart.setPartPrice(Double.parseDouble(partPrice));
                    inPart.setPartInStock(Integer.parseInt(partInv));
                    inPart.setPartMin(Integer.parseInt(partMin));
                    inPart.setPartMax(Integer.parseInt(partMax));
                    inPart.setMachineIDPart(Integer.parseInt(partDyn));
                    Inventory.addPart(inPart);
                } 
                else 
                {
                    System.out.println("Part name: " + partName);
                    outsourcedPart outPart = new outsourcedPart();
                    outPart.setPartID(ID);
                    outPart.setPartName(partName);
                    outPart.setPartPrice(Double.parseDouble(partPrice));
                    outPart.setPartInStock(Integer.parseInt(partInv));
                    outPart.setPartMin(Integer.parseInt(partMin));
                    outPart.setPartMax(Integer.parseInt(partMax));
                    outPart.setCompanyName(partDyn);
                    Inventory.addPart(outPart);
                }
                Parent addPartSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addPartSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch(NumberFormatException e) 
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void addPartCancel(ActionEvent event) throws IOException 
    {   
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancellation Confirmation");
        alert.setContentText("Cancel adding new part?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) 
        {
            Parent addPartCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(addPartCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else 
        {
            alert.close();
        }
    }
public static String validPart(String name, int min, int max, int inv, 
    double price, String errorMessage)
    {
        if(name == null || name.length() == 0) 
        {
            errorMessage = errorMessage + "Name field cannot be empty. ";
        }
        if(price <= 0) 
        {
            errorMessage = errorMessage + "Price must be greater than zero. ";
        }
        if(inv < 1) 
        {
            errorMessage = errorMessage + "Inventory must be greater than zero. ";
        }                
        if(inv > max || inv < min) 
        {
            errorMessage = errorMessage + "Inventory may not fall outside minimum and maximum. ";
        }
        if(max < min) 
        {
            errorMessage = errorMessage + "Minimum cannot be greater than maximum. ";
        }
        return errorMessage;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        ID = Inventory.getPartCount();
        addPartID.setText("Auto Gen: " + ID);
    }
}
