//package com.example.MocBE.model;
//
//import jakarta.persistence.*;
//import jakarta.persistence.Table;
//import lombok.AccessLevel;
//import lombok.Data;
//import lombok.experimental.FieldDefaults;
//import org.hibernate.annotations.JdbcTypeCode;
//import org.hibernate.annotations.UuidGenerator;
//import org.hibernate.type.SqlTypes;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Data
//@Entity
//@Table(name = "comment")
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class Comment {
//
//    @Id
//    @GeneratedValue
//    @UuidGenerator
//    @JdbcTypeCode(SqlTypes.CHAR)
//    @Column(name = "id", columnDefinition = "CHAR(36)", updatable = false, nullable = false)
//    UUID id;
//
//    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
//    String comment;
//
//    @Column(name = "rating", nullable = false)
//    Integer rating;
//
//    @Column(name = "comment_date", nullable = false)
//    LocalDateTime commentDate = LocalDateTime.now();
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "account_id", nullable = false)
//    Account account;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "menuItem_id", nullable = false)
//    MenuItem menuItem;
//}
