package com.example.Comment_Service.services.impl;

import com.example.Comment_Service.dto.UserDto;
import com.example.Comment_Service.exception.ResourceNotFoundException;
import com.example.Comment_Service.model.User;
import com.example.Comment_Service.repository.UserRepository;
import com.example.Comment_Service.services.UserService;
import com.example.Comment_Service.utils.ModelMapperConfig;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapperConfig modelMapperConfig;
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = dtoToUser(userDto);
        User savedUser = userRepository.save(user);
        return userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        User user = userRepository.findById(id).orElseThrow(()->(new ResourceNotFoundException("User", "id", id)));
        user.setUsername(userDto.getUsername());
        user.setFName(userDto.getFName());
        user.setLName(userDto.getLName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        userRepository.save(user);
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->(new ResourceNotFoundException("User", "id", id)));
        return userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::userToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->(new ResourceNotFoundException("User", "id", id)));
        userRepository.delete(user);
    }


    private User dtoToUser(UserDto userDto){
        return modelMapperConfig.modelMapper().map(userDto, User.class);
    }
    private UserDto userToDto(User user){
        return modelMapperConfig.modelMapper().map(user, UserDto.class);
    }
}
