package com.example.MocBE.repository.spec;

import com.example.MocBE.model.Order;
import com.example.MocBE.enums.OrderStatus;
import com.example.MocBE.enums.OrderType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderSpecification {

    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("statusOrder"), status);
    }

    public static Specification<Order> hasType(OrderType type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("orderType"), type);
    }

    public static Specification<Order> customerNameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;
            return cb.like(cb.lower(root.get("customer").get("fullName")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Order> createdAfter(LocalDateTime startDate) {
        return (root, query, cb) ->
                startDate == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), startDate);
    }

    public static Specification<Order> createdBefore(LocalDateTime endDate) {
        return (root, query, cb) ->
                endDate == null ? null : cb.lessThanOrEqualTo(root.get("createdAt"), endDate);
    }
}
