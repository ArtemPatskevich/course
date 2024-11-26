package main.services;

import main.models.dto.Client;
import main.models.entities.ClientEntity;
import main.repositories.ClientRepository;
import main.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public void registerClient(Client client) {
        Integer userId = userRepository.findByUsername(client.getUser().getUsername()).getId();
        clientRepository.save(new ClientEntity(null, null, userId, client.getPhoneNumber(), client.getPassportNumber(), client.getBirthDate()));
    }
}
