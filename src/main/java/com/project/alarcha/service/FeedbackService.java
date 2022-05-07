package com.project.alarcha.service;

import com.project.alarcha.entities.Feedback;
import com.project.alarcha.models.FeedbackModel.FeedbackModel;
import com.project.alarcha.models.FeedbackModel.ReplyToUserMessageModel;

import java.util.List;

public interface FeedbackService {
    FeedbackModel createFeedback(FeedbackModel feedbackModel);
    Feedback getById(Long feedbackId);
    ReplyToUserMessageModel replyToUserMessage(ReplyToUserMessageModel replyToUserMessageModel);
    List<Feedback> getAll();
}
