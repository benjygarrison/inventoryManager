package View_Controller;
/**
 * FXML Controller class
 *
 * @author ben garrison
 */
import Model.Inventory;
import Model.Part;
import Model.Product;
import static Model.Inventory.removeProduct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;
import static Model.Inventory.getProductList;
import static Model.Inventory.getPartList;
import static Model.Inventory.deletePart;
import static Model.Inventory.productDeletionValidation;
import static Model.Inventory.partDeletionValidation;

public class MainScreenController implements Initializable 
{
    private static Part modifyPart;
    private static int modifyPartIndex;
    private static Product modifyProduct;
    private static int modifyProductIndex;
    
    @FXML private TableView<Part> partsTable;
    @FXML private TableColumn<Part, Integer> partsTableID;
    @FXML private TableColumn<Part, String> partsTableName;
    @FXML private TableColumn<Part, Integer> partsTableInventory;
    @FXML private TableColumn<Part, Double> partsTablePrice;
    
    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, Integer> productsTableID;
    @FXML private TableColumn<Product, String> productsTableName;
    @FXML private TableColumn<Product, Integer> productsTableInventory;
    @FXML private TableColumn<Product, Double> productsTablePrice;
    
    @FXML private TextField searchPartsText;
    @FXML private TextField searchProductsText;

    public static int partToModifyIndex() 
    {
        return modifyPartIndex;
    }

    public static int productToModifyIndex() 
    {
        return modifyProductIndex;
    }

    @FXML
    private void searchParts(ActionEvent event) 
    {
        String searchPart = searchPartsText.getText();
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
            partsTable.setItems(tempPartList);
        }
    }
    
    @FXML
    private void clearSearchParts(ActionEvent event) 
    {
        updatePartsTable();
        searchPartsText.setText("");
    }

    @FXML
    private void addParts(ActionEvent event) throws IOException 
    {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    @FXML
    private void modifyParts(ActionEvent event) throws IOException 
    {
        modifyPart = partsTable.getSelectionModel().getSelectedItem();
        modifyPartIndex = getPartList().indexOf(modifyPart);
        
        if (modifyPart == null)
        {
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please select part to modify.");
            alert.showAndWait();
        }
        else
        {
            Parent modifyPartParent = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
            Scene modifyPartScene = new Scene(modifyPartParent);
            Stage modifyPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyPartStage.setScene(modifyPartScene);
            modifyPartStage.show();
        }
    }
    
    @FXML
    private void deleteParts(ActionEvent event) 
    {
        Part part = partsTable.getSelectionModel().getSelectedItem();
        
        if(part == null)
        {
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please select part to delete.");
            alert.showAndWait();  
        }        
        else if(partDeletionValidation(part)) 
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("Part is in use. Part not deleted.");
            alert.showAndWait();
        }
        else 
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deletion Confirmation");
            alert.setContentText("Delete " + part.getPartName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) 
            {
                deletePart(part);
                updatePartsTable();
                System.out.println(part.getPartName() + " removed.");
            }
            else 
            {
                alert.close();
            }
        }
    }
    
    public void updatePartsTable() 
    {
        partsTable.setItems(getPartList());
    }

    @FXML
    private void searchProducts(ActionEvent event) 
    {
        String searchProduct = searchProductsText.getText();
        int prodIndex;
        
        if(Inventory.lookupProduct(searchProduct) < 0) 
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("No such product exists.");
            alert.showAndWait();
        }
        else 
        {
            prodIndex = Inventory.lookupProduct(searchProduct);
            Product tempProduct = Inventory.getProductList().get(prodIndex);
            ObservableList<Product> tempProductList = FXCollections.observableArrayList();
            tempProductList.add(tempProduct);
            productsTable.setItems(tempProductList);
        }
    }

    @FXML
    private void clearSearchProducts(ActionEvent event) 
    {
        updateProductsTable();
        searchProductsText.setText("");
    }
    
    @FXML
    private void addProducts(ActionEvent event) throws IOException 
    {
        Parent addProductParent = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene addProductScene = new Scene(addProductParent);
        Stage addProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addProductStage.setScene(addProductScene);
        addProductStage.show();
    }

    @FXML
    private void modifyProducts(ActionEvent event) throws IOException 
    {
        modifyProduct = productsTable.getSelectionModel().getSelectedItem();
        modifyProductIndex = getProductList().indexOf(modifyProduct);
        
        if (modifyProduct == null)
        {
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please select product to modify.");
            alert.showAndWait();
        }
        else
        {
        Parent modifyProductParent = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
        Scene modifyProductScene = new Scene(modifyProductParent);
        Stage modifyProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modifyProductStage.setScene(modifyProductScene);
        modifyProductStage.show();
        }
    }
    
    @FXML 
    private void deleteProducts(ActionEvent event) 
    {
        Product product = productsTable.getSelectionModel().getSelectedItem();
        
        if(product == null)
        {
            Alert alert  = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please select product to delete.");
            alert.showAndWait();  
        }        
        else if(productDeletionValidation(product)) 
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("Product contains parts. Product not deleted.");
            alert.showAndWait();
        } 
        else 
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);           
            alert.setTitle("Deletion Confirmation");
            alert.setContentText("Delete " + product.getProductName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            
            if(result.get() == ButtonType.OK) 
            {
                removeProduct(product);
                updateProductsTable();
                System.out.println(product.getProductName() + " removed.");
            } 
            else 
            {
                System.out.println(product.getProductName() + " not removed.");
            }
        }
    }
    
    public void updateProductsTable() 
    {
        productsTable.setItems(getProductList());
    }

    @FXML
    private void exit(ActionEvent event) 
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setContentText("Confirm exit?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK) 
        {
            System.exit(0);
        }
        else 
        {
            alert.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        partsTableID.setCellValueFactory(cellData -> cellData.getValue().partIDInteger().asObject());
        partsTableName.setCellValueFactory(cellData -> cellData.getValue().partNameString());
        partsTableInventory.setCellValueFactory(cellData -> cellData.getValue().partInStockInteger().asObject());
        partsTablePrice.setCellValueFactory(cellData -> cellData.getValue().partCostDouble().asObject());
        productsTableID.setCellValueFactory(cellData -> cellData.getValue().productIDInteger().asObject());
        productsTableName.setCellValueFactory(cellData -> cellData.getValue().productNameString());
        productsTableInventory.setCellValueFactory(cellData -> cellData.getValue().productInStockInteger().asObject());
        productsTablePrice.setCellValueFactory(cellData -> cellData.getValue().productPriceDouble().asObject());
        updatePartsTable();
        updateProductsTable();
    }
}

