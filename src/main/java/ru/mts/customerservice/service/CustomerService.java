package ru.mts.customerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mts.customerservice.entity.Customer;
import ru.mts.customerservice.exception.CustomerNotFoundException;
import ru.mts.customerservice.repository.CustomerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * Ищет клиента по его идентификатору.
     * Возвращает Optional, содержащий объект Customer, если такой клиент существует,
     * или пустой Optional, если такого клиента нет
     *
     * @param customerId Идентификатор клиента
     * @return Найденного клиента
     */
    public Customer findCustomer(Integer customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty())
            throw new CustomerNotFoundException(
                    "CUSTOMER_NOT_FOUND",
                    "Клиент не найден со следующим id: " + customerId
            );
        return optionalCustomer.get();
    }
}
