package com.ajinkya.blogapp.repository;

import com.ajinkya.blogapp.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContainingIgnoreCaseOrExcerptContainingIgnoreCaseOrContentContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title,
            String excerpt,
            String content,
            String author
    );

    List<Post> findByTagsNameContainingIgnoreCase(String tagName);

    List<Post> findByTagsId(Long tagId);

    List<Post> findByAuthorContainingIgnoreCase(String author);


}