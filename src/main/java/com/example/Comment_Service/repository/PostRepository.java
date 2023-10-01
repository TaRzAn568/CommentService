package com.example.Comment_Service.repository;

import com.example.Comment_Service.model.Post;
import com.example.Comment_Service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUser(User user);
}
