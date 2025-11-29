package com.example.MocBE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderDetailRepository extends JpaRepository<com.example.MocBE.model.OrderDetail, UUID> {
    @Query("SELECT od.menuItem, SUM(od.quantity) as totalSold " +
            "FROM OrderDetail od " +
            "GROUP BY od.menuItem " +
            "ORDER BY totalSold DESC")
    List<Object[]> findTopSellingMenuItems();
}
