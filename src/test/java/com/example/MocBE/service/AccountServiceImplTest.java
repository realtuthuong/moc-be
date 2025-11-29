//package com.example.MocBE.service;
//
//import com.example.MocBE.dto.request.UpdateAccountRequest;
//import com.example.MocBE.dto.response.AccountResponse;
//import com.example.MocBE.dto.response.SuccessResponse;
//import com.example.MocBE.mapper.AccountMapper;
//import com.example.MocBE.model.Account;
//import com.example.MocBE.model.Role;
//import com.example.MocBE.repository.AccountRepository;
//import com.example.MocBE.service.imp.AccountServiceImpl;
//import com.example.MocBE.util.ResponseUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class AccountServiceImplTest {
//
//    @Mock private AccountRepository accountRepository;
//    @Mock private AccountMapper accountMapper;
//    @Mock private PasswordEncoder passwordEncoder;
//
//    @InjectMocks private AccountServiceImpl accountService;
//
//    private Pageable pageable;
//
//    @BeforeEach
//    void setUp() {
//        pageable = PageRequest.of(0, 5, Sort.by("username"));
//    }
//
//    @Test
//    void getAllAccount_shouldReturnPageOfAccountResponse() {
//        Account acc1 = new Account();
//        acc1.setId(UUID.randomUUID());
//        acc1.setUsername("user1");
//        acc1.setEmail("user1@example.com");
//        acc1.setCreatedAt(LocalDateTime.now());
//        acc1.setRole(new Role());
//
//        AccountResponse res1 = new AccountResponse(acc1.getId(), acc1.getUsername(), acc1.getAvatarUrl(), acc1.getFullName(), acc1.getPhone(), acc1.getEmail(), acc1.getCreatedAt(), acc1.getRole(), acc1.getSalary());
//
//        Page<Account> accountPage = new PageImpl<>(List.of(acc1), pageable, 1);
//        when(accountRepository.findAll(pageable)).thenReturn(accountPage);
//        when(accountMapper.toDto(acc1)).thenReturn(res1);
//
//        Page<AccountResponse> result = accountService.getAllAccount(pageable);
//
//        assertNotNull(result);
//        assertEquals(1, result.getContent().size());
//        verify(accountRepository).findAll(pageable);
//        verify(accountMapper).toDto(acc1);
//    }
//
//    @Test
//    void deleteAccount_shouldDeleteSuccessfully() {
//        UUID id = UUID.randomUUID();
//        Account acc = new Account(); acc.setId(id);
//        when(accountRepository.findById(id)).thenReturn(Optional.of(acc));
//
//        ResponseEntity<SuccessResponse> response = accountService.deleteAccount(id);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("Xóa tài khoản thành công", response.getBody().getMessage());
//        verify(accountRepository).delete(acc);
//    }
//
//    @Test
//    void deleteAccount_shouldThrowIfNotFound() {
//        UUID id = UUID.randomUUID();
//        when(accountRepository.findById(id)).thenReturn(Optional.empty());
//        RuntimeException ex = assertThrows(RuntimeException.class, () -> accountService.deleteAccount(id));
//        assertEquals("Không tìm thấy tài khoản có ID: " + id, ex.getMessage());
//    }
//
//    @Test
//    void updateAccount_shouldUpdateSuccessfully() {
//        UUID id = UUID.randomUUID();
//        Account acc = new Account();
//        acc.setId(id);
//        acc.setEmail("old@mail.com");
//        UpdateAccountRequest req = new UpdateAccountRequest();
//        req.setId(id);
//        req.setEmail("new@mail.com");
//        req.setFullName("New Name");
//        req.setPhone("0123456789");
//        req.setAvatarUrl("http://img");
//        req.setPassword("123456");
//
//        when(accountRepository.findById(id)).thenReturn(Optional.of(acc));
//        when(accountRepository.existsByEmail("new@mail.com")).thenReturn(false);
//        when(passwordEncoder.encode("123456")).thenReturn("hashed");
//
//        ResponseEntity<SuccessResponse> res = accountService.updateAccount(req);
//
//        assertEquals(200, res.getStatusCodeValue());
//        assertEquals("Cập nhật tài khoản thành công", res.getBody().getMessage());
//        verify(accountRepository).save(acc);
//    }
//
//    @Test
//    void updateAccount_shouldThrowIfEmailExists() {
//        UUID id = UUID.randomUUID();
//        Account acc = new Account(); acc.setId(id); acc.setEmail("old@mail.com");
//
//        UpdateAccountRequest req = new UpdateAccountRequest();
//        req.setId(id);
//        req.setEmail("new@mail.com");
//
//        when(accountRepository.findById(id)).thenReturn(Optional.of(acc));
//        when(accountRepository.existsByEmail("new@mail.com")).thenReturn(true);
//
//        RuntimeException ex = assertThrows(RuntimeException.class, () -> accountService.updateAccount(req));
//        assertEquals("Email đã tồn tại trong hệ thống.", ex.getMessage());
//    }
//
//    @Test
//    void updateAccount_shouldThrowIfNotFound() {
//        UUID id = UUID.randomUUID();
//        UpdateAccountRequest req = new UpdateAccountRequest();
//        req.setId(id);
//        when(accountRepository.findById(id)).thenReturn(Optional.empty());
//        RuntimeException ex = assertThrows(RuntimeException.class, () -> accountService.updateAccount(req));
//        assertEquals("Không tìm thấy tài khoản với ID: " + id, ex.getMessage());
//    }
//}
