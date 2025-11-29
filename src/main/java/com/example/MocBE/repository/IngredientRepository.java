package com.example.MocBE.repository;

import com.example.MocBE.model.Account;
import com.example.MocBE.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
    @EntityGraph(attributePaths = {"unit", "categoryIngredient"})
    Page<Ingredient> findAll(Specification<Ingredient> spec, Pageable pageable);
}
