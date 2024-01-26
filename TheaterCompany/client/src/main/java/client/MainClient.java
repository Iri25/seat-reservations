package client;

import client.controller.AuthenticationController;
import client.controller.LoginController;
import client.controller.RegisterController;
import domain.Spectator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.IServices;

import java.io.IOException;

public class MainClient extends Application {

    private IServices server;

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
            System.out.println("Start client");

            server = (IServices) factory.getBean("serv");
            System.out.println("Obtained a reference to remote theater company server");

        }
        catch (Exception exception) {
            System.err.println("Theater Company initialization exception:" + exception);
            exception.printStackTrace();
        }

        Stage stage = new Stage();
        try {
            initWindow(stage);
            stage.setTitle("Authentication");
            stage.show();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    private void initWindow(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("views/AuthenticationView.fxml"));
        Pane layout1 = loader.load();
        primaryStage.setScene(new Scene(layout1));

        AuthenticationController authenticationController = loader.getController();
        authenticationController.setService(server);

        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getClassLoader().getResource("views/LoginView.fxml"));
        Pane layout2 = loader2.load();

        LoginController loginController = loader2.getController();
        loginController.setService(server);

        FXMLLoader loader3 = new FXMLLoader();
        loader3.setLocation(getClass().getClassLoader().getResource("views/RegisterView.fxml"));
        Pane layout3 = loader3.load();

        RegisterController RegisterController = loader3.getController();
        RegisterController.setService(server);

        //authenticationController.setObserver(loginController);


    }

    public static void main(String[] args) {
        launch(args);
    }
}