package com.example.MocBE.repository.spec;

import com.example.MocBE.enums.MenuItemStatus;
import com.example.MocBE.model.MenuItem;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class MenuItemSpecification {
    public static Specification<MenuItem> nameContains(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<MenuItem> categoryEquals(UUID categoryId) {
        return (root, query, cb) ->
                categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<MenuItem> statusEquals(MenuItemStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<MenuItem> locationEquals(UUID locationId) {
        return (root, query, cb) -> {
            if (locationId == null) return null;
            return cb.equal(root.get("location").get("id"), locationId);
        };
    }
}
