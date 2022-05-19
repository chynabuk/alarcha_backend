package com.project.alarcha.models.AdditionalServiceModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AdditionalServiceOrderModel extends BaseModel {
    private Long userId;
    private Long additionalServiceId;
    @NotEmpty(message = "Поле должно быть заполнено")
    @Length(max = 100, message = "Слишком длинное полное имя")
    private String fullName;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date registrationDate;

    @NotEmpty(message = "Поле должно быть заполнено")
    private Time startTime;
    @NotEmpty(message = "Поле должно быть заполнено")
    private Time endTime;
}
