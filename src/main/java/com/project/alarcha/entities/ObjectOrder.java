package com.project.alarcha.entities;

import com.project.alarcha.enums.OrderStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "object_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ObjectOrder extends BaseEntity {
    @Column(name = "start_time")
    private Time startTime;

    @Column(name = "end_time")
    private Time endTime;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Column(name = "start_date")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "full_name")
    private String fullName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "object_id", referencedColumnName = "id")
    private Object object;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "img_of_check")
    private byte[] imgOfCheck;
}