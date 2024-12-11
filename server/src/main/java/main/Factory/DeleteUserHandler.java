package main.Factory;

import main.utils.tcp.ServerResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeleteUserHandler implements RequestHandler {
    private final ServerResponse serverResponse;

    public DeleteUserHandler(ServerResponse serverResponse) {
        this.serverResponse = serverResponse;
    }

    @Override
    public void handle(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        serverResponse.deleteUserById(output, input);
    }
}

