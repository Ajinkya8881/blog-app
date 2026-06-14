package com.ajinkya.blogapp.controller;

import com.ajinkya.blogapp.service.PostService;
import com.ajinkya.blogapp.service.TagService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final TagService tagService;

    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping
    public String getAllPosts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model
    ) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("posts", postService.searchPosts(search));
            model.addAttribute("search", search);
            return "list";
        }
        if (sort != null) {
            model.addAttribute("posts", postService.getAllPostsSorted(sort));
            return "list";
        }
        if (tagId != null) {
            model.addAttribute("posts", postService.getPostsByTag(tagId));
            return "list";
        }
        if (author != null && !author.isBlank()) {
            model.addAttribute("posts", postService.getPostsByAuthor(author));
            return "list";
        }
        model.addAttribute("postsPage", postService.getPostPage(page, size));
        return "list";
    }

    @GetMapping("/new")
    public String showCreatePostForm(Model model) {
        model.addAttribute("tags", tagService.getAllTags());
        return "create-post";
    }

    @PostMapping
    public String createPost(
            @RequestParam String title,
            @RequestParam String excerpt,
            @RequestParam String content,
            @RequestParam List<Long> tagIds,
            Authentication auth
    ) {
        postService.createPost(title, excerpt, auth.getName(), content, tagIds);
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        return "single-post";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, Authentication auth) {
        var post = postService.getPostById(id);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !post.getAuthor().equals(auth.getName())) {
            return "redirect:/posts?error=unauthorized";
        }

        model.addAttribute("post", post);
        model.addAttribute("tags", tagService.getAllTags());
        return "edit-post";
    }

    @PostMapping("/{id}/edit")
    public String updatePost(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String excerpt,
            @RequestParam(required = false) String author,
            @RequestParam String content,
            @RequestParam List<Long> tagIds,
            Authentication auth
    ) {
        var post = postService.getPostById(id);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !post.getAuthor().equals(auth.getName())) {
            return "redirect:/posts?error=unauthorized";
        }

        String authorToSave = isAdmin ? author : auth.getName();
        postService.updatePost(id, title, excerpt, authorToSave, content, tagIds);
        return "redirect:/posts";
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id, Authentication auth) {
        var post = postService.getPostById(id);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !post.getAuthor().equals(auth.getName())) {
            return "redirect:/posts?error=unauthorized";
        }

        postService.deletePost(id);
        return "redirect:/posts";
    }
}