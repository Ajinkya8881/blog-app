package com.ajinkya.blogapp.api;

import com.ajinkya.blogapp.entity.Post;
import com.ajinkya.blogapp.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;

    public PostApiController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        if (search != null) return ResponseEntity.ok(postService.searchPosts(search));
        if (sort != null)   return ResponseEntity.ok(postService.getAllPostsSorted(sort));
        if (tagId != null)  return ResponseEntity.ok(postService.getPostsByTag(tagId));
        if (author != null) return ResponseEntity.ok(postService.getPostsByAuthor(author));
        return ResponseEntity.ok(postService.getPostPage(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping
    public ResponseEntity<String> createPost(
            @RequestBody Map<String, Object> body,
            Authentication auth
    ) {
        boolean isAdmin = isAdmin(auth);

        String author = isAdmin ? (String) body.get("author") : auth.getName();

        postService.createPost(
                (String) body.get("title"),
                (String) body.get("excerpt"),
                author,
                (String) body.get("content"),
                ((List<?>) body.get("tagIds")).stream().map(id -> Long.valueOf(id.toString())).toList()
        );
        return ResponseEntity.status(201).body("Post created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            Authentication auth
    ) {
        Post post = postService.getPostById(id);
        boolean isAdmin = isAdmin(auth);

        if (!isAdmin && !post.getAuthor().equals(auth.getName())) {
            return ResponseEntity.status(403).body("You can only edit your own posts");
        }

        String author = isAdmin ? (String) body.get("author") : auth.getName();

        postService.updatePost(
                id,
                (String) body.get("title"),
                (String) body.get("excerpt"),
                author,
                (String) body.get("content"),
                ((List<?>) body.get("tagIds")).stream().map(i -> Long.valueOf(i.toString())).toList()
        );
        return ResponseEntity.ok("Post updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long id,
            Authentication auth
    ) {
        Post post = postService.getPostById(id);
        boolean isAdmin = isAdmin(auth);

        if (!isAdmin && !post.getAuthor().equals(auth.getName())) {
            return ResponseEntity.status(403).body("You can only delete your own posts");
        }

        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted");
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}