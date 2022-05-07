package com.project.alarcha.models.FeedbackModel;


import com.project.alarcha.models.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedbackModel extends BaseModel {
    private String email;
    private String name;
    private String message;
}
