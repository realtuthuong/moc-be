package com.example.MocBE.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuItemImagesRepository extends JpaRepository<com.example.MocBE.model.MenuItemImages, UUID> {
}
