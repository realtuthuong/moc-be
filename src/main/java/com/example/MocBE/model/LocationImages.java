package com.example.MocBE.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Data
@Entity
@Table(name = "Location_images")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationImages {

    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    UUID id;

    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100) COLLATE utf8mb4_unicode_ci")
    String name;

    @Column(nullable = false, length = 255, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    Location location;

}
