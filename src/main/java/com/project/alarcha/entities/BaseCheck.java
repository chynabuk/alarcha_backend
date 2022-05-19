package com.project.alarcha.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "base_check")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BaseCheck{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "checked_day")
    private Date checkedDay;

    @Column(name = "next_check_day")
    private Date nextCheckDay;
}
