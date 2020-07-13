package az.ibar.payday.ms.auth.controller;

import az.ibar.payday.ms.auth.model.AuthToken;
import az.ibar.payday.ms.auth.model.UserDto;
import az.ibar.payday.ms.auth.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<AuthToken> register(@RequestBody @NotNull UserDto loginUser) {

        return ResponseEntity.ok(authenticationService.authenticate(loginUser));
    }
}
