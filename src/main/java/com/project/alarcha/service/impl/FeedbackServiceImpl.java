package com.project.alarcha.service.impl;


import com.project.alarcha.entities.Feedback;
import com.project.alarcha.models.FeedbackModel.FeedbackModel;
import com.project.alarcha.repositories.FeedbackRepository;
import com.project.alarcha.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Override
    public FeedbackModel createFeedback(FeedbackModel feedbackModel) {
       Feedback feedback = new Feedback();

       feedbackRepository.save(initAndGet(feedback,feedbackModel));

       return feedbackModel;
    }

    @Override
    public Feedback getById(Long feedbackId) {
        return feedbackRepository.getById(feedbackId);
    }


    private Feedback initAndGet(Feedback feedback, FeedbackModel feedbackModel){
        feedback.setEmail(feedbackModel.getEmail());
        feedback.setName(feedbackModel.getName());
        feedback.setMessage(feedbackModel.getMessage());


        return feedback;
    }
}
