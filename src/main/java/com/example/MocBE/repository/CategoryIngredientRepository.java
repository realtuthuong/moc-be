package com.example.MocBE.repository;


import com.example.MocBE.model.CategoryIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryIngredientRepository extends JpaRepository<CategoryIngredient, UUID> {
    Optional<CategoryIngredient> findByName(String name);
}
