package com.example.Comment_Service.repository;

import com.example.Comment_Service.entity.Post;
import com.example.Comment_Service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUser(User user, Pageable pageable);
}
