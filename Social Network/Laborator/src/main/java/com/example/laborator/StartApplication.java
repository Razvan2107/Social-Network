package com.example.laborator;

import domain.Utilizator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import repository.DBRepository;
import repository.Repository;
import service.Service;
import validator.UtilizatorValidator;

import java.io.IOException;

public class StartApplication extends Application {
    UtilizatorValidator val=new UtilizatorValidator();
    String url="jdbc:postgresql://localhost:5432/socialnetwork";
    String username = "postgres";
    String password="sabinarazvan";
    Repository<Long, Utilizator> repo;
    Service service;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException{
        repo=new DBRepository(url,username,password,val);
        service=new Service(repo);

        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException{
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("hello-view.fxml"));
        AnchorPane layout=loader.load();
        primaryStage.setScene(new Scene(layout));

        Controller controller=loader.getController();
        controller.setService(service);
    }
}
