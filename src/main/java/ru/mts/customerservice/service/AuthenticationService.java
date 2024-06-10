package ru.mts.customerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.mts.customerservice.entity.Customer;
import ru.mts.customerservice.model.JwtRequest;
import ru.mts.customerservice.model.JwtResponse;
import ru.mts.customerservice.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;

    /**
     * Выполняет аутентификацию пользователя на основе предоставленных учетных данных и генерирует токен JWT
     *
     * @param request содержит номер телефона и пароль пользователя
     * @return Объект {@link JwtResponse}, содержащий токен JWT и время его истечения
     */
    public JwtResponse login(JwtRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhone(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Customer customer = customerRepository.findByPhone(request.getPhone()).get();
        String jwtToken = jwtService.generateToken(customer);

        return JwtResponse.builder()
                .token(jwtToken)
                .expiration(jwtService.getExpirationTime())
                .build();
    }
}
