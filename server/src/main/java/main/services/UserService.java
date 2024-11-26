package main.services;

import main.enums.entityAttributes.RoleName;
import main.enums.status.RegistrationStatus;
import main.models.dto.User;
import main.models.entities.UserEntity;
import main.repositories.RoleRepository;
import main.repositories.UserRepository;
import main.utils.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public RegistrationStatus registerUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            return RegistrationStatus.USERNAME_EXISTS;
        }
        user.setPassword(EncryptionUtil.hashData(user.getPassword()));

        System.out.println(user.getRole().getRolename());

        userRepository.save(new UserEntity(null,
                            user.getUsername(),
                            user.getPassword(),
                            user.getFirstName(),
                            user.getLastName(),
                            null,
                            roleRepository.findIdByName(user.getRole().getRolename()).getId()));
        return RegistrationStatus.OK;
    }
}
