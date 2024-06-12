package ru.mts.customerservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import ru.mts.customerservice.security.UserDetailsServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("89174108831");
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    public void testValidateToken() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("89174108831");
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    public void testExtractUsername() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("89174108831");
        String token = jwtService.generateToken(userDetails);
        assertEquals("89174108831", jwtService.extractUserPhone(token));
    }
}
