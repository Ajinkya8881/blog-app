package com.ajinkya.blogapp.service;


import com.ajinkya.blogapp.entity.Comment;
import com.ajinkya.blogapp.entity.Post;
import com.ajinkya.blogapp.repository.CommentRepository;
import com.ajinkya.blogapp.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    public CommentService(PostRepository postRepository, CommentRepository commentRepository){

        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public void createComment(
            Long postId,
            String name,
            String email,
            String commentText

    ){

        Post post = postRepository.findById(postId).orElseThrow();

        Comment comment = new Comment();
        comment.setName(name);
        comment.setEmail(email);
        comment.setComment(commentText);
        comment.setPost(post);
        commentRepository.save(comment);

    }

    public Long deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        Long postId = comment.getPost().getId();
        commentRepository.delete(comment);
        return postId;
    }
    public Comment getCommentById(Long commentId){
        return commentRepository.findById(commentId).orElseThrow();
    }
    public Long updateComment(Long commentId,String commentText){
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        comment.setComment(commentText);
        commentRepository.save(comment);

        return comment.getPost().getId();


    }
}
