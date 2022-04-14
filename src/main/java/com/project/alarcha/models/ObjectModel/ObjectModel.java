package com.project.alarcha.models.ObjectModel;

import com.project.alarcha.entities.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ObjectModel extends BaseEntity {
    private String name;
    private Long object_type_id;
}
