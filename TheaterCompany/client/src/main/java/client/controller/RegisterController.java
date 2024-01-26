package client.controller;

import client.controller.alert.MessageAlert;
import domain.Login;
import domain.Spectator;
import domain.UserStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import service.IServices;

public class RegisterController {

    IServices service;
    public void setService(IServices service) {

        this.service = service;
    }

    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private PasswordField passwordFieldPassword;
    @FXML
    private PasswordField passwordFieldConfirmPassword;

    @FXML
    private Button buttonSubmit;

    @FXML
    public void handleRegister(ActionEvent actionEvent) throws Exception {
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String email = textFieldEmail.getText();
        String username = textFieldUsername.getText();
        String password = passwordFieldPassword.getText();
        String confirmPassword = passwordFieldConfirmPassword.getText();
        UserStatus status = UserStatus.Spectator;

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty())
            MessageAlert.showErrorMessage(null, "Field empty!\n");
        else {
            if (!password.equals(confirmPassword))
                MessageAlert.showErrorMessage(null, "Password does not match!\n");
            else {
                Login login = new Login(username, password, status);
                service.addUser(login);

                Spectator spectator = new Spectator(username, password, status, firstName, lastName, email);
                service.addSpectator(spectator);

                Login loginUser = service.login(login);
                if (login == null)
                    MessageAlert.showErrorMessage(null, "User invalid!\n");
                else {
                    Stage stage = new Stage();
                    stage.setTitle(login.getStatus().toString());

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
