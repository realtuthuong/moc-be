package com.example.MocBE.repository;

import com.example.MocBE.model.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID>, JpaSpecificationExecutor<MenuItem> {
//    @EntityGraph(attributePaths = {"listImage", "category"})
//    Page<MenuItem> findAll(Pageable pageable);

//    @EntityGraph(attributePaths = {"listImage"})
//    Optional<MenuItem> findById(UUID id);
    Optional<MenuItem> findByName(String name);
    List<MenuItem> findByCategoryIdAndIsDeletedFalse(UUID categoryId);
    Page<MenuItem> findByCategoryIdAndIsDeletedFalse(UUID categoryId, Pageable pageable);

    @Query("SELECT COUNT(m) FROM MenuItem m WHERE m.isDeleted = false")
    Long countAllActiveMenuItems();

}
