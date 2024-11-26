package by.bsuir.services;

import by.bsuir.enums.status.RegistrationStatus;
import by.bsuir.models.dto.User;
import by.bsuir.repositories.UserRepository;
import by.bsuir.utils.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public RegistrationStatus registerUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            return RegistrationStatus.USERNAME_EXISTS;
        }
        user.setPassword(EncryptionUtil.hashData(user.getPassword()));
        userRepository.save(user.toUserEntity());
        return RegistrationStatus.OK;
    }
}
