package main.utils.tcp;

import lombok.RequiredArgsConstructor;
import main.enums.entityAttributes.RoleName;
import main.enums.status.RegistrationStatus;
import main.enums.status.ServerResponseStatus;
import main.models.dto.*;
import main.models.entities.*;
import main.repositories.*;
import main.services.ClientService;
import main.services.RequestService;
import main.services.UserService;
import main.utils.EncryptionUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ServerResponse {
    private final RequestService requestService;
    private final UserService userService;
    private final ClientService clientService;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final TestDriveRepository testDriveRepository;
    private final RequestRepository requestRepository;
    private final ClientRepository clientRepository;

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

    public void getUserByUsername(ObjectOutputStream output, ObjectInputStream input) throws IOException, ClassNotFoundException {
        String username = (String) input.readObject();
        output.writeObject(userRepository.findByUsername(username).toUser());
    }

    public void getUsers(ObjectOutputStream output) throws IOException {
        List<User> users = userRepository.findAllByRole(RoleName.CLIENT).stream()
                                                                        .map(UserEntity::toUser)
                                                                        .toList();
        output.writeObject(users);
    }

    public void deleteUserById(ObjectOutputStream output, ObjectInputStream input) throws IOException {
        try {
            int userId = (Integer) input.readObject();
            userRepository.deleteById(userId);
            output.writeObject(ServerResponseStatus.OK);
        } catch(Exception e) {
            output.writeObject(ServerResponseStatus.ERROR);
        }
    }

    public void deleteCarById(ObjectOutputStream output, ObjectInputStream input) throws IOException {
        try {
            Car car = (Car) input.readObject();
            carRepository.deleteById(car.getId());
            output.writeObject(ServerResponseStatus.OK);
        } catch (Exception e) {
            output.writeObject(ServerResponseStatus.ERROR);
        }
    }

    public void getCars(ObjectOutputStream output) throws IOException {
        Stream<CarEntity> carStream = ((List<CarEntity>) carRepository.findAll()).stream();
        List<Car> cars = carStream.map(CarEntity::toCar).toList();
        output.writeObject(cars);
    }

    public void addCar(ObjectOutputStream output, ObjectInputStream input) throws IOException {
        try {
            Car car = (Car) input.readObject();
            carRepository.save(new CarEntity(car.getId(),
                                            car.getBrand(),
                                            car.getCost(),
                                            car.getPetrolType(),
                                            car.getBodyType(),
                                            car.getImagePath())
            );
            output.writeObject(ServerResponseStatus.OK);
        } catch (Exception e) {
            output.writeObject(ServerResponseStatus.ERROR);
        }
    }

    public void updateCar(ObjectOutputStream output, ObjectInputStream input) throws IOException {
        try {
            Car car = (Car) input.readObject();
            CarEntity carEntity = new CarEntity(car.getId(),
                                            car.getBrand(),
                                            car.getCost(),
                                            car.getPetrolType(),
                                            car.getBodyType(),
                                            car.getImagePath()
            );
            carRepository.save(carEntity);
            output.writeObject(ServerResponseStatus.OK);
        } catch (Exception e) {
            output.writeObject(ServerResponseStatus.ERROR);
        }
    }

    public void getTestDrives(ObjectOutputStream output) throws IOException {
        Stream<TestDriveEntity> stream = ((List<TestDriveEntity>) testDriveRepository.findAll()).stream();
        List<TestDrive> testDrives = stream.map(TestDriveEntity::toTestDrive).toList();
        output.writeObject(testDrives);
    }

    public void addRequest(ObjectOutputStream output, ObjectInputStream input) throws IOException {
        try {
            Request request = (Request) input.readObject();
            RequestEntity requestEntity = new RequestEntity(request);

            requestRepository.save(requestEntity);

            output.writeObject(ServerResponseStatus.OK);
        } catch (Exception e) {
            output.writeObject(ServerResponseStatus.ERROR);
        }
    }

    public void addTestDrive(ObjectOutputStream output, ObjectInputStream input) throws IOException {
        try {
            TestDrive testDrive = (TestDrive) input.readObject();
            testDriveRepository.save(new TestDriveEntity(testDrive));
            output.writeObject(ServerResponseStatus.OK);
        } catch (Exception e) {
            output.writeObject(ServerResponseStatus.ERROR);
        }
    }

    public void deleteTestDrive(ObjectOutputStream output, ObjectInputStream input) throws IOException {
        try {
            Integer testDriveId = (Integer) input.readObject();
            testDriveRepository.deleteById(testDriveId);
            output.writeObject(ServerResponseStatus.OK);
        } catch(Exception e) {
            output.writeObject(ServerResponseStatus.ERROR);
        }
    }

    public void getClients(ObjectOutputStream output) throws IOException {
        Stream<ClientEntity> stream = ((List<ClientEntity>) clientRepository.findAll()).stream();
        List<Client> clients = stream.map(ClientEntity::toClient).toList();
        output.writeObject(clients);
    }

    public void getTopThreeCars(ObjectOutputStream output) throws IOException {
        Stream<CarEntity> cars = requestRepository.getTopThreeCars().stream();
        List<Car> topThreeCars = cars.map(CarEntity::toCar).toList();
        output.writeObject(topThreeCars);
    }
}
