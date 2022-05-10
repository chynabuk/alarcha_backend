package com.project.alarcha.entities;

import com.project.alarcha.enums.OrderStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "additional_service_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdditionalServiceOrder extends BaseEntity {
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Column(name = "registration_date", nullable = false)
    private Date registrationDate;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "start_time", nullable = false)
    private Time startTime;

    @Column(name = "end_time", nullable = false)
    private Time endTime;

    @Column(name = "full_name")
    private String fullName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "additional_service_id", referencedColumnName = "id")
    private AdditionalService additionalService;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus orderStatus;
}
