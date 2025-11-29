package com.example.MocBE.service.imp;

import com.example.MocBE.dto.request.AccountFilterRequest;
import com.example.MocBE.dto.request.CreateAccountRequest;
import com.example.MocBE.dto.request.UpdateAccountRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.exception.RegistrationException;
import com.example.MocBE.mapper.AccountMapper;
import com.example.MocBE.mapper.WrapResponseFactory;
import com.example.MocBE.model.Account;
import com.example.MocBE.model.Location;
import com.example.MocBE.model.Role;
import com.example.MocBE.repository.AccountRepository;
import com.example.MocBE.repository.LocationRepository;
import com.example.MocBE.repository.RoleRepository;
import com.example.MocBE.repository.spec.AccountSpecification;
import com.example.MocBE.service.AccountService;
import com.example.MocBE.service.CloudinaryService;
import com.example.MocBE.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CloudinaryService cloudinaryService;
    private final LocationRepository locationRepository;
    private final WrapResponseFactory wrapResponseFactory;

    @Override
    public WrapResponse<PageResponse<AccountResponse>> getAllAccounts(AccountFilterRequest filterRequest, Pageable pageable) {
        try {
            Specification<Account> spec = Specification
                    .where(AccountSpecification.usernameContains(filterRequest.getUsername()))
                    .and(AccountSpecification.fullNameContains(filterRequest.getFullName()))
                    .and(AccountSpecification.phoneContains(filterRequest.getPhone()))
                    .and(AccountSpecification.emailContains(filterRequest.getEmail()))
                    .and(AccountSpecification.roleEquals(filterRequest.getRoleName()))
                    .and(AccountSpecification.isNotDeleted());

            Page<AccountResponse> accounts = accountRepository.findAll(spec, pageable)
                    .map(accountMapper::toDto);

            PageResponse<AccountResponse> pageResponse = PageResponse.<AccountResponse>builder()
                    .totalItems(accounts.getTotalElements())
                    .totalPages(accounts.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1)
                    .objectJson(accounts.getContent())
                    .build();

            return wrapResponseFactory.success(pageResponse, "GET_ALL_ACCOUNTS");

        } catch (Exception ex) {
            logger.error("Lỗi khi lấy danh sách accounts: ", ex);
            return wrapResponseFactory.error(
                    "GET_ALL_ACCOUNTS",
                    "SYSTEM",
                    ex.getMessage(),
                    null
            );
        }
    }

    @Override
    @Transactional
    public ResponseEntity<SuccessResponse> deleteAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Không tìm thấy tài khoản có ID: {}", id);
                    return new RuntimeException("Không tìm thấy tài khoản có ID: " + id);
                });
        account.setIsDeleted(true);
        accountRepository.save(account);
        SuccessResponse response = ResponseUtil.success(
                "Xóa tài khoản thành công",
                "/api/account/" + id
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public WrapResponse<AccountResponse> getByIdAccount(UUID id) {
        try {
            Account account = accountRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản có ID: " + id));

            AccountResponse response = AccountResponse.builder()
                    .id(account.getId())
                    .username(account.getUsername())
                    .fullName(account.getFullName())
                    .email(account.getEmail())
                    .phone(account.getPhone())
                    .avatarUrl(account.getAvatarUrl())
                    .salary(account.getSalary())
                    .build();

            return wrapResponseFactory.success(response, "GET_ACCOUNT_BY_ID");

        } catch (Exception ex) {
            logger.error("Lỗi khi lấy thông tin account ID {}: {}", id, ex.getMessage(), ex);

            return wrapResponseFactory.error(
                    "GET_ACCOUNT_BY_ID",
                    "SYSTEM",
                    ex.getMessage(),
                    null
            );
        }
    }

    @Override
    @Transactional
    public ResponseEntity<SuccessResponse> updateAccount(UpdateAccountRequest request) throws IOException {
        UUID id = request.getId();
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Không tìm thấy tài khoản để cập nhật: {}", id);
                    return new RuntimeException("Không tìm thấy tài khoản với ID: " + id);
                });

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy roleId: " + request.getRoleId()));

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy locationId: " + request.getLocationId()));

        if (!account.getUsername().equals(request.getUsername())) {
            if (accountRepository.existsByUsernameAndIsDeletedFalse(request.getUsername())) {
                throw new RuntimeException("Tên đăng nhập đã tồn tại trong hệ thống.");
            }
            account.setUsername(request.getUsername());
        }

        if (!account.getEmail().equals(request.getEmail())) {
            if (accountRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
                throw new RuntimeException("Email đã tồn tại trong hệ thống.");
            }
            account.setEmail(request.getEmail());
        }


        if (request.getFile() != null && !request.getFile().isEmpty()) {
            String avatarUrl = cloudinaryService.getImageUrlAfterUpload(request.getFile(), "avatar");
            account.setAvatarUrl(avatarUrl);
        }

        if (!account.getEmail().equals(request.getEmail())) {
            if (accountRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email đã tồn tại trong hệ thống.");
            }
            account.setEmail(request.getEmail());
        }
        account.setUsername(request.getUsername());
        account.setFullName(request.getFullName());
        account.setPhone(request.getPhone());
        account.setLocation(location);
        account.setRole(role);
        account.setSalary(request.getSalary());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            account.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        accountRepository.save(account);

        SuccessResponse response = ResponseUtil.success(
                "Cập nhật tài khoản thành công",
                "/api/account/" + id
        );
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<SuccessResponse> createAccount(CreateAccountRequest request) throws IOException {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RegistrationException("Tên đăng nhập đã tồn tại!");
        }
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email đã được sử dụng!");
        }
        String avatarUrl = null;
        if (request.getFile() != null && !request.getFile().isEmpty()) {
            avatarUrl = cloudinaryService.getImageUrlAfterUpload(request.getFile(), "avatar");
        }

        if (accountRepository.existsByUsernameAndIsDeletedFalse(request.getUsername())) {
            throw new RegistrationException("Tên đăng nhập đã tồn tại!");
        }

        if (accountRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
            throw new RegistrationException("Email đã được sử dụng!");
        }


        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy locationId: " + request.getLocationId()));

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setFullName(request.getFullName());
        account.setLocation(location);
        account.setAvatarUrl(avatarUrl);
        account.setSalary(request.getSalary());
        account.setIsDeleted(false);

        Role roleUser = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id role này!"));
        account.setRole(roleUser);

        accountRepository.save(account);

        SuccessResponse response = ResponseUtil.success("Tạo thành công", "/api/account");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public LocationIdResponse getLocationIdByAccountId(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return new LocationIdResponse(account.getLocation().getId());
    }

//    @Override
//    public WrapResponse<List<AccountStatisticResponse>> getAccountStatistics() {
//        try {
//            List<AccountStatisticResponse> result = accountRepository.getAllAccountStatistics();
//            return wrapResponseFactory.success(result, "GET_ACCOUNT_STATISTICS");
//        } catch (Exception ex) {
//            logger.error("Lỗi khi lấy thống kê account: ", ex);
//            return wrapResponseFactory.error(
//                    "GET_ACCOUNT_STATISTICS",
//                    "SYSTEM",
//                    ex.getMessage(),
//                    null
//            );
//        }
//    }

    @Override
    public WrapResponse<AccountStatisticResponse> getAccountStatistics(UUID id) {
        try {
            AccountStatisticResponse result = accountRepository.getAccountStatistics(id);
            return wrapResponseFactory.success(result, "GET_ACCOUNT_STATISTICS");
        }
        catch (Exception ex) {
            return wrapResponseFactory.error("GET_ACCOUNT_STATISTICS", "SYSTEM", ex.getMessage(), null);
        }
    }

}
