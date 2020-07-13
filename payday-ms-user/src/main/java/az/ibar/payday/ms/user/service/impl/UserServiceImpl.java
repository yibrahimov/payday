package az.ibar.payday.ms.user.service.impl;

import az.ibar.payday.ms.user.model.UserDto;
import az.ibar.payday.ms.user.repository.UserRepository;
import az.ibar.payday.ms.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto get(Long userId) {
        return null;
    }
}
