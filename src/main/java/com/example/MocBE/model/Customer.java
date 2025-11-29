package com.example.MocBE.model;

import com.example.MocBE.enums.MembershipLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // Hibernate dùng
@AllArgsConstructor                              // Builder cần
@Builder
@Table(name = "customer")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {

    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    UUID id;

    @Column(name = "full_name", nullable = false, length = 100, columnDefinition = "VARCHAR(100) COLLATE utf8mb4_unicode_ci")
    String fullName;

    @Column(nullable = false, length = 20)
    String phone;

    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100) COLLATE utf8mb4_unicode_ci")
    String email;

    private String password;

    private boolean emailVerified = false;
    private String emailVerificationToken;
    private LocalDateTime emailVerificationExpires;

    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "date_of_birth")
    LocalDateTime dateOfBirth;

    @Column(name = "is_deleted")
    Boolean isDeleted;

    @Column(columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String address;

    @Column(name = "total_orders")
    Integer totalOrders;

    @Column(name = "total_spent", precision = 15, scale = 0)
    BigDecimal totalSpent;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_level", columnDefinition = "ENUM('Bronze','Silver','Gold','Platinum')")
    MembershipLevel membershipLevel;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Order> listOrder;
}
