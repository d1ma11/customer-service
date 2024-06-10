package ru.mts.customerservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mts.customerservice.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepository customerRepository;

    /**
     * Загружает информацию о пользователе по номеру телефона из репозитория.
     * Если пользователь не найден, генерирует исключение {@link UsernameNotFoundException}.
     *
     * @param phone Номер телефона пользователя
     * @return Информация о пользователе
     * @throws UsernameNotFoundException Если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        return customerRepository.findByPhone(phone).get();
    }
}
