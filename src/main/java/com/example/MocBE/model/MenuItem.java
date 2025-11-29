package com.example.MocBE.model;
import com.example.MocBE.enums.MenuItemStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Data
@Entity
@Table(name = "menu_item")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuItem {

    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    UUID id;

    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100) COLLATE utf8mb4_unicode_ci")
    String name;

    @Column(nullable = false, precision = 15, scale = 0)
    BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_menu_item_id", nullable = false)
    CategoryMenuItem category;

    @Column(columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    MenuItemStatus status;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP(0)")
    LocalDateTime updatedAt;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP(0)")
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now().withNano(0);
        updatedAt = LocalDateTime.now().withNano(0);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now().withNano(0);
    }

    @Column(nullable = false)
    Boolean isDeleted;

    @OneToMany(mappedBy = "menuItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<MenuItemImages> listImage;

    @OneToMany(mappedBy = "menuItem", fetch = FetchType.LAZY)
    @JsonIgnore
    List<OrderDetail> listOrderDetail;

    @OneToMany(mappedBy = "menuItem", fetch = FetchType.LAZY)
    @JsonIgnore
    List<MenuItemIngredient> listMenuItemIngredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    @JsonIgnore
    Location location;

//    @OneToMany(mappedBy = "menuItem", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JsonIgnore
//    List<Comment> comments;
}
