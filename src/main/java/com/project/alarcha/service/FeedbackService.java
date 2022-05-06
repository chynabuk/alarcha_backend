package com.project.alarcha.service;

import com.project.alarcha.entities.Feedback;
import com.project.alarcha.models.FeedbackModel.FeedbackModel;

public interface FeedbackService {
    FeedbackModel createFeedback(FeedbackModel feedbackModel);
    Feedback getById(Long feedbackId);
}
