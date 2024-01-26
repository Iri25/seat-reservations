package client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import service.IServices;

import java.io.IOException;

public class AuthenticationController {
    IServices service;

    public void setService(IServices service){
        this.service = service;
    }

    public void handleLogin(ActionEvent actionEvent) throws Exception {
        Stage stage = new Stage();
        stage.setTitle("Sign in");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/LoginView.fxml"));

        Pane layout = loader.load();
        stage.setScene(new Scene(layout));

        LoginController loginController = loader.getController();
        loginController.setService(service);

        stage.show();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void handleRegister(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Sign up");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/RegisterView.fxml"));

        Pane layout = loader.load();
        stage.setScene(new Scene(layout));

        RegisterController registerController = loader.getController();
        registerController.setService(service);

        stage.show();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}
