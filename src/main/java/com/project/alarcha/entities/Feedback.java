package com.project.alarcha.entities;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Feedback extends BaseEntity{
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "message", nullable = false)
    private String message;
    @Column(name = "is_replied")
    private Boolean isReplied;
}
