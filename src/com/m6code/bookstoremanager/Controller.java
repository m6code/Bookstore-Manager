package com.m6code.bookstoremanager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.m6code.bookstoremanager.model.Book;
import com.m6code.bookstoremanager.model.Datasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private JFXTreeTableView<Book> treeTableView;

    @FXML
    private TreeTableColumn<Book, String> idCol;

    @FXML
    private TreeTableColumn<Book, String> titleCol;

    @FXML
    private TextField titleTF;

    @FXML
    private JFXButton updateBT;

    private ObservableList<Book> data = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(param -> param.getValue().getValue()._idProperty());
        titleCol.setCellValueFactory(param -> param.getValue().getValue().titleProperty());

        updateTableTableView();

    }

    private void updateTableTableView() {
        //query the books table from database;
        data = FXCollections.observableArrayList(Datasource.getInstance().queryBooks());
        // Show database query on UI TreeTableView
        TreeItem<Book> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);
    }

    @FXML
    public void addButtonClick(ActionEvent event) {
        String title = titleTF.getText();
        // Check if the text field if empty or only contains white spaces
        if (titleTF.getText().trim().isEmpty()) {
            alertUser("Please provide a title in the Text Field");
        } else {
            Datasource.getInstance().insertBook(title); // Insert book into the database
            reloadUI(); // reload UI
            clearTextField(); // Clear textField
        }
    }

    @FXML
    public void editButtonClick(ActionEvent event) {
    }

    @FXML
    public void clearButtonClick(ActionEvent event) {
        clearTextField();
    }

    @FXML
    public void deleteButtonClick(ActionEvent event) {
        try {
            int selectedIndex = treeTableView.getSelectionModel().getSelectedIndex();
            String id = treeTableView.getTreeItem(selectedIndex).getValue().get_id();
            Datasource.getInstance().deleteBook(id); // Delete selected book
            reloadUI(); // reload changes to UI
        } catch (NullPointerException e) {
            System.out.println("Error: "+e.getMessage());
            alertUser("Please select an entry first");
        }
    }

    @FXML
    public void updateButton(ActionEvent event) {
    }

    /* Reload the data in the TreeTableView*/
    private void reloadUI() {
        // Clear existing data
        data.clear();
        // reLoad data
        updateTableTableView();
    }

    /* Display an alert message to the UI */
    private void alertUser(String msg) {
        Alert alert = new Alert(Alert.AlertType.NONE, msg, ButtonType.OK);
        alert.show();
    }

    /* Clear the Text Field */
    private void clearTextField() {
        titleTF.clear();
    }
}

