package com.project.alarcha.repositories;

import com.project.alarcha.entities.ObjectOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ObjectOrderRepository extends JpaRepository<ObjectOrder, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM object_orders " +
            "WHERE NOT is_deleted " +
            "ORDER BY id DESC")
    Page<ObjectOrder> getAll(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM object_orders " +
            "WHERE NOT is_deleted AND status = 'IN_PROCESS' " +
            "ORDER BY id DESC")
    Page<ObjectOrder> getInProcessOrders(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM object_orders " +
            "WHERE NOT is_deleted AND ( status = 'CONFIRMED' OR status = 'DECLINED' OR status = 'PAID' )" +
            " ORDER BY id DESC")
    Page<ObjectOrder> getConfirmedOrDeclinedOrders(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM object_orders " +
            "WHERE NOT is_deleted AND status = 'CHECK_CHECK'" +
            " ORDER BY id DESC")
    Page<ObjectOrder> getInCheckPay(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM object_orders " +
            "WHERE NOT is_deleted AND status = 'PAID'" +
            " ORDER BY id DESC")
    Page<ObjectOrder> getCheckedPay(PageRequest of);
}
