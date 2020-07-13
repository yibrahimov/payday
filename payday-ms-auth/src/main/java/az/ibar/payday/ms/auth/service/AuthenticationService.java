package az.ibar.payday.ms.auth.service;

import az.ibar.payday.ms.auth.model.AuthToken;
import az.ibar.payday.ms.auth.model.UserDto;

public interface AuthenticationService {
    AuthToken authenticate(UserDto user);
}
