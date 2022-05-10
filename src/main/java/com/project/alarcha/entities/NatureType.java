package com.project.alarcha.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "nature_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NatureType extends BaseEntity{
    @Column(name = "type", nullable = false)
    private String type;

    @OneToMany(mappedBy = "natureType", cascade = CascadeType.ALL)
    private List<Nature> natures;
}
