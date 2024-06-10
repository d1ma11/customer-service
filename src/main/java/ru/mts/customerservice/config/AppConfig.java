package ru.mts.customerservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mts.customerservice.repository.CustomerRepository;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final CustomerRepository customerRepository;

    /**
     * Создает сервис для поиска пользователей, который использует репозиторий клиентов
     * для поиска пользователя по номеру телефона.
     * Если пользователь не найден, генерируется исключение {@link UsernameNotFoundException}
     *
     * @return Сервис поиска пользователей
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return phone -> customerRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));
    }

    /**
     * /**
     * Создает кодировщик паролей с использованием алгоритма BCrypt
     *
     * @return Кодировщик паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Создает менеджер аутентификации из конфигурации SpringSecurity
     *
     * @param config Конфигурация безопасности
     * @return Менеджер аутентификации
     * @throws Exception При возникновении ошибки
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Создает провайдер аутентификации, который использует сервис поиска пользователей и кодировщик паролей
     *
     * @return Провайдер аутентификации
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
