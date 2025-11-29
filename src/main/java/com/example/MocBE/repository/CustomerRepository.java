package com.example.MocBE.repository;

import com.example.MocBE.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository  extends JpaRepository<Customer, UUID>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByPhone(String phone);
    boolean existsByEmail(String email);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByEmailVerificationToken(String token);

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.isDeleted = false")
    Long countAllActiveCustomers();
}
