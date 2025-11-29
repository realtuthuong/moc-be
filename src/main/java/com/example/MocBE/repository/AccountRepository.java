package com.example.MocBE.repository;

import com.example.MocBE.dto.response.AccountStatisticResponse;
import com.example.MocBE.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {

    @Query("SELECT a FROM Account a JOIN FETCH a.role WHERE a.username = :username")
    Optional<Account> findByUsername(@Param("username") String username);

    boolean existsByEmail(String email);

//    @Query("SELECT a FROM Account a WHERE a.isDeleted = false")
//    @EntityGraph(attributePaths = {"role", "location"})
//    Page<Account> findAllActive(Pageable pageable);

    @EntityGraph(attributePaths = {"role", "location"})
    Page<Account> findAll(Specification<Account> spec, Pageable pageable);

    Optional<Account> findByUsernameAndIsDeletedFalse(String username);
    Boolean existsByUsernameAndIsDeletedFalse(String username);
    Boolean existsByEmailAndIsDeletedFalse(String email);

//    @Query("""
//    SELECT new com.example.MocBE.dto.response.AccountStatisticResponse(
//        a.id,
//        a.fullName,
//        a.username,
//        a.salary,
//        COUNT(o),
//        COALESCE(SUM(o.totalPrice), 0)
//    )
//    FROM Account a
//    LEFT JOIN Order o ON o.account.id = a.id
//    WHERE a.isDeleted = FALSE
//    GROUP BY a.id, a.fullName, a.username, a.salary
//    """)
//    List<AccountStatisticResponse> getAllAccountStatistics();

    @Query("""
    SELECT new com.example.MocBE.dto.response.AccountStatisticResponse(
        a.id,
        a.fullName,
        a.salary,
        COUNT(o.id),
        COALESCE(SUM(o.totalPrice), 0)
    )
    FROM Account a
    LEFT JOIN Order o ON o.account.id = a.id
    WHERE a.id = :accountId
    GROUP BY a.id, a.fullName, a.salary
    """)
    AccountStatisticResponse getAccountStatistics(UUID accountId);


}
