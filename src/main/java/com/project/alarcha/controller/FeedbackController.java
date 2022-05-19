package com.project.alarcha.controller;


import com.project.alarcha.entities.Feedback;
import com.project.alarcha.models.FeedbackModel.FeedbackModel;
import com.project.alarcha.models.FeedbackModel.ReplyToUserMessageModel;
import com.project.alarcha.service.FeedbackService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/get-all")
    public ResponseMessage<List<Feedback>> getAll(){
        return new ResponseMessage<List<Feedback>>().prepareSuccessMessage(feedbackService.getAll());
    }
}
