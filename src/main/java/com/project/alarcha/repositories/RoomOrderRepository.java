package com.project.alarcha.repositories;

import com.project.alarcha.entities.RoomOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping
public interface RoomOrderRepository extends JpaRepository<RoomOrder, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM room_orders " +
            "WHERE NOT is_deleted " +
            "ORDER BY id DESC")
    List<RoomOrder> getAllRoomOrders();

    @Query(nativeQuery = true, value = "SELECT * FROM room_orders " +
            "WHERE NOT is_deleted " +
            "ORDER BY id DESC")
    Page<RoomOrder> getAll(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM room_orders " +
            "WHERE NOT is_deleted AND status = 'IN_PROCESS' " +
            "ORDER BY id DESC")
    Page<RoomOrder> getInProcessOrders(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM room_orders " +
            "WHERE NOT is_deleted AND ( status = 'CONFIRMED' OR status = 'DECLINED' OR status = 'PAID' )" +
            " ORDER BY id DESC")
    Page<RoomOrder> getConfirmedOrDeclinedOrders(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM room_orders " +
            "WHERE NOT is_deleted AND status = 'CHECK_CHECK'" +
            " ORDER BY id DESC")
    Page<RoomOrder> getInCheckPay(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM room_orders " +
            "WHERE NOT is_deleted AND status = 'PAID'" +
            " ORDER BY id DESC")
    Page<RoomOrder> getCheckedPay(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM room_orders " +
            "WHERE NOT is_deleted AND user_id = :userId ORDER BY id DESC")
    List<RoomOrder> getByUserId(@Param("userId") long userId, Pageable pageable);
}
