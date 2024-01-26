package client.controller;

import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import service.IServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static domain.SeatStatus.Reserved;

public class SpectatorMenuController {

    IServices service;
    Spectator spectator;

    public void setService(IServices service, Spectator spector) throws Exception {
        this.service = service;
        this.spectator = spector;

        initShowModel();
        initLodgeAModel();
        initLodgeBModel();
    }

    public void initialize() {
        initializeLodgeAComboBox();
        initializeLodgeBComboBox();
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
    private ComboBox<Seat> lodgeAComboBox;
    @FXML
    private ComboBox<Seat> lodgeBComboBox;

    @FXML
    private TableView<Booking> bookingTableView;
    @FXML
    private TableColumn<Booking, String> nameShow;
    @FXML
    private TableColumn<Booking, String> dateShow;
    @FXML
    private TableColumn<Booking, String> timeShow;
    @FXML
    private TableColumn<Booking, String> priceShow;
    @FXML
    private TableColumn<Booking, String> lodgeSeat;
    @FXML
    private TableColumn<Booking, String> rowSeat;
    @FXML
    private TableColumn<Booking, String> numberSeat;

    @FXML
    private RadioButton radioButtonCash;
    @FXML
    private RadioButton radioButtonCard;

    @FXML
    private Label labelMessage;

    ObservableList<Show> modelShow = FXCollections.observableArrayList();
    ObservableList<Booking> modelBooking = FXCollections.observableArrayList();
    ObservableList<Seat> modelLodgeA = FXCollections.observableArrayList();
    ObservableList<Seat> modelLodgeB = FXCollections.observableArrayList();


    private void initializeShowTable() {
        idShow.setCellValueFactory(new PropertyValueFactory<Show, String>("IdString"));
        name.setCellValueFactory(new PropertyValueFactory<Show, String>("NameString"));
        date.setCellValueFactory(new PropertyValueFactory<Show, String>("DateString"));
        time.setCellValueFactory(new PropertyValueFactory<Show, String>("TimeString"));
        price.setCellValueFactory(new PropertyValueFactory<Show, String>("PriceString"));

        showsTableView.setItems(modelShow);
    }

    private void initShowModel() {
       List<Show> shows = new ArrayList<>();
        for (Show show:service.getAllShows()) {
            shows.add(show);
        }

        modelShow.setAll(shows);
    }

    private void initializeLodgeAComboBox() {

        lodgeAComboBox.setItems(modelLodgeA);
    }

    private void initLodgeAModel() {
        List<Seat> seats = new ArrayList<Seat>();
        for (Seat seat : service.getAllSeats()) {
            if(seat.getLodge().equals("A"))
                seats.add(seat);
        }
        modelLodgeA.setAll(seats);
    }

    private void initializeLodgeBComboBox() {

        lodgeBComboBox.setItems(modelLodgeB);
    }

    private void initLodgeBModel() {
        List<Seat> seats = new ArrayList<Seat>();
        for (Seat seat : service.getAllSeats()) {
            if(seat.getLodge().equals("B"))
                seats.add(seat);
        }
        modelLodgeB.setAll(seats);
    }

    private void initializeBookingTable() {
        nameShow.setCellValueFactory(new PropertyValueFactory<Booking, String>("NameShowString"));
        dateShow.setCellValueFactory(new PropertyValueFactory<Booking, String>("DateShowString"));
        timeShow.setCellValueFactory(new PropertyValueFactory<Booking, String>("TimeShowString"));
        priceShow.setCellValueFactory(new PropertyValueFactory<Booking, String>("PriceShowString"));
        lodgeSeat.setCellValueFactory(new PropertyValueFactory<Booking, String>("LodgeSeatString"));
        rowSeat.setCellValueFactory(new PropertyValueFactory<Booking, String>("RowSeatString"));
        numberSeat.setCellValueFactory(new PropertyValueFactory<Booking, String>("NumberSeatString"));

        bookingTableView.setItems(modelBooking);
    }

    private void initBookingModel() {
        Show show = handleSelect();
        Seat seat = handleLodge();
        PayMethod payStatus = null;
        Ticket ticket = new Ticket(show, seat, payStatus);
        Booking booking = new Booking(spectator, ticket);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        modelBooking.setAll(bookings);
    }

    @FXML
    public Show handleSelect() {
        Show show = showsTableView.getSelectionModel().getSelectedItem();
        return show;
    }

    @FXML
    public Seat handleLodge() {
        Seat seat = new Seat();
        Seat seatA = lodgeAComboBox.getValue();
        Seat seatB = lodgeBComboBox.getValue();

        if (seatA != null)
            seat = seatA;
        if (seatB != null)
            seat = seatB;

        return seat;
    }

    @FXML
    public void handleBooking() throws Exception {
        initBookingModel();
        initializeBookingTable();
    }

    @FXML
    public void handleBuy() {
        try {
            Show show = handleSelect();

            Seat seat = handleLodge();
            seat.setSeatStatus(Reserved);
            service.updateSeatStatus(seat);

            PayMethod cash = PayMethod.valueOf(radioButtonCash.getText());
            PayMethod card = PayMethod.valueOf(radioButtonCard.getText());

            boolean isCash = radioButtonCash.isSelected();
            boolean isCard = radioButtonCard.isSelected();

            Ticket ticket;
            if (isCash) {
                ticket = new Ticket(show, seat, cash);
                service.buyTicketBooking(ticket, spectator);
            }

            if (isCard) {
                ticket = new Ticket(show, seat, card);
                service.buyTicketBooking(ticket, spectator);
            }

            labelMessage.setText("Booking successful!");
            initLodgeAModel();
            initLodgeBModel();
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @FXML
    public void handlePaymentByCard() throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Payment By Card");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/CardPaymentView.fxml"));

        Pane layout = loader.load();
        stage.setScene(new Scene(layout));

        CardPaymentController cardPaymentController = loader.getController();
        cardPaymentController.setService(service, spectator);

        stage.show();
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
}
