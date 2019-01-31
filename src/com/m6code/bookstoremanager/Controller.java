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
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

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
        data = FXCollections.observableArrayList(Datasource.getInstance().queryBooks());
        TreeItem<Book> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        treeTableView.setRoot(root);
        treeTableView.setShowRoot(false);
    }

    @FXML
    public void addButtonClick(ActionEvent event) {
    }

    @FXML
    public void editButtonClick(ActionEvent event) {
    }

    @FXML
    public void clearButtonClick(ActionEvent event) {
    }

    @FXML
    public void deleteButtonClick(ActionEvent event) {
    }

    @FXML
    public void updateButton(ActionEvent event) {
    }

    private void reloadUI(){
        data.clear();
        updateTableTableView();
    }
}

