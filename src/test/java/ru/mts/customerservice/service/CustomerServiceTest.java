package ru.mts.customerservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.mts.customerservice.entity.Customer;
import ru.mts.customerservice.exception.CustomerNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Test
    public void testFindCustomerById() {
        Customer customer = customerService.findCustomer(1);
        assertNotNull(customer);
        assertEquals(1, customer.getId());
    }

    @Test
    public void testFindCustomerByIdNotFound() {
        assertThrows(CustomerNotFoundException.class, () -> customerService.findCustomer(999));
    }
}
