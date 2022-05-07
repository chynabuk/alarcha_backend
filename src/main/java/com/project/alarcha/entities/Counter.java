package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "views_counter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Counter extends BaseEntity {
    @Column(name = "count")
    public Integer count;
}
