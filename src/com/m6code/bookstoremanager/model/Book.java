package com.m6code.bookstoremanager.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book extends RecursiveTreeObject<Book> {
    StringProperty _id;
    StringProperty title;

    public Book(String _id, String title){
        this._id= new SimpleStringProperty(_id);
        this.title = new SimpleStringProperty(title);
    }

    public String  get_id() {
        return _id.get();
    }

    public StringProperty _idProperty() {
        return _id;
    }

    public void set_id(String _id) {
        this._id.set(_id);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }
}
