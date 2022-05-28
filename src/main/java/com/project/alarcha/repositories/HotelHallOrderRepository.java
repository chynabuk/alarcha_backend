package com.project.alarcha.repositories;

import com.project.alarcha.entities.HotelHallOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelHallOrderRepository extends JpaRepository<HotelHallOrder, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM hotel_hall_orders " +
            "WHERE NOT is_deleted " +
            "ORDER BY id DESC")
    List<HotelHallOrder> getAllHotelHallOrders();

    @Query(nativeQuery = true, value = "SELECT * FROM hotel_hall_orders " +
            "WHERE NOT is_deleted " +
            "ORDER BY id DESC")
    Page<HotelHallOrder> getAll(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM hotel_hall_orders " +
            "WHERE NOT is_deleted AND order_status = 'IN_PROCESS' " +
            "ORDER BY id DESC")
    Page<HotelHallOrder> getInProcessOrders(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM hotel_hall_orders " +
            "WHERE NOT is_deleted AND ( order_status = 'CONFIRMED' OR order_status = 'DECLINED' OR order_status = 'PAID' )" +
            " ORDER BY id DESC")
    Page<HotelHallOrder> getConfirmedOrDeclinedOrders(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM hotel_hall_orders " +
            "WHERE NOT is_deleted AND order_status = 'CHECK_CHECK'" +
            " ORDER BY id DESC")
    Page<HotelHallOrder> getInCheckPay(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM hotel_hall_orders " +
            "WHERE NOT is_deleted AND order_status = 'PAID'" +
            " ORDER BY id DESC")
    Page<HotelHallOrder> getCheckedPay(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM hotel_hall_orders " +
            "WHERE NOT is_deleted AND user_id = :userId ORDER BY id DESC")
    List<HotelHallOrder> getByUserId(@Param("userId") long userId, Pageable pageable);
}