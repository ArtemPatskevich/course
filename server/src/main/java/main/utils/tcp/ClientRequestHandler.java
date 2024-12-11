package main.utils.tcp;

import main.Factory.RequestHandler;
import main.Factory.RequestHandlerFactory;
import main.Main;
import main.enums.requests.ClientRequestType;

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
            RequestHandlerFactory handlerFactory = new RequestHandlerFactory(serverResponse);

            while (true) {
                ClientRequestType clientRequestType = (ClientRequestType) input.readObject();
                RequestHandler handler = handlerFactory.getHandler(clientRequestType);

                if (handler != null) {
                    handler.handle(output, input);
                } else {
                    throw new IllegalArgumentException("Unknown request type: " + clientRequestType);
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
