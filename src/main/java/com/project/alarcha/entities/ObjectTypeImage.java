package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "object_type_images")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ObjectTypeImage extends BaseEntity{
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "object_type_id", nullable = false, referencedColumnName = "id")
    private ObjectType objectType;

    @Column(name = "img")
    private byte[] img;
}
