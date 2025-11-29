package com.example.MocBE.security;

import com.example.MocBE.model.Account;
import com.example.MocBE.model.Role;
import com.example.MocBE.repository.AccountRepository;
import com.example.MocBE.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            logger.info("Tài khoản '{}' được tìm thấy", username);

            Role role = account.getRole();
            if (role == null || role.getName() == null) {
                logger.error("Tài khoản '{}' không có role hoặc role name null", username);
                throw new UsernameNotFoundException("Tài khoản không có quyền truy cập");
            }

            logger.info("Tài khoản '{}' đăng nhập với role: {}", username, role.getName());
            return new org.springframework.security.core.userdetails.User(
                    account.getUsername(),
                    account.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + role.getName()))
            );
        }

        // --- customer ---
        return customerRepository.findByEmail(username)
                .map(customer -> {
                    if (!customer.isEmailVerified()) {
                        throw new UsernameNotFoundException("Email chưa xác thực!");
                    }
                    logger.info("Khách hàng '{}' đăng nhập", username);
                    return new org.springframework.security.core.userdetails.User(
                            customer.getEmail(),
                            customer.getPassword(),
                            List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
                    );
                })
                .orElseThrow(() -> {
                    logger.warn("Không tìm thấy account hoặc customer '{}'", username);
                    return new UsernameNotFoundException("Tài khoản không tồn tại");
                });
    }

}
