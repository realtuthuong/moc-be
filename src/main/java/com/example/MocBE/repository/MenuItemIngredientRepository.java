package com.example.MocBE.repository;

import com.example.MocBE.model.MenuItemIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuItemIngredientRepository extends JpaRepository<MenuItemIngredient, UUID> {
}
