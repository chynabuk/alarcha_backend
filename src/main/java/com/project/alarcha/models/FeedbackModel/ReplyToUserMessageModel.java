package com.project.alarcha.models.FeedbackModel;

import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReplyToUserMessageModel extends BaseModel {
    private Long userFeedBackId;
    private String subject;
    private String body;
}
