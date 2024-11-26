package by.bsuir.services;

import by.bsuir.models.dto.Request;
import by.bsuir.repositories.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;

    public List<Request> getRequests() {
        List<Request> requests = new ArrayList<>();
        requestRepository.findAll().forEach(request -> requests.add(request.toRequest()));
        return requests;
    }
}
