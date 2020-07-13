package az.ibar.payday.ms.auth.repository.postgres;

import az.ibar.payday.ms.auth.entity.UserEntity;
import az.ibar.payday.ms.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryPostgres implements UserRepository {

    @Value("${tmp.user.username}")
    String username;
    @Value("${tmp.user.password}")
    String password;

    @Override
    public UserEntity findByUsername(String username) {

        return UserEntity
                .builder()
                .password(password)
                .username(username)
                .build();
    }
}
