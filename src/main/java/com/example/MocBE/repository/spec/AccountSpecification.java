package com.example.MocBE.repository.spec;
import com.example.MocBE.model.Account;
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecification {

    public static Specification<Account> usernameContains(String username) {
        return (root, query, cb) ->
                username == null ? null :
                        cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<Account> fullNameContains(String fullName) {
        return (root, query, cb) ->
                fullName == null ? null :
                        cb.like(cb.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%");
    }

    public static Specification<Account> roleEquals(String roleName) {
        return (root, query, cb) ->
                roleName == null ? null :
                        cb.equal(cb.lower(root.get("role").get("name")), roleName.toLowerCase());
    }

    public static Specification<Account> phoneContains(String phone) {
        return (root, query, cb) ->
                phone == null ? null :
                        cb.like(root.get("phone"), "%" + phone + "%");
    }

    public static Specification<Account> emailContains(String email) {
        return (root, query, cb) ->
                email == null ? null :
                        cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<Account> isNotDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("isDeleted"));
    }

    public static Specification<Account> locationIsNotDeleted() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isFalse(root.get("location").get("isDeleted"));
    }

    public static Specification<Account> locationIsValid() {
        return (root, query, cb) ->
                cb.and(
                        cb.isFalse(root.get("location").get("isDeleted")), // Location chưa xoá
                        cb.isTrue(root.get("location").get("status"))      // Location đang hoạt động
                );
    }
}
