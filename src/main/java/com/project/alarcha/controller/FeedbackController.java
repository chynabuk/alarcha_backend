package com.project.alarcha.controller;


import com.project.alarcha.models.FeedbackModel.FeedbackModel;
import com.project.alarcha.service.EmailSenderService;
import com.project.alarcha.service.FeedbackService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    FeedbackService feedbackService;

    @PostMapping("/feedback")
    public ResponseMessage<FeedbackModel> createFeedback(@RequestBody FeedbackModel feedbackCreateModel){
        emailSenderService.sendEmail(feedbackCreateModel.getEmail(), feedbackCreateModel.getName(), feedbackCreateModel.getMessage());


        return new ResponseMessage<FeedbackModel>().prepareSuccessMessage(feedbackService.createFeedback(feedbackCreateModel));
    }
}
