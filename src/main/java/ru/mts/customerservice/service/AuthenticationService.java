package ru.mts.customerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.mts.customerservice.entity.User;
import ru.mts.customerservice.model.JwtRequest;
import ru.mts.customerservice.model.JwtResponse;
import ru.mts.customerservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    /**
     * Выполняет аутентификацию пользователя на основе предоставленных учетных данных и генерирует токен JWT.
     *
     * @param request содержит номер телефона и пароль пользователя.
     * @return объект {@link JwtResponse}, содержащий токен JWT и время его истечения.
     */
    public JwtResponse login(JwtRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhone(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByPhone(request.getPhone()).get();
        String jwtToken = jwtService.generateToken(user);

        return JwtResponse.builder()
                .token(jwtToken)
                .expiration(jwtService.getExpirationTime())
                .build();
    }
}
