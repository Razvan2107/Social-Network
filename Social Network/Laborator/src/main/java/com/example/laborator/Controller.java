package com.example.laborator;

import domain.Utilizator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import service.Service;
import utils.Observer;
import utils.events.UserChangeEvent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Controller implements Observer<UserChangeEvent> {
    Service service;
    ObservableList<Utilizator> model= FXCollections.observableArrayList();

    @FXML
    TableView<Utilizator> tableView;
//    @FXML
//    TableColumn<Utilizator,Long> tableColumnID;
    @FXML
    TableColumn<Utilizator,String> tableColumnFirstName;
    @FXML
    TableColumn<Utilizator,String> tableColumnLastName;

//    @FXML
//    TextField AdaugaId;
    @FXML
    TextField AdaugaFirstName;
    @FXML
    TextField AdaugaLastName;

    @FXML
    TextField StergeId;

    @FXML
    TextField UpdateId;
    @FXML
    TextField UpdateFirstName;
    @FXML
    TextField UpdateLastName;

    public void setService(Service  srv){
        service=srv;
        service.addObserver(this);
        initModel();
    }

    @FXML
    private void initialize(){
        //tableColumnID.setCellValueFactory(new PropertyValueFactory<Utilizator, Long>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("firstName"));
        //tableColumnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("lastName"));
        tableView.setItems(model);
        //tableView.setEditable(true);
    }

    private void initModel(){
        Iterable<Utilizator> users=service.getAll();
        List<Utilizator> usersList= StreamSupport.stream(users.spliterator(),false).collect(Collectors.toList());
        model.setAll(usersList);
    }

    @Override
    public void update(UserChangeEvent userChangeEvent){
        initModel();
    }

    @FXML
    public void addUser(ActionEvent actionEvent){
        //Long id=Long.parseLong(AdaugaId.getText());
        String firstName= AdaugaFirstName.getText();
        String lasttName= AdaugaLastName.getText();
        if(firstName!=null && lasttName!=null)
        {
            Optional<Utilizator> added=service.add_user(firstName,lasttName);
            if(added.isEmpty())
                MessageAlert.showMessage(null,Alert.AlertType.INFORMATION,"SAVE","Utilizatorul a fost adaugat cu succes!");
            else MessageAlert.showErrorMessage(null,"Acest utilizator nu poate fi adaugat!");
        }
        //AdaugaId.setText("");
        AdaugaFirstName.setText("");
        AdaugaLastName.setText("");
    }

    @FXML
    public void deleteUser(ActionEvent actionEvent){
        Long id=Long.parseLong(StergeId.getText());
        if(id!=null){
            Optional<Utilizator> deleted=service.delete_user(id);
            if(deleted.isEmpty())
                MessageAlert.showMessage(null,Alert.AlertType.INFORMATION,"DELETE","Utilizatorul a fost sters cu succes!");
            else MessageAlert.showErrorMessage(null,"Acest utilizator nu poate fi sters!");
        }
        StergeId.setText("");
    }

    @FXML
    public void updateUser(ActionEvent actionEvent){
        Long id=Long.parseLong(UpdateId.getText());
        String firstName= UpdateFirstName.getText();
        String lastName= UpdateLastName.getText();
        if(id!=null && firstName!=null && lastName!=null){
            Optional<Utilizator> updated=service.update_user(id,firstName,lastName);
            if(updated.isEmpty())
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"UPDATE","Utilizatorul a fost actualizat cu succes!");
            else MessageAlert.showErrorMessage(null,"Nu ati selectat niciun utilizator!");
        }
        UpdateId.setText("");
        UpdateFirstName.setText("");
        UpdateLastName.setText("");
    }
}
