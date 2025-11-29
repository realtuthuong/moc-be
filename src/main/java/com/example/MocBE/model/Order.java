package com.example.MocBE.model;

import com.example.MocBE.enums.OrderStatus;
import com.example.MocBE.enums.OrderType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
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
@jakarta.persistence.Table(name = "`order`") // tránh trùng từ khóa SQL
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_order", length = 50, nullable = false)
    OrderStatus statusOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", length = 50, nullable = false)
    OrderType orderType;

    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "total_price",precision = 15, scale = 0)
    BigDecimal totalPrice;

    @Column(columnDefinition = "TEXT")
    String note;

    @Column(name = "reservation_date", nullable = false)
    LocalDateTime reservationDate;

    @Column(name = "guest_count")
    int guestCount;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true)
    Customer customer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    Table table;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @JsonManagedReference
    List<OrderDetail> listOrderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = true)
    Account account;
}
