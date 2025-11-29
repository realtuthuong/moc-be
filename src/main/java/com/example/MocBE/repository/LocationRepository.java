package com.example.MocBE.repository;

import com.example.MocBE.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    Page<Location> findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(
    String name, String address, Pageable pageable);
}
