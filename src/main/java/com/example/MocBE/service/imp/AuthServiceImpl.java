package com.example.MocBE.service.imp;

import com.example.MocBE.dto.request.AuthRequest;
import com.example.MocBE.dto.request.RegisterRequest;
import com.example.MocBE.dto.response.AccountResponse;
import com.example.MocBE.dto.response.LoginResponse;
import com.example.MocBE.dto.response.SuccessResponse;
import com.example.MocBE.dto.response.WrapResponse;
import com.example.MocBE.exception.RegistrationException;
import com.example.MocBE.mapper.AccountMapper;
import com.example.MocBE.mapper.WrapResponseFactory;
import com.example.MocBE.model.Account;
import com.example.MocBE.model.Customer;
import com.example.MocBE.repository.AccountRepository;
import com.example.MocBE.repository.CustomerRepository;
import com.example.MocBE.security.JwtUtil;
import com.example.MocBE.service.AuthService;
import com.example.MocBE.service.EmailService;
import com.example.MocBE.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final WrapResponseFactory wrapResponseFactory;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public WrapResponse<LoginResponse> authenticate(AuthRequest request) {
        try {
            Authentication authenticationRequest =
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

            Authentication authentication = authManager.authenticate(authenticationRequest);
            UserDetails user = (UserDetails) authentication.getPrincipal();

            // Kiểm tra account trước
            Optional<Account> accountOpt = accountRepository.findByUsername(user.getUsername());

            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();
                if(account.getIsDeleted() == true){
                    throw new RuntimeException("Tài khoản đã xóa mền");
                }
                String jwt = jwtUtil.generateToken(
                        account.getId(),
                        account.getUsername(),
                        account.getRole().getName()
                );

                AccountResponse accountResponse = accountMapper.toDto(account);
                LoginResponse loginResponse = LoginResponse.builder()
                        .account(accountResponse)
                        .authInfo(LoginResponse.AuthInfo.builder()
                                .accessToken(jwt)
                                .tokenType("Bearer")
                                .message("Success")
                                .build())
                        .exp(jwtUtil.extractExpiration(jwt).toString())
                        .build();

                return wrapResponseFactory.success(loginResponse, "LOGIN");
            }

            // Nếu không phải account, check customer
            Optional<Customer> customerOpt = customerRepository.findByEmail(user.getUsername());
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                String jwt = jwtUtil.generateToken(
                        customer.getId(),
                        customer.getEmail(),
                        "CUSTOMER"
                );

                LoginResponse loginResponse = LoginResponse.builder()
                        .customerEmail(customer.getEmail())
                        .authInfo(LoginResponse.AuthInfo.builder()
                                .accessToken(jwt)
                                .tokenType("Bearer")
                                .message("Success")
                                .build())
                        .exp(jwtUtil.extractExpiration(jwt).toString())
                        .build();

                return wrapResponseFactory.success(loginResponse, "LOGIN");
            }

            // Nếu không tìm thấy gì
            throw new RuntimeException("Không tìm thấy người dùng");

        } catch (BadCredentialsException e) {
            return wrapResponseFactory.error("LOGIN", "BadCredentials",
                    "Tên đăng nhập hoặc mật khẩu không đúng", null);
        }
    }

    @Override
    public ResponseEntity<SuccessResponse> register(RegisterRequest request) {
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Email đã tồn tại!");
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

        Customer customer = Customer.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .emailVerified(false)
                .emailVerificationToken(token)
                .emailVerificationExpires(expiresAt)
                .build();

        customerRepository.save(customer);

        String verifyLink = "http://localhost:8080/api/auth/verify-email?token=" + token;
        emailService.sendVerificationEmail(customer.getEmail(), verifyLink);

        return ResponseEntity.ok(
                SuccessResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.OK.value())
                        .message("Gửi mail thành công cho email " + request.getEmail())
                        .path("/api/auth/register")
                        .build()
        );
    }

    public String verifyEmail(String token) {
        Customer customer = customerRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Mã xác minh không hợp lệ"));

        if (customer.getEmailVerificationExpires().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Link xác minh đã hết hạng");
        }

        customer.setEmailVerified(true);
        customer.setEmailVerificationToken(null);
        customer.setEmailVerificationExpires(null);
        customerRepository.save(customer);

        return "Email đã xác thực thành công! Bạn có thể đăng nhập.";
    }
}

