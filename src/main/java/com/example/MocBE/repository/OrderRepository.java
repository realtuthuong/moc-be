package com.example.MocBE.repository;

import com.example.MocBE.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    @Query("SELECT COUNT(o) > 0 FROM Order o " +
            "WHERE o.table.id = :tableId " +
            "AND FUNCTION('DATE', o.reservationDate) = :reservationDate")
    boolean existsByTableIdAndReservationDate(
            @Param("tableId") UUID tableId,
            @Param("reservationDate") LocalDate reservationDate
    );

    List<Order> findAllByReservationDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.listOrderDetail d " +
            "LEFT JOIN FETCH d.menuItem " +
            "WHERE o.reservationDate BETWEEN :start AND :end")
    List<Order> findAllWithDetailsByReservationDateBetween(
            @Param("start") LocalDateTime startOfDay,
            @Param("end") LocalDateTime endOfDay);

    // Doanh thu theo tháng
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) " +
            "FROM Order o " +
            "WHERE MONTH(o.reservationDate) = :month " +
            "AND YEAR(o.reservationDate) = :year")
    BigDecimal getTotalRevenueByMonth(@Param("month") int month, @Param("year") int year);

    // Doanh thu theo năm
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) " +
            "FROM Order o " +
            "WHERE YEAR(o.reservationDate) = :year")
    BigDecimal getTotalRevenueByYear(@Param("year") int year);


    // Tổng đơn hàng theo tháng
    @Query("SELECT COUNT(o) " +
            "FROM Order o " +
            "WHERE MONTH(o.reservationDate) = :month " +
            "AND YEAR(o.reservationDate) = :year")
    Long getTotalOrdersByMonth(@Param("month") int month, @Param("year") int year);

    // Tổng đơn hàng theo năm
    @Query("SELECT COUNT(o) " +
            "FROM Order o " +
            "WHERE YEAR(o.reservationDate) = :year")
    Long getTotalOrdersByYear(@Param("year") int year);

    @Query("SELECT COUNT(o) FROM Order o " +
            "WHERE o.statusOrder = 'DANG_CHUAN_BI' " +
            "AND FUNCTION('DATE', o.reservationDate) = CURRENT_DATE")
    Long countDangChuanBiToday();

    @Query("SELECT COUNT(o) FROM Order o " +
            "WHERE o.statusOrder = 'DA_HOAN_THANH' " +
            "AND FUNCTION('DATE', o.reservationDate) = CURRENT_DATE")
    Long countDaHoanThanhToday();

    // danh thu theo giờ
    @Query("SELECT FUNCTION('HOUR', o.reservationDate) as hour, " +
            "COALESCE(SUM(o.totalPrice), 0) as revenue " +
            "FROM Order o " +
            "GROUP BY FUNCTION('HOUR', o.reservationDate) " +
            "ORDER BY hour")
    List<Object[]> getRevenueByHourAllTime();


    // order hôm nay By locationId
    List<Order> findAllByReservationDateBetweenAndLocation_Id(
            LocalDateTime start,
            LocalDateTime end,
            UUID locationId
    );

    List<Order> findByCustomer_Id(UUID customerId);

}
