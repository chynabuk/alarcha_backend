package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "additional_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdditionalService extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "in_stock")
    private Integer inStock;

    @OneToMany(mappedBy = "additionalService", cascade = CascadeType.ALL)
    private List<AdditionalServiceOrder> additionalServiceOrders;
}
