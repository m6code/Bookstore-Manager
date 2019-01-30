package com.m6code.bookstoremanager;

import com.jfoenix.controls.JFXTreeTableView;
import com.m6code.bookstoremanager.model.Book;
import com.m6code.bookstoremanager.model.Datasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class Controller {

    @FXML
    private TableView tableView;

    public void  loadBooks(){
        Task<ObservableList<Book>> task = new GetAllBooksTask();
        tableView.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }


}

class GetAllBooksTask extends Task{
    @Override
    protected ObservableList<Book> call(){
        return FXCollections.observableArrayList
                (Datasource.getInstance().queryBooks());
    }
}
