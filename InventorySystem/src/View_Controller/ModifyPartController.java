package View_Controller;
/**
 * FXML Controller class
 *
 * @author ben garrison
 */
import Model.InHousePart;
import Model.Inventory;
import Model.outsourcedPart;
import Model.Part;
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
import static View_Controller.MainScreenController.partToModifyIndex;
import static Model.Inventory.getPartList;

public class ModifyPartController implements Initializable 
{
    private int ID;
    private boolean inHouse;
    int partIndex = partToModifyIndex();
    private String errorMessage = new String();
    
    @FXML private RadioButton inHouseRadioButton;
    @FXML private RadioButton outsourcedRadioButton;
    @FXML private Label modifyPartID;
    @FXML private TextField modifyPartName;
    @FXML private TextField modifyPartInventory;
    @FXML private TextField modifyPartPrice;
    @FXML private TextField modifyPartMinimum;
    @FXML private TextField modifyPartMaximum;
    @FXML private Label modifyPartDynamicLabel;
    @FXML private TextField modifyPartDynamicTextfield;

    @FXML
    void inHouseRadioSelected(ActionEvent event) 
    {
        inHouse = true;
        outsourcedRadioButton.setSelected(false);
        modifyPartDynamicLabel.setText("Machine ID");
        modifyPartDynamicTextfield.setText("");
        modifyPartDynamicTextfield.setPromptText("Machine ID");
    }

    @FXML
    void outsourcedRadioSelected(ActionEvent event) 
    {
        inHouse = false;
        inHouseRadioButton.setSelected(false);
        modifyPartDynamicLabel.setText("Company Name");
        modifyPartDynamicTextfield.setText("");
        modifyPartDynamicTextfield.setPromptText("Company Name");
    }

    @FXML
    void modifyPartSave(ActionEvent event) throws IOException 
    {
        String partName = modifyPartName.getText();
        String partInv = modifyPartInventory.getText();
        String partPrice = modifyPartPrice.getText();
        String partMin = modifyPartMinimum.getText();
        String partMax = modifyPartMaximum.getText();
        String partDyn = modifyPartDynamicTextfield.getText();

        try 
        {
            errorMessage = AddPartController.validPart(partName, Integer.parseInt(partMin), 
                    Integer.parseInt(partMax), Integer.parseInt(partInv), 
                    Double.parseDouble(partPrice), errorMessage);
            
            if(errorMessage.length() > 0) 
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Part could not be modified.");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                errorMessage = "";
            }
            else 
            {
                if(inHouse) 
                {
                    System.out.println("Part name: " + partName);
                    InHousePart inPart = new InHousePart();
                    inPart.setPartID(ID);
                    inPart.setPartName(partName);
                    inPart.setPartInStock(Integer.parseInt(partInv));
                    inPart.setPartPrice(Double.parseDouble(partPrice));
                    inPart.setPartMin(Integer.parseInt(partMin));
                    inPart.setPartMax(Integer.parseInt(partMax));
                    inPart.setMachineIDPart(Integer.parseInt(partDyn));
                    Inventory.updatePart(partIndex, inPart);
                }
                else 
                {
                    System.out.println("Part name: " + partName);
                    outsourcedPart outPart = new outsourcedPart();
                    outPart.setPartID(ID);
                    outPart.setPartName(partName);
                    outPart.setPartInStock(Integer.parseInt(partInv));
                    outPart.setPartPrice(Double.parseDouble(partPrice));
                    outPart.setPartMin(Integer.parseInt(partMin));
                    outPart.setPartMax(Integer.parseInt(partMax));
                    outPart.setCompanyName(partDyn);
                    Inventory.updatePart(partIndex, outPart);
                }
                Parent modifyProductSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSave);
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
    private void modifyPartCancel(ActionEvent event) throws IOException 
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancellation Confirmation");
        alert.setContentText("Cancel modifying part?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) 
        {
            Parent modifyPartCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(modifyPartCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else 
        {
            alert.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        Part part = getPartList().get(partIndex);
        ID = getPartList().get(partIndex).getPartID();
        modifyPartID.setText("Auto Gen: " + ID);
        modifyPartName.setText(part.getPartName());
        modifyPartInventory.setText(Integer.toString(part.getPartInStock()));
        modifyPartPrice.setText(Double.toString(part.getPartPrice()));
        modifyPartMinimum.setText(Integer.toString(part.getPartMin()));
        modifyPartMaximum.setText(Integer.toString(part.getPartMax()));
        
        if(part instanceof InHousePart) 
        {
            modifyPartDynamicLabel.setText("Machine ID");
            modifyPartDynamicTextfield.setText(Integer.toString(((InHousePart) 
            getPartList().get(partIndex)).getMachineIDPart()));
            inHouseRadioButton.setSelected(true);
        }
        else 
        {
            modifyPartDynamicLabel.setText("Company Name");
            modifyPartDynamicTextfield.setText(((outsourcedPart) 
            getPartList().get(partIndex)).getCompanyName());
            outsourcedRadioButton.setSelected(true);
        }
    }
}
