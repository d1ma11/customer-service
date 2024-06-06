package ru.mts.customerservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.mts.customerservice.config.AppConfig;
import ru.mts.customerservice.config.SecurityConfig;
import ru.mts.customerservice.controller.SecurityController;
import ru.mts.customerservice.entity.User;
import ru.mts.customerservice.model.JwtRequest;
import ru.mts.customerservice.model.JwtResponse;
import ru.mts.customerservice.repository.UserRepository;
import ru.mts.customerservice.security.UserDetailsServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, AppConfig.class})
@WebMvcTest(SecurityController.class)
public class AuthenticationServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void testLoginSuccess() throws Exception {
        JwtRequest request = new JwtRequest("89174108831", "password");
        JwtResponse expectedResponse = new JwtResponse("token", 3600);

        when(userDetailsService.loadUserByUsername(request.getPhone())).thenReturn(
                new User(1, "89174108831", "$2a$12$70TZL1bskdsf5szTdAepTuQW9UkHtBGNaVTnC7k5X5Mg4AsDD.zTK")
        );
        when(jwtService.generateToken(any(User.class))).thenReturn(expectedResponse.getToken());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }

//    @Test
//    @WithMockUser
//    void testLoginFailureWrongCredentials() throws Exception {
//        JwtRequest request = new JwtRequest("89174108831", "wrong_password");
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(request)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    @WithAnonymousUser
//    void testLoginFailureUserNotFound() throws Exception {
//        JwtRequest request = new JwtRequest("nonexistentuser", "password");
//
//        when(userDetailsService.loadUserByUsername(request.getPhone())).thenThrow(new UsernameNotFoundException("User not found"));
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(request)))
//                .andExpect(status().isUnauthorized());
//    }
}

