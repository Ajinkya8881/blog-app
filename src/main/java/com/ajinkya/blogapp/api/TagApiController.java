package com.ajinkya.blogapp.api;

import com.ajinkya.blogapp.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tags")
public class TagApiController {

    private final TagService tagService;

    public TagApiController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @PostMapping
    public ResponseEntity<String> createTag(
            @RequestBody Map<String, String> body,
            Authentication auth
    ) {
        if (!isAdmin(auth)) {
            return ResponseEntity.status(403).body("Only admin can create tags");
        }
        tagService.createTag(body.get("name"));
        return ResponseEntity.status(201).body("Tag created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTag(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication auth
    ) {
        if (!isAdmin(auth)) {
            return ResponseEntity.status(403).body("Only admin can update tags");
        }
        tagService.updateTag(id, body.get("name"));
        return ResponseEntity.ok("Tag updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(
            @PathVariable Long id,
            Authentication auth
    ) {
        if (!isAdmin(auth)) {
            return ResponseEntity.status(403).body("Only admin can delete tags");
        }
        tagService.deleteTag(id);
        return ResponseEntity.ok("Tag deleted");
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}