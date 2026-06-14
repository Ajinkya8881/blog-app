package com.ajinkya.blogapp.service;


import com.ajinkya.blogapp.entity.Tag;
import com.ajinkya.blogapp.repository.TagRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags(){
        return tagRepository.findAll();
    }

    public void createTag(String name){
        Tag tag = new Tag();
        tag.setName(name);
        tagRepository.save(tag);
    }

    @Transactional
    public void deleteTag(Long tagId) {

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow();

        tag.getPosts().forEach(post ->
                post.getTags().remove(tag));

        tagRepository.delete(tag);
    }

    public Tag getTagById(Long tagId){
        return tagRepository.findById(tagId).orElseThrow();
    }

    public void updateTag(Long tagId, String name){

        Tag tag = tagRepository.findById(tagId).orElseThrow();
        tag.setName(name);
        tagRepository.save(tag);
    }


}
