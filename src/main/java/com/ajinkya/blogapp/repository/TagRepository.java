package com.ajinkya.blogapp.repository;

import com.ajinkya.blogapp.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
