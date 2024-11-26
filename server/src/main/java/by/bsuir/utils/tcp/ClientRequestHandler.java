package by.bsuir.utils.tcp;

import by.bsuir.Main;
import by.bsuir.enums.requests.ClientRequestType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientRequestHandler implements Runnable {
    private final ServerResponse serverResponse = Main.springContext.getBean(ServerResponse.class);
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private final Socket clientSocket;

    public ClientRequestHandler(Socket clientSocket) {
        try {
            this.output = new ObjectOutputStream(clientSocket.getOutputStream());
            this.input = new ObjectInputStream(clientSocket.getInputStream());
            this.clientSocket = clientSocket;
        } catch (Exception e) {
            throw new RuntimeException("Unable to get IO streams: " + e.getMessage(), e);
        }
    }

    @Override
    public void run() {
        try {
            ClientRequestType clientRequestType;
            while (true) {
                clientRequestType = (ClientRequestType) input.readObject();
                switch (clientRequestType) {
                    case GET_REQUESTS -> serverResponse.getRequests(output);
                    case REGISTER_CLIENT -> serverResponse.registerClient(output, input);
                }
            }
        } catch (SocketException e) {
            System.out.println("Пользователь " + clientSocket.toString() + " вышел\nПользователей в системе: "
                                                                                    + (--Main.activeUsers));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
