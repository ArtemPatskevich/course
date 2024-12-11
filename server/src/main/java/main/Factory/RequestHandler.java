package main.Factory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface RequestHandler {
    void handle(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException;
}
