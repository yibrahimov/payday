package az.ibar.payday.ms.user.service;

import az.ibar.payday.ms.user.model.UserDto;

public interface UserService {
    UserDto get(Long userId);
}
