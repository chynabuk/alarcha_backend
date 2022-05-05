package com.project.alarcha.models.RoomModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomTypeImageModel extends BaseModel {
    private Long roomTypeId;
    private String img;
}
