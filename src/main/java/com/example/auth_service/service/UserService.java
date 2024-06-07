package com.example.auth_service.service;

import org.springframework.stereotype.Service;

import com.example.auth_service.dto.LoginUserDto;
import com.example.auth_service.dto.RegisterUserDto;
import com.example.auth_service.entity.User;

@Service
public interface UserService {

	public User signup(RegisterUserDto input);
	
	public User authenticate(LoginUserDto input);
}
