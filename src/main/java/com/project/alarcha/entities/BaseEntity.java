package com.project.alarcha.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "deleted_date")
    private Date deletedDate;

    @PostPersist
    public void postPersist() {
        this.createDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = new Date();
    }
}
