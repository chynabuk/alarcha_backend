package com.project.alarcha.service;

import com.project.alarcha.entities.Blog;
import com.project.alarcha.models.BlogModel.BlogModel;

public interface BlogService {
    BlogModel createBlog(BlogModel blogModel);
    Blog getById(Long blogId);
}
