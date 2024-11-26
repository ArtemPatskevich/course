package by.bsuir.utils.tcp;

import by.bsuir.enums.requests.ServerResponseStatus;
import by.bsuir.models.dto.Request;
import by.bsuir.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ServerResponse {
    private final RequestService requestService;

    public void getRequests(ObjectOutputStream output) throws IOException {
        List<Request> requests = requestService.getRequests();
        output.writeObject(requests);
    }

}
