package com.project.alarcha.models.FeedbackModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class ReplyToUserMessageModel extends BaseModel {
    private Long userFeedBackId;
//    @NotEmpty(message = "Поле должно быть заполнено")
//    @Length(max = 150, message = "Слишком длинное поле")
    private String subject;
//    @NotEmpty(message = "Поле должно быть заполнено")
    private String body;
}
