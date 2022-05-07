package com.project.alarcha.controller;


import com.project.alarcha.models.FeedbackModel.FeedbackModel;
import com.project.alarcha.models.FeedbackModel.ReplyToUserMessageModel;
import com.project.alarcha.service.FeedbackService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseMessage<FeedbackModel> createFeedback(@RequestBody FeedbackModel feedbackCreateModel){
        return new ResponseMessage<FeedbackModel>().prepareSuccessMessage(feedbackService.createFeedback(feedbackCreateModel));
    }

    @PostMapping("/reply")
    public ResponseMessage<ReplyToUserMessageModel> replyToUserMessage(@RequestBody ReplyToUserMessageModel replyToUserMessageModel){
        return new ResponseMessage<ReplyToUserMessageModel>().prepareSuccessMessage(feedbackService.replyToUserMessage(replyToUserMessageModel));
    }
}
