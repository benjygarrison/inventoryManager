package View_Controller;
/**
 * FXML Controller class
 *
 * @author ben garrison
 */
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import static Model.Inventory.getPartList;

public class AddProductController implements Initializable 
{    
    private String errorMessage = new String();
    private int ID;
    private final ObservableList<Part> currentParts = FXCollections.observableArrayList();
    
    @FXML private Label addProductID;
    @FXML private TextField addProductName;
    @FXML private TextField addProductInventory;
    @FXML private TextField addProductPrice;
    @FXML private TextField addProductMinimum;
    @FXML private TextField addProductMaximum;
    @FXML private TextField addProductSearch;
    
    @FXML private TableView<Part> addProductTable;
    @FXML private TableColumn<Part, Integer> addProductTableID;
    @FXML private TableColumn<Part, String> addProductTableName;
    @FXML private TableColumn<Part, Integer> addProductTableInventory;
    @FXML private TableColumn<Part, Double> addProductTablePrice;
    
    @FXML private TableView<Part> deleteProductTable;
    @FXML private TableColumn<Part, Integer> deleteProductTableID;
    @FXML private TableColumn<Part, String> deleteProductTableName;
    @FXML private TableColumn<Part, Integer> DeleteProductTableInventory;
    @FXML private TableColumn<Part, Double> deleteProductTablePrice;

    @FXML
    void search(ActionEvent event) 
    {
        String searchPart = addProductSearch.getText();
        int partIndex;
        
        if(Inventory.lookupPart(searchPart) < 0) 
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("No such part exists.");
            alert.showAndWait();
        }
        else 
        {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = getPartList().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            addProductTable.setItems(tempPartList);
        }
    }
    
    @FXML
    void clearSearch(ActionEvent event) 
    {
        updateAddPartTable();
        addProductSearch.setText("");
    }

    @FXML
    void add(ActionEvent event) 
    {
        Part part = addProductTable.getSelectionModel().getSelectedItem();
        
        if(part == null)
        {
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please select part to add.");
            alert.showAndWait();
    }
    else
        {    
        currentParts.add(part);
        updateDeletePartTable();
        }
    }
    
    public void updateAddPartTable() 
    {
        addProductTable.setItems(getPartList());
    }

    @FXML
    void delete(ActionEvent event) 
    {
        Part part = deleteProductTable.getSelectionModel().getSelectedItem();
        
        if(part == null)
        {
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please select part to delete.");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);        
            alert.setTitle("Deletion Confirmation");
            alert.setContentText("Delete " + part.getPartName() + " from parts?");
            Optional<ButtonType> result = alert.showAndWait();
        
            if(result.get() == ButtonType.OK) 
            {
                System.out.println("Deleted.");
                currentParts.remove(part);
            }
            else 
            {
                alert.close();
            }
        }
    }
        

    @FXML
    void addProductSave(ActionEvent event) throws IOException 
    {
        String productName = addProductName.getText();
        String productInv = addProductInventory.getText();
        String productPrice = addProductPrice.getText();
        String productMin = addProductMinimum.getText();
        String productMax = addProductMaximum.getText();

        try
        {
            errorMessage = validProduct(productName, Integer.parseInt(productMin), 
            Integer.parseInt(productMax), Integer.parseInt(productInv), 
            Double.parseDouble(productPrice), currentParts, errorMessage);
            
            if(errorMessage.length() > 0) 
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Could not add product.");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                errorMessage = "";
            }                     
            else if(addProductTable.getSelectionModel().getSelectedItem() == null)
            {
                Alert alert  = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText("Product does not contain parts. Please add parts.");
                alert.showAndWait();   
            }                 
            else 
            {
                System.out.println("Product name: " + productName);
                Product newProduct = new Product();
                newProduct.setProductID(ID);
                newProduct.setProductName(productName);
                newProduct.setProductInStock(Integer.parseInt(productInv));
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setParts(currentParts);
                Inventory.addProduct(newProduct);
                Parent addProductSaveParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch(NumberFormatException e) 
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void addProductCancel(ActionEvent event) throws IOException 
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancellation Confirmation");    
        alert.setContentText("Cancel adding new product?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) 
        {
            Parent addProductCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(addProductCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } 
        else 
        {
            alert.close();
        }
    }
    
    public void updateDeletePartTable() 
    {
        deleteProductTable.setItems(currentParts);
    }

    public static String validProduct(String name, int min, int max, int inv, 
            double price, ObservableList<Part> parts, String errorMessage) 
    {
        double partsTotal = 0.00;
         for(int i = 0; i < parts.size(); i++) 
         {
             partsTotal = partsTotal + parts.get(i).getPartPrice();
         }
        if(name == null || name.length() == 0) 
        {
            errorMessage = errorMessage + "Name field cannot be empty. ";
        }
        if(price <= 0) 
        {
            errorMessage = errorMessage + "The price must be greater than zero. ";
        }
        if(inv < 1) 
        {
            errorMessage = errorMessage + "Inventory must be greater than zero. ";
        }       
        if(max < min) 
        {
            errorMessage = errorMessage + "Minimum cannot be greater than maximum. ";
        }
        if(inv > max || inv < min) 
        {
            errorMessage = errorMessage + "Inventory may not fall outside the minimum and maximum. ";
        }
        if(partsTotal > price) 
        {
            errorMessage = errorMessage + "Price must be greater than cost of parts. ";
        }
        return errorMessage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        addProductTableID.setCellValueFactory(cellData -> cellData.getValue().partIDInteger().asObject());
        addProductTableName.setCellValueFactory(cellData -> cellData.getValue().partNameString());
        addProductTableInventory.setCellValueFactory(cellData -> cellData.getValue().partInStockInteger().asObject());
        addProductTablePrice.setCellValueFactory(cellData -> cellData.getValue().partCostDouble().asObject());
        deleteProductTableID.setCellValueFactory(cellData -> cellData.getValue().partIDInteger().asObject());
        deleteProductTableName.setCellValueFactory(cellData -> cellData.getValue().partNameString());
        DeleteProductTableInventory.setCellValueFactory(cellData -> cellData.getValue().partInStockInteger().asObject());
        deleteProductTablePrice.setCellValueFactory(cellData -> cellData.getValue().partCostDouble().asObject());
        updateAddPartTable();
        updateDeletePartTable();
        ID = Inventory.getProductCount();
        addProductID.setText("Auto Gen: " + ID);
    }  
}
