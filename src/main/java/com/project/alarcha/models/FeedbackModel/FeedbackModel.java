package com.project.alarcha.models.FeedbackModel;


import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class FeedbackModel extends BaseModel {
    @NotEmpty(message = "Email должен быть обязательно заполнен")
    @Email(message = "Введите правильный формат email")
    private String email;
    @NotEmpty(message = "Имя должно быть обязательно заполнено")
    @Length(max = 50, message = "Имя слишком длинное, должно быть не более 50 символов")
    private String name;
    @NotEmpty(message = "Сообщение должно быть обязательно заполнено")
    private String message;
}
