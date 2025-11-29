package com.example.MocBE.repository.spec;

import com.example.MocBE.model.Ingredient;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class IngredientSpecification {
    public static Specification<Ingredient> nameContains(String name) {
        return (root, query, cb) ->
                name == null ? null :
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Ingredient> categoryIngredientEquals(String categoryIngredientName) {
        return (root, query, cb) ->
                categoryIngredientName == null ? null :
                        cb.equal(cb.lower(root.get("categoryIngredient").get("name")), categoryIngredientName.toLowerCase());
    }

    public static Specification<Ingredient> unitEquals(String categoryIngredientName) {
        return (root, query, cb) ->
                categoryIngredientName == null ? null :
                        cb.equal(cb.lower(root.get("unit").get("name")), categoryIngredientName.toLowerCase());
    }

    public static Specification<Ingredient> statusEquals(Boolean status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Ingredient> isNotDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("isDeleted"));
    }

    public static Specification<Ingredient> locationEquals(UUID locationId) {
        return (root, query, cb) -> {
            if (locationId == null) return null;
            return cb.equal(root.get("location").get("id"), locationId);
        };
    }
}
