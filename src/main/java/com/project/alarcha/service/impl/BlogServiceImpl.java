package com.project.alarcha.service.impl;


import com.project.alarcha.entities.Blog;
import com.project.alarcha.models.BlogModel.BlogModel;
import com.project.alarcha.repositories.BlogRepository;
import com.project.alarcha.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    BlogRepository blogRepository;

    @Override
    public BlogModel createBlog(BlogModel blogModel) {
        Blog blog = new Blog();

        blogRepository.save(initAndGet(blog, blogModel));

        return blogModel;
    }

    private Blog initAndGet(Blog blog, BlogModel blogModel){

        blog.setTitle(blogModel.getTitle());

        blog.setDescription(blogModel.getDescription());

        blog.setPhoto(blogModel.getPhoto());

        return blog;
    }

    @Override
    public Blog getById(Long blogId) {
        return blogRepository.getById(blogId);
    }
}
