package ru.mts.customerservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import ru.mts.customerservice.model.JwtRequest;
import ru.mts.customerservice.model.JwtResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    public void testAuthenticateUser() {
        JwtResponse jwtResponse = authenticationService.login(new JwtRequest("89174108831", "password"));
        assertNotNull(jwtResponse);
        assertNotNull(jwtResponse.getToken());
    }

    @Test
    public void testAuthenticateUserBadCredentials() {
        assertThrows(BadCredentialsException.class, () -> authenticationService.login(new JwtRequest("wronguser", "wrongpass")));
    }
}

