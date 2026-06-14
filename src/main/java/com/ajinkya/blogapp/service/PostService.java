package com.ajinkya.blogapp.service;

import com.ajinkya.blogapp.entity.Post;
import com.ajinkya.blogapp.entity.Tag;
import com.ajinkya.blogapp.repository.PostRepository;
import com.ajinkya.blogapp.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public PostService(PostRepository postRepository,
                       TagRepository tagRepository){

        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    public Page<Post> getPostPage(int page, int size){

        Pageable pageable =
                PageRequest.of(page,size);

        return postRepository.findAll(pageable);
    }

    public void createPost(
            String title,
            String excerpt,
            String author,
            String content,
            List<Long> tagIds
    ){

        Post post = new Post();

        List<Tag> tags =
                tagRepository.findAllById(tagIds);

        post.setTitle(title);
        post.setExcerpt(excerpt);
        post.setAuthor(author);
        post.setContent(content);

        post.setPublishedAt(
                LocalDateTime.now()
        );

        post.setCreatedAt(
                LocalDateTime.now()
        );

        post.setUpdatedAt(
                LocalDateTime.now()
        );

        post.getTags().addAll(tags);
        post.setIsPublished(true);


        postRepository.save(post);
    }

    public Post getPostById(Long id){

        return postRepository
                .findById(id)
                .orElseThrow();
    }

    public List<Post> getPostsByTag(Long tagId){

        return postRepository.findByTagsId(tagId);
    }

    public List<Post> getPostsByAuthor(String author){

        return postRepository.findByAuthorContainingIgnoreCase(author);
    }

    public void updatePost(Long id,
                           String title,
                           String excerpt,
                           String author,
                           String content,
                           List<Long> tagIds){

        Post post =
                postRepository.findById(id)
                        .orElseThrow();

        List<Tag> tags =
                tagRepository.findAllById(tagIds);

        post.setTitle(title);
        post.setExcerpt(excerpt);
        post.setAuthor(author);
        post.setContent(content);

        post.setUpdatedAt(
                LocalDateTime.now()
        );

        post.getTags().clear();
        post.getTags().addAll(tags);

        postRepository.save(post);
    }

    public void deletePost(Long id){

        postRepository.deleteById(id);
    }

    public List<Post> searchPosts(String search){

        Set<Post> results =
                new HashSet<>();

        results.addAll(
                postRepository
                        .findByTitleContainingIgnoreCaseOrExcerptContainingIgnoreCaseOrContentContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                                search,
                                search,
                                search,
                                search
                        )
        );

        results.addAll(
                postRepository.findByTagsNameContainingIgnoreCase(search)
        );

        return List.copyOf(results);
    }

    public List<Post> getAllPostsSorted(String direction){

        Sort sort;

        if(direction.equalsIgnoreCase("asc")){

            sort = Sort.by("publishedAt")
                    .ascending();

        }else{

            sort = Sort.by("publishedAt")
                    .descending();
        }

        return postRepository.findAll(sort);
    }
}