package client.controller;

import domain.Show;
import domain.Spectator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import service.IServices;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AdminMenuController {

    IServices service;
    Spectator spectator;

    public void setService(IServices service, Spectator spectator) throws Exception {
        this.service = service;
        this.spectator = spectator;

        initShowModel();
    }

    public void initialize() {
        initializeShowTable();
    }

    @FXML
    private TableView<Show> showsTableView;
    @FXML
    private TableColumn<Show, String> idShow;
    @FXML
    private TableColumn<Show, String> name;
    @FXML
    private TableColumn<Show, String> date;
    @FXML
    private TableColumn<Show, String> time;
    @FXML
    private TableColumn<Show, String> price;

    @FXML
    private TextField textFieldName;
    @FXML
    private DatePicker datePickerDate;
    @FXML
    private TextField textFieldTime;
    @FXML
    private TextField textFieldPrice;

    ObservableList<Show> modelShow = FXCollections.observableArrayList();

    private void initializeShowTable() {
        idShow.setCellValueFactory(new PropertyValueFactory<Show, String>("IdString"));
        name.setCellValueFactory(new PropertyValueFactory<Show, String>("NameString"));
        date.setCellValueFactory(new PropertyValueFactory<Show, String>("DateString"));
        time.setCellValueFactory(new PropertyValueFactory<Show, String>("TimeString"));
        price.setCellValueFactory(new PropertyValueFactory<Show, String>("PriceString"));

        showsTableView.setItems(modelShow);
    }

    private void initShowModel() throws Exception {
        List<Show> shows = new ArrayList<>();
        for (Show show:service.getAllShows()) {
            shows.add(show);
        }

        modelShow.setAll(shows);
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) throws Exception {
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("views/LoginView.fxml"));

        Pane layout = loader.load();
        stage.setScene(new Scene(layout));

        LoginController loginController = loader.getController();
        loginController.setService(service);

        stage.setTitle("Login");
        stage.show();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    @FXML
    public Show handleSelect() {
        Show show = showsTableView.getSelectionModel().getSelectedItem();
        return show;
    }

    public void handleAdd() throws Exception {
        String name = textFieldName.getText();
        LocalDate date = datePickerDate.getValue();
        LocalTime time = LocalTime.parse(textFieldTime.getText());
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        Double price = Double.valueOf(textFieldPrice.getText());

        Show show = new Show(name, dateTime,price);

        service.addShow(show);
        initShowModel();
        initializeShowTable();
    }

    public void handleUpdate() throws Exception {
        Show show = handleSelect();

        String name = textFieldName.getText();
        if(!name.isEmpty()){
            show.setName(name);
        }
        LocalDate date = datePickerDate.getValue();
        String stringTime = textFieldTime.getText();
        LocalTime time = null;
        if(!stringTime.isEmpty()){
            time = LocalTime.parse(stringTime);
        }
        LocalDateTime dateTime = null;
        if(date == null && time != null) {
            dateTime = LocalDateTime.of(show.getDate(), time);
            show.setDateTime(dateTime);
        }
        if(date != null && time == null) {
            dateTime = LocalDateTime.of(date, show.getTime());
            show.setDateTime(dateTime);
        }
        if(date != null && time != null) {
            dateTime = LocalDateTime.of(date, time);
            show.setDateTime(dateTime);
        }

       String stringPrice = textFieldPrice.getText();
        if(!stringPrice.isEmpty()) {
            Double price = Double.valueOf(textFieldPrice.getText());
            show.setPrice(price);
        }

        service.updateShow(show);
        initShowModel();
        initializeShowTable();
    }

    public void handleDelete() throws Exception {
        Show show = handleSelect();

        service.deleteShow(show);
        initShowModel();
        initializeShowTable();
    }
}
