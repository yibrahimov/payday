package az.ibar.payday.ms.auth.repository;

import az.ibar.payday.ms.auth.entity.UserEntity;

public interface UserRepository {

    UserEntity findByUsername(String username);
}
