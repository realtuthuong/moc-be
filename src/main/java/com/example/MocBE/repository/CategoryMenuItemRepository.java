package com.example.MocBE.repository;

import com.example.MocBE.model.CategoryMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryMenuItemRepository extends JpaRepository<CategoryMenuItem, UUID>, JpaSpecificationExecutor<CategoryMenuItem> {

}

