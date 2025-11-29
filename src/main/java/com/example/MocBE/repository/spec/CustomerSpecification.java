package com.example.MocBE.repository.spec;

import com.example.MocBE.dto.request.CustomerFilterRequest;
import com.example.MocBE.model.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {

    public static Specification<Customer> filter(CustomerFilterRequest filterRequest) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isFalse(root.get("isDeleted")));

            // Keyword search (name OR email OR phone)
            if (filterRequest.getKeyword() != null && !filterRequest.getKeyword().isBlank()) {
                String likePattern = "%" + filterRequest.getKeyword().trim() + "%";
                predicates.add(
                        cb.or(
                                cb.like(root.get("fullName"), likePattern),
                                cb.like(root.get("email"), likePattern),
                                cb.like(root.get("phone"), likePattern)
                        )
                );
            }

            // createdAt between
            if (filterRequest.getCreatedFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("createdAt"), filterRequest.getCreatedFrom().atStartOfDay()
                ));
            }
            if (filterRequest.getCreatedTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("createdAt"), filterRequest.getCreatedTo().atTime(23, 59, 59)
                ));
            }

            // birthMonth
            if (filterRequest.getBirthMonth() != null) {
                predicates.add(cb.equal(
                        cb.function("MONTH", Integer.class, root.get("dateOfBirth")),
                        filterRequest.getBirthMonth()
                ));
            }

            // city
            if (filterRequest.getCity() != null && !filterRequest.getCity().isBlank()) {
                String cityPattern = "%" + filterRequest.getCity().trim() + "%";
                predicates.add(cb.like(root.get("address"), cityPattern));
            }

            // 🏅 membershipLevel
            if (filterRequest.getMembershipLevel() != null) {
                predicates.add(cb.equal(root.get("membershipLevel"), filterRequest.getMembershipLevel()));
            }

            // totalOrders
            if (filterRequest.getMinTotalOrders() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalOrders"), filterRequest.getMinTotalOrders()));
            }
            if (filterRequest.getMaxTotalOrders() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalOrders"), filterRequest.getMaxTotalOrders()));
            }

            // totalSpent
            if (filterRequest.getMinTotalSpent() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalSpent"), filterRequest.getMinTotalSpent()));
            }
            if (filterRequest.getMaxTotalSpent() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalSpent"), filterRequest.getMaxTotalSpent()));
            }

            // 🔗 Combine tất cả conditions bằng AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
