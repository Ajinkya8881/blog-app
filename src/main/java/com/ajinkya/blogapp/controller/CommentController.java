package com.ajinkya.blogapp.controller;

import com.ajinkya.blogapp.service.CommentService;
import com.ajinkya.blogapp.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/create/{postId}")
    public String createComment(
            @PathVariable Long postId,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String comment
    ) {
        commentService.createComment(postId, name, email, comment);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, Authentication auth) {
        var comment = commentService.getCommentById(commentId);
        Long postId = comment.getPost().getId();
        var post = postService.getPostById(postId);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !post.getAuthor().equals(auth.getName())) {
            return "redirect:/posts/" + postId + "?error=unauthorized";
        }

        commentService.deleteComment(commentId);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/{commentId}/edit")
    public String showEditCommentForm(@PathVariable Long commentId, Model model, Authentication auth) {
        var comment = commentService.getCommentById(commentId);
        var post = postService.getPostById(comment.getPost().getId());

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !post.getAuthor().equals(auth.getName())) {
            return "redirect:/posts/" + post.getId() + "?error=unauthorized";
        }

        model.addAttribute("comment", comment);
        return "edit-comment";
    }

    @PostMapping("/{commentId}/edit")
    public String updateComment(@PathVariable Long commentId, @RequestParam String comment, Authentication auth) {
        var existingComment = commentService.getCommentById(commentId);
        var post = postService.getPostById(existingComment.getPost().getId());

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !post.getAuthor().equals(auth.getName())) {
            return "redirect:/posts/" + post.getId() + "?error=unauthorized";
        }

        Long postId = commentService.updateComment(commentId, comment);
        return "redirect:/posts/" + postId;
    }
}