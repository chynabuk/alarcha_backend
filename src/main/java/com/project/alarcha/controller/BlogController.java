package com.project.alarcha.controller;


import com.project.alarcha.models.BlogModel.BlogModel;
import com.project.alarcha.service.BlogService;
import com.project.alarcha.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    BlogService blogService;

    @PostMapping("/create")
    public ResponseMessage<BlogModel> createBlog(@RequestBody BlogModel blogModel){
        return new ResponseMessage<BlogModel>().prepareSuccessMessage(blogService.createBlog(blogModel));
    }
}
