package az.ibar.payday.ms.auth.service.impl;

import az.ibar.payday.ms.auth.config.JwtTokenUtil;
import az.ibar.payday.ms.auth.model.AuthToken;
import az.ibar.payday.ms.auth.model.UserDto;
import az.ibar.payday.ms.auth.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public AuthToken authenticate(UserDto loginUser) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));

        String token = jwtTokenUtil.generateToken(loginUser.getUsername());

        return AuthToken
                .builder()
                .token(token)
                .username(loginUser.getUsername())
                .build();
    }
}
