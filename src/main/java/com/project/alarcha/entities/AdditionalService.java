package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "additional_service")
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
    private float price;
}
