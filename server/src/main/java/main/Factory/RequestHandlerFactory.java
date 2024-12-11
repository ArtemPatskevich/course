package main.Factory;

import main.enums.requests.ClientRequestType;
import main.utils.tcp.ServerResponse;

import java.util.HashMap;
import java.util.Map;

public class RequestHandlerFactory {
    private final Map<ClientRequestType, RequestHandler> handlerMap = new HashMap<>();

    public RequestHandlerFactory(ServerResponse serverResponse) {
        handlerMap.put(ClientRequestType.IS_USERNAME_EXISTS, new IsUsernameExistsHandler(serverResponse));
        handlerMap.put(ClientRequestType.REGISTER_CLIENT, new RegisterClientHandler(serverResponse));
        handlerMap.put(ClientRequestType.AUTHORIZE_USER, new AuthorizeUserHandler(serverResponse));
        handlerMap.put(ClientRequestType.GET_REQUESTS, new GetRequestsHandler(serverResponse));
        handlerMap.put(ClientRequestType.GET_USER_BY_USERNAME, new GetUserByUsernameHandler(serverResponse));
        handlerMap.put(ClientRequestType.GET_USERS, new GetUsersHandler(serverResponse));
        handlerMap.put(ClientRequestType.DELETE_USER, new DeleteUserHandler(serverResponse));
        handlerMap.put(ClientRequestType.DELETE_CAR, new DeleteCarHandler(serverResponse));
        handlerMap.put(ClientRequestType.GET_CARS, new GetCarsHandler(serverResponse));
        handlerMap.put(ClientRequestType.ADD_CAR, new AddCarHandler(serverResponse));
        handlerMap.put(ClientRequestType.UPDATE_CAR, new UpdateCarHandler(serverResponse));
        handlerMap.put(ClientRequestType.GET_TEST_DRIVES, new GetTestDrivesHandler(serverResponse));
        handlerMap.put(ClientRequestType.ADD_REQUEST, new AddRequestHandler(serverResponse));
        handlerMap.put(ClientRequestType.ADD_TEST_DRIVE, new AddTestDriveHandler(serverResponse));
        handlerMap.put(ClientRequestType.DELETE_TEST_DRIVE, new DeleteTestDriveHandler(serverResponse));
        handlerMap.put(ClientRequestType.GET_CLIENTS, new GetClientsHandler(serverResponse));
    }

    public RequestHandler getHandler(ClientRequestType clientRequestType) {
        return handlerMap.get(clientRequestType);
    }
}

