package ru.mts.customerservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mts.customerservice.model.JwtRequest;
import ru.mts.customerservice.model.JwtResponse;
import ru.mts.customerservice.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final AuthenticationService authenticationService;

    /**
     * Обрабатывает запросы пользователя на вход в систему.
     * Принимает информацию в формате JSON, содержащую номер телефона и пароль пользователя
     * После успешной аутентификации возвращает токен JWT
     *
     * @param request текст запроса, содержащий учетные данные пользователя
     * @return объект {@link JwtResponse}, содержащий токен JWT и время его истечения
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello, nigga");
    }
}
