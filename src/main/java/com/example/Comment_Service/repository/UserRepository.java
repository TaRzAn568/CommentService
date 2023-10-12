package com.example.Comment_Service.repository;

import com.example.Comment_Service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
