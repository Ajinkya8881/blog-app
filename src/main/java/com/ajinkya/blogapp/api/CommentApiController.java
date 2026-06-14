package com.ajinkya.blogapp.api;

import com.ajinkya.blogapp.entity.Comment;
import com.ajinkya.blogapp.entity.Post;
import com.ajinkya.blogapp.service.CommentService;
import com.ajinkya.blogapp.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentApiController {

    private final CommentService commentService;
    private final PostService postService;

    public CommentApiController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<String> createComment(
            @PathVariable Long postId,
            @RequestBody Map<String, String> body
    ) {
        commentService.createComment(postId, body.get("name"), body.get("email"), body.get("comment"));
        return ResponseEntity.status(201).body("Comment added");
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<String> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody Map<String, String> body,
            Authentication auth
    ) {
        Post post = postService.getPostById(postId);
        boolean isAdmin = isAdmin(auth);

        if (!isAdmin && !post.getAuthor().equals(auth.getName())) {
            return ResponseEntity.status(403).body("Only the post owner or admin can edit comments");
        }

        commentService.updateComment(commentId, body.get("comment"));
        return ResponseEntity.ok("Comment updated");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            Authentication auth
    ) {
        Post post = postService.getPostById(postId);
        boolean isAdmin = isAdmin(auth);

        if (!isAdmin && !post.getAuthor().equals(auth.getName())) {
            return ResponseEntity.status(403).body("Only the post owner or admin can delete comments");
        }

        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted");
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}