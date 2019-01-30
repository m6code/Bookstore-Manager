package com.m6code.bookstoremanager;

import com.jfoenix.controls.JFXTreeTableView;
import com.m6code.bookstoremanager.model.Book;
import com.m6code.bookstoremanager.model.Datasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private TableView tableView;

    @FXML
    private TextField titleTF;

    public void  loadBooks(){
        Task<ObservableList<Book>> task = new GetAllBooksTask();
        tableView.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    @FXML
    public void addButton(){
        String title = titleTF.getText();

        if(title.equals("")){
            titleTF.setPromptText("Please Enter A Title");
            System.out.println("Error. Enter a valid Test");

        }else{
            Task<Boolean> addBookTask = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    return Datasource.getInstance().insertBook(title);
                }
            };
            addBookTask.setOnSucceeded(e -> {
                // TODO: Update UI with added data
                    tableView.refresh();
                    //clearFields();
            });

            new Thread(addBookTask).start();

        }
    }

    @FXML
    public void updateBook(){

    }


}

class GetAllBooksTask extends Task{
    @Override
    protected ObservableList<Book> call(){
        return FXCollections.observableArrayList
                (Datasource.getInstance().queryBooks());
    }
}
