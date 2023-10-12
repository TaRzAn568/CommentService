package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.dto.UserDto;
import com.example.Comment_Service.exception.ResourceNotFoundException;
import com.example.Comment_Service.mapping.UserMapper;
import com.example.Comment_Service.model.User;
import com.example.Comment_Service.repository.UserRepository;
import com.example.Comment_Service.services.UserService;
import com.example.Comment_Service.utils.ModelMapperConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        User user = userRepository.findById(id).orElseThrow(()->(new ResourceNotFoundException("User", "id", id)));
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->(new ResourceNotFoundException("User", "id", id)));
        return userMapper.toDto(user);
    }

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<UserDto> userDtos = users.getContent().stream().map(user-> userMapper.toDto(user)).toList();
        return new PageImpl<>(userDtos, pageable, users.getTotalElements());
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->(new ResourceNotFoundException("User", "id", id)));
        userRepository.delete(user);
    }

}
