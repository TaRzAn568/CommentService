package com.example.Comment_Service.repository;

import com.example.Comment_Service.model.Post;
import com.example.Comment_Service.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser(User user, Pageable pageable);
}
