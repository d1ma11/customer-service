package ru.mts.customerservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mts.customerservice.entity.Customer;
import ru.mts.customerservice.model.JwtRequest;
import ru.mts.customerservice.model.JwtResponse;
import ru.mts.customerservice.service.AuthenticationService;
import ru.mts.customerservice.service.CustomerService;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final AuthenticationService authenticationService;
    private final CustomerService customerService;

    /**
     * Обрабатывает запросы пользователя для авторизации в системе
     *
     * @param request запрос, в котором указаны номер телефона и пароль пользователя
     * @return Объект {@link JwtResponse}, содержащий токен JWT и время его истечения
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer customerId) {
        Customer customer = customerService.findCustomer(customerId);
        return new ResponseEntity<>(customer, HttpStatus.FOUND);
    }
}
