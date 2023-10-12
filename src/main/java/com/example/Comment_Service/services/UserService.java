package com.example.Comment_Service.services;

import com.example.Comment_Service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto, Long id);
    UserDto getUserById(Long id);
    Page<UserDto> getAllUsers(Pageable pageable);
    void deleteUser(Long id);
}
