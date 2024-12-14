package main.Factory;

import main.utils.tcp.ServerResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GetTopThreeCars implements RequestHandler {
    private final ServerResponse serverResponse;

    public GetTopThreeCars(ServerResponse serverResponse) {
        this.serverResponse = serverResponse;
    }

    @Override
    public void handle(ObjectOutputStream output, ObjectInputStream input) throws IOException {
        serverResponse.getTopThreeCars(output);
    }
}
