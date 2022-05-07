package com.project.alarcha.service.impl;


import com.project.alarcha.entities.Feedback;
import com.project.alarcha.exception.ApiFailException;
import com.project.alarcha.models.FeedbackModel.FeedbackModel;
import com.project.alarcha.models.FeedbackModel.ReplyToUserMessageModel;
import com.project.alarcha.repositories.FeedbackRepository;
import com.project.alarcha.service.EmailSenderService;
import com.project.alarcha.service.FeedbackService;
import com.project.alarcha.util.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private FeedbackRepository feedbackRepository;

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

    @Override
    public List<Feedback> getAll() {
        return feedbackRepository.findAll();
    }

    @Override
    public ReplyToUserMessageModel replyToUserMessage(ReplyToUserMessageModel replyToUserMessageModel) {
        Feedback feedback = feedbackRepository.getById(replyToUserMessageModel.getUserFeedBackId());

        if (feedback == null){
            throw new ApiFailException("FeedBack is not found");
        }

        if (!feedback.getIsReplied()){
            emailSenderService.sendEmail(feedback.getEmail(), replyToUserMessageModel.getSubject(), replyToUserMessageModel.getBody());
            feedback.setIsReplied(true);
        }

        feedbackRepository.save(feedback);

        return replyToUserMessageModel;
    }

    private Feedback initAndGet(Feedback feedback, FeedbackModel feedbackModel){
        feedback.setEmail(feedbackModel.getEmail());
        feedback.setName(feedbackModel.getName());
        feedback.setMessage(feedbackModel.getMessage());
        feedback.setIsReplied(false);

        return feedback;
    }
}
