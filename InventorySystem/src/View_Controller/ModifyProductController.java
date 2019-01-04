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
import static View_Controller.MainScreenController.productToModifyIndex;
import static Model.Inventory.getProductList;
import static Model.Inventory.getPartList;

public class ModifyProductController implements Initializable 
{
    private int ID;
    private String errorMessage = new String();  
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private final int productIndex = productToModifyIndex();
    
    @FXML private Label modifyProductID;
    @FXML private TextField modifyProductName;
    @FXML private TextField modifyProductInventory;
    @FXML private TextField modifyProductPrice;
    @FXML private TextField modifyProductMinimum;
    @FXML private TextField modifyProductMaximum;
    @FXML private TextField modifyProductSearch;
    
    @FXML private TableView<Part> addProductTable;
    @FXML private TableColumn<Part, Integer> addProductTableID;
    @FXML private TableColumn<Part, String> addProductTableName;
    @FXML private TableColumn<Part, Integer> addProductTableInventory;
    @FXML private TableColumn<Part, Double> addProductTablePrice;
    
    @FXML private TableView<Part> deleteProductTable;
    @FXML private TableColumn<Part, Integer> deleteProductTableID;
    @FXML private TableColumn<Part, String> deleteProductTableName;
    @FXML private TableColumn<Part, Integer> deleteProductTableInventory;
    @FXML private TableColumn<Part, Double> DeleteProductTablePrice;

    @FXML
    void search(ActionEvent event) 
    {
        String searchPart = modifyProductSearch.getText();
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
            Part tempPart = Inventory.getPartList().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            addProductTable.setItems(tempPartList);
        }
    }

    @FXML
    void clearSearch(ActionEvent event) 
    {
        updateAddPartsTable();
        modifyProductSearch.setText("");
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
        updateDeletePartsTable();
        }
    }

    public void updateAddPartsTable() 
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
        alert.setContentText("Delete " + part.getPartName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        
            if(result.get() == ButtonType.OK) 
            {
                currentParts.remove(part);
            }
            else 
            {
                alert.close();
            }
        }    
    }
  
    public void updateDeletePartsTable() 
    {
        deleteProductTable.setItems(currentParts);
    }
    
    @FXML
    private void modifyProductSave(ActionEvent event) throws IOException 
    {
        String productName = modifyProductName.getText();
        String productInv = modifyProductInventory.getText();
        String productPrice = modifyProductPrice.getText();
        String productMin = modifyProductMinimum.getText();
        String productMax = modifyProductMaximum.getText();

        try 
        {
            errorMessage = AddProductController.validProduct(productName, 
            Integer.parseInt(productMin), Integer.parseInt(productMax), 
            Integer.parseInt(productInv), Double.parseDouble(productPrice), 
            currentParts, errorMessage);
            
            if(errorMessage.length() > 0) 
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Product not modified.");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                errorMessage = "";
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
                Inventory.updateProduct(productIndex, newProduct);
                Parent modifyProductSaveParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSaveParent);
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
    private void modifyProductCancel(ActionEvent event) throws IOException 
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancellation Confirmation");
        alert.setContentText("Cancel modifying product?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) 
        {
            Parent modifyProductCancelParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(modifyProductCancelParent);
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
        Product product = getProductList().get(productIndex);
        ID = getProductList().get(productIndex).getProductID();
        modifyProductID.setText("Auto Gen: " + ID);
        modifyProductName.setText(product.getProductName());
        modifyProductInventory.setText(Integer.toString(product.getProductInStock()));
        modifyProductPrice.setText(Double.toString(product.getProductPrice()));
        modifyProductMinimum.setText(Integer.toString(product.getProductMin()));
        modifyProductMaximum.setText(Integer.toString(product.getProductMax()));
        currentParts = product.getParts();
        addProductTableID.setCellValueFactory(cellData -> cellData.getValue().partIDInteger().asObject());
        addProductTableName.setCellValueFactory(cellData -> cellData.getValue().partNameString());
        addProductTableInventory.setCellValueFactory(cellData -> cellData.getValue().partInStockInteger().asObject());
        addProductTablePrice.setCellValueFactory(cellData -> cellData.getValue().partCostDouble().asObject());
        deleteProductTableID.setCellValueFactory(cellData -> cellData.getValue().partIDInteger().asObject());
        deleteProductTableName.setCellValueFactory(cellData -> cellData.getValue().partNameString());
        deleteProductTableInventory.setCellValueFactory(cellData -> cellData.getValue().partInStockInteger().asObject());
        DeleteProductTablePrice.setCellValueFactory(cellData -> cellData.getValue().partCostDouble().asObject());
        updateAddPartsTable();
        updateDeletePartsTable();
    }   
}

