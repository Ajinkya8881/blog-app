package com.ajinkya.blogapp.controller;


import com.ajinkya.blogapp.repository.TagRepository;
import com.ajinkya.blogapp.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {

        this.tagService = tagService;
    }

    @GetMapping
    public String getAllTags(Model model){
        model.addAttribute("tags", tagService.getAllTags());
        return "tag-list";
    }
    @GetMapping("/new")
    public String showCreateTagForm(){

        return "create-tag";
    }

    @PostMapping
    public String createTag(@RequestParam String name){
        tagService.createTag(name);
        return "redirect:/tags";
    }

    @PostMapping("/{tagId}/delete")
    public String deleteTag(@PathVariable Long tagId){
        tagService.deleteTag(tagId);
        return "redirect:/tags";
    }

    @GetMapping("/{tagId}/edit")
    public String showEditTagForm(@PathVariable Long tagId, Model model){

        model.addAttribute("tag", tagService.getTagById(tagId));

        return "edit-tag";
    }

    @PostMapping("/{tagId}/edit")
    public String updateTag(@PathVariable Long tagId,@RequestParam String name){
        tagService.updateTag(tagId, name);
        return "redirect:/tags";
    }

}
