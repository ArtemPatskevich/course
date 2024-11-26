package main.utils.tcp;

import main.enums.status.RegistrationStatus;
import main.models.dto.Client;
import main.models.dto.Request;
import main.models.dto.User;
import main.models.entities.UserEntity;
import main.repositories.UserRepository;
import main.services.ClientService;
import main.services.RequestService;
import main.services.UserService;
import lombok.RequiredArgsConstructor;
import main.utils.EncryptionUtil;
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
    private final UserRepository userRepository;

    public void getRequests(ObjectOutputStream output) throws IOException {
        List<Request> requests = requestService.getRequests();
        output.writeObject(requests);
    }

    public void registerClient(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        Client client = (Client) input.readObject();

        RegistrationStatus registrationStatus = userService.registerUser(client.getUser());
        if(!registrationStatus.equals(RegistrationStatus.OK)) {
            output.writeObject(registrationStatus);
            return;
        }
        clientService.registerClient(client);
        output.writeObject(registrationStatus);
    }

    public void authorizeUser(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        User user = (User) input.readObject();
        user.setPassword(EncryptionUtil.hashData(user.getPassword()));
        UserEntity authorizedUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if(authorizedUser == null) {
            output.writeObject(null);
        } else {
            output.writeObject(authorizedUser.toUser());
        }
    }

    public void isUsernameExists(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        String username = (String) input.readObject();
        if(userRepository.existsByUsername(username)) {
            output.writeObject(true);
        } else {
            output.writeObject(false);
        }
    }

}
