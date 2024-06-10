package ru.mts.customerservice.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import ru.mts.customerservice.entity.BankAccount;
import ru.mts.customerservice.entity.Customer;
import ru.mts.customerservice.model.JwtRequest;
import ru.mts.customerservice.model.JwtResponse;
import ru.mts.customerservice.repository.CustomerRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DataJpaTest
@SpringBootTest
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void login() {
        JwtRequest request = new JwtRequest("1234567890", "password");
        Customer customer = new Customer();
        customer.setId(1);
        customer.setPhone("1234567890");
        customer.setPassword("encodedPassword");

        when(customerRepository.findByPhone(anyString())).thenReturn(
                Optional.of(new Customer(1, "1234567890", "encodedPassword", new BankAccount()))
        );
        when(jwtService.generateToken(any(), any())).thenReturn("token");

        JwtResponse result = authenticationService.login(request);

        assertEquals("token", result.getToken());
        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
    }
}

