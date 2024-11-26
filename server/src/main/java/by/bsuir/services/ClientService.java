package by.bsuir.services;

import by.bsuir.models.dto.Client;
import by.bsuir.repositories.ClientRepository;
import by.bsuir.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public void registerClient(Client client) {
        Integer userId = userRepository.findByUsername(client.getUser().getUsername());
        clientRepository.save(client.toClientEntity(userId));
    }
}
