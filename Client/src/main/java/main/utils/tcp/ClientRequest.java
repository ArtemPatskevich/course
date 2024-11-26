package main.utils.tcp;

import main.enums.requests.ClientRequestType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientRequest {
    public static Socket clientSocket;
    public static ObjectOutputStream output;
    public static ObjectInputStream input;

    public static void sendRequestType(ClientRequestType requestType) throws IOException {
        output.writeObject(requestType);
    }
}
