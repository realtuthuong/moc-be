package com.example.MocBE.repository.spec;

import com.example.MocBE.dto.request.TableFilterRequest;
import com.example.MocBE.model.Table;
import org.springframework.data.jpa.domain.Specification;

public class TableSpecification {

    public static Specification<Table> filter(TableFilterRequest req) {
        return Specification.where(nameContains(req.getName()))
                .and(statusEquals(req.getTableStatus()))
                .and(locationEquals(req.getLocation()))
                .and(isNotDeleted());
    }

    public static Specification<Table> nameContains(String name) {
        return (root, query, cb) -> name == null ? null :
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Table> statusEquals(Enum status) {
        return (root, query, cb) -> status == null ? null :
                cb.equal(root.get("status"), status);
    }

    public static Specification<Table> locationEquals(String locationName) {
        return (root, query, cb) -> locationName == null ? null :
                cb.like(cb.lower(root.join("location").get("name")), "%" + locationName.toLowerCase() + "%");
    }

    public static Specification<Table> isNotDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("isDeleted"));
    }
}
