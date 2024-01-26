package client.controller;

import domain.Spectator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import service.IServices;

public class CardPaymentController {

    IServices service;
    Spectator spectator;
    public void setService(IServices service, Spectator spectator) {
        this.service = service;
        this.spectator = spectator;
    }

    @FXML
    public void handleSubmit(ActionEvent actionEvent){
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}
