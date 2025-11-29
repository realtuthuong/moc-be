package com.example.MocBE.repository;

import com.example.MocBE.model.Account;
import com.example.MocBE.model.Role;
import com.example.MocBE.model.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {

    Page<Unit> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Unit> findByName(String name);
}
