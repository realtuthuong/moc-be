package com.example.MocBE.repository;

import com.example.MocBE.enums.TableStatus;
import com.example.MocBE.model.Table;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface TableRepository extends JpaRepository<Table, UUID>, JpaSpecificationExecutor<Table> {
    boolean existsByNameAndLocationIdAndIsDeletedFalse(String name, UUID locationId);
    long countByLocationId(UUID locationId);
    List<Table> findByLocation_Id(UUID locationId);
    List<Table> findByLocation_IdAndStatus(UUID locationId, TableStatus status);
    Page<Table> findAllByLocationIdAndIsDeletedFalse(UUID locationId, Pageable pageable);
}