//package com.example.MocBE.service;
//
//import com.example.MocBE.dto.request.AuthRequest;
//import com.example.MocBE.dto.request.RegisterRequest;
//import com.example.MocBE.dto.response.AuthResponse;
//import com.example.MocBE.dto.response.SuccessResponse;
//import com.example.MocBE.exception.RegistrationException;
//import com.example.MocBE.model.Account;
//import com.example.MocBE.model.Role;
//import com.example.MocBE.repository.AccountRepository;
//import com.example.MocBE.repository.RoleRepository;
//import com.example.MocBE.security.CustomUserDetailsService;
//import com.example.MocBE.security.JwtUtil;
//import com.example.MocBE.service.imp.AuthServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class AuthServiceImplTest {
//
//    @Mock
//    private AuthenticationManager authManager;
//
//    @Mock
//    private CustomUserDetailsService userDetailsService;
//
//    @Mock
//    private JwtUtil jwtUtil;
//
//    @Mock
//    private AccountRepository accountRepository;
//
//    @Mock
//    private RoleRepository roleRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private AuthServiceImpl authService;
//
//    private RegisterRequest registerRequest;
//    private AuthRequest authRequest;
//
//    @BeforeEach
//    void setup() {
//        registerRequest = new RegisterRequest();
//        registerRequest.setUsername("testuser");
//        registerRequest.setPassword("password123");
//        registerRequest.setEmail("test@example.com");
//        registerRequest.setPhone("123456789");
//        registerRequest.setFullName("Test User");
//        registerRequest.setAvatarUrl("avatar.png");
//
//        authRequest = new AuthRequest();
//        authRequest.setUsername("testuser");
//        authRequest.setPassword("password123");
//    }
//
//    @Test
//    void authenticate_shouldReturnToken() {
//        Authentication authentication = mock(Authentication.class);
//        User userDetails = new User("testuser", "password123", new java.util.ArrayList<>());
//
//        when(authManager.authenticate(any())).thenReturn(authentication);
//        when(authentication.getPrincipal()).thenReturn(userDetails);
//        when(jwtUtil.generateToken(userDetails)).thenReturn("fake-jwt-token");
//
//        AuthResponse response = authService.authenticate(authRequest);
//
//        assertNotNull(response);
//        assertEquals("fake-jwt-token", response.getToken());
//    }
//
//    @Test
//    void authenticate_shouldThrowExceptionWithInvalidCredentials() {
//        when(authManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));
//
//        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.authenticate(authRequest));
//        assertEquals("Tên đăng nhập hoặc mật khẩu không đúng", ex.getMessage());
//    }
//
//    @Test
//    void register_shouldCreateNewAccountSuccessfully() {
//        when(accountRepository.findByUsername("testuser")).thenReturn(Optional.empty());
//        when(accountRepository.existsByEmail("test@example.com")).thenReturn(false);
//        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
//
//        Role userRole = new Role();
//        userRole.setName("USER");
//        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
//
//        ResponseEntity<SuccessResponse> response = authService.register(registerRequest);
//
//        assertEquals(201, response.getStatusCodeValue());
//        assertEquals("Đăng ký thành công", response.getBody().getMessage());
//    }
//
//    @Test
//    void register_shouldThrowExceptionIfUsernameExists() {
//        when(accountRepository.findByUsername("testuser")).thenReturn(Optional.of(new Account()));
//
//        assertThrows(RegistrationException.class, () -> authService.register(registerRequest));
//    }
//
//    @Test
//    void register_shouldThrowExceptionIfEmailExists() {
//        when(accountRepository.findByUsername("testuser")).thenReturn(Optional.empty());
//        when(accountRepository.existsByEmail("test@example.com")).thenReturn(true);
//
//        assertThrows(RegistrationException.class, () -> authService.register(registerRequest));
//    }
//
//    @Test
//    void register_shouldThrowExceptionIfRoleNotFound() {
//        when(accountRepository.findByUsername("testuser")).thenReturn(Optional.empty());
//        when(accountRepository.existsByEmail("test@example.com")).thenReturn(false);
//        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());
//
//        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
//    }
//}
