package by.bsuir.utils.tcp;

import by.bsuir.enums.status.RegistrationStatus;
import by.bsuir.models.dto.Client;
import by.bsuir.models.dto.Request;
import by.bsuir.models.dto.User;
import by.bsuir.services.ClientService;
import by.bsuir.services.RequestService;
import by.bsuir.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ServerResponse {
    private final RequestService requestService;
    private final UserService userService;
    private final ClientService clientService;

    public void getRequests(ObjectOutputStream output) throws IOException {
        List<Request> requests = requestService.getRequests();
        output.writeObject(requests);
    }

    public void registerClient(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        User user = (User) input.readObject();
        Client client = (Client) input.readObject();

        RegistrationStatus registrationStatus = userService.registerUser(user);
        if(!registrationStatus.equals(RegistrationStatus.OK)) {
            output.writeObject(registrationStatus);
            return;
        }
        clientService.registerClient(client);
        output.writeObject(registrationStatus);
    }

}
