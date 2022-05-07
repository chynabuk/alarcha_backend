package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "room_type_images")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class RoomTypeImage extends BaseEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_type_id", nullable = false, referencedColumnName = "id")
    private RoomType roomType;

    @Column(name = "img")
    private byte[] img;
}
