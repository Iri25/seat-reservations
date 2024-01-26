package client.controller;

import client.controller.alert.MessageAlert;
import domain.Login;
import domain.Spectator;
import domain.UserStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import service.IServices;

import java.util.ArrayList;
import java.util.List;

public class LoginController {

    IServices service;

    public void setService(IServices service) throws Exception {
        this.service = service;

        initStatusModel();
    }

    @FXML
    private TextField textFieldUsername;
    @FXML
    private PasswordField passwordFieldPassword;
    @FXML
    private ComboBox<UserStatus> comboBoxStatus;

    @FXML
    private Button buttonSubmit;

    ObservableList<UserStatus> modelStatus = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initStatusComboBox();
    }

    private void initStatusComboBox() {
        comboBoxStatus.setItems(modelStatus);
    }
    private void initStatusModel() throws Exception {
        UserStatus statusAdmin = UserStatus.Admin;
        UserStatus statusSpectator= UserStatus.Spectator;

        List<UserStatus> status = new ArrayList<>();
        status.add(statusAdmin);
        status.add(statusSpectator);

        modelStatus.setAll(status);
    }

    @FXML
    public void handleLogin(ActionEvent actionEvent) throws Exception {
        String username = textFieldUsername.getText();
        String password = passwordFieldPassword.getText();
        UserStatus status = comboBoxStatus.getValue();

        if ((username.isEmpty()) || password.isEmpty() || status == null)
            MessageAlert.showErrorMessage(null, "Field empty!\n");
        else {
            Login loginUser = new Login(username, password, status);
            Login login = service.login(loginUser);
            if (login == null)
                MessageAlert.showErrorMessage(null, "Field invalid!\n");
            else {
                Stage stage = new Stage();

                Spectator spectator = service.getSpectator(login.getUsername());

                if (login.getStatus().toString().equals("Admin")) {
                    stage.setTitle("Admin");

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/views/AdminMenuView.fxml"));

                    Pane layout = loader.load();
                    stage.setScene(new Scene(layout));

                    AdminMenuController adminMenuController = loader.getController();
                    adminMenuController.setService(service, spectator);

                    stage.show();
                    ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                }

                if (login.getStatus().toString().equals("Spectator")) {
                    stage.setTitle("Spectator " + spectator.getUsername());

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/views/SpectatorMenuView.fxml"));

                    Pane layout = loader.load();
                    stage.setScene(new Scene(layout));

                    SpectatorMenuController spectatorMenuController = loader.getController();
                    spectatorMenuController.setService(service, spectator);

                    stage.show();
                    ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                }
            }
        }
    }
}
