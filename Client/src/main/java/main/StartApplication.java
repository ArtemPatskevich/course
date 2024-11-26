package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.utils.tcp.ClientRequest;
import main.utils.tcp.ServerSocketInfo;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/main/Authorization.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);

        stage.setTitle("Authorization");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            connect();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Не удалось подключиться к серверу",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println("Успешно подключено к серверу");
        launch();
    }

    private static void connect() throws IOException {
        ClientRequest.clientSocket = new Socket(ServerSocketInfo.HOST, ServerSocketInfo.PORT);
        ClientRequest.output = new ObjectOutputStream(ClientRequest.clientSocket.getOutputStream());
        ClientRequest.input = new ObjectInputStream(ClientRequest.clientSocket.getInputStream());
    }
}