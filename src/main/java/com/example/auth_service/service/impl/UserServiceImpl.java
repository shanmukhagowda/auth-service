package com.example.auth_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.auth_service.dto.LoginUserDto;
import com.example.auth_service.dto.RegisterUserDto;
import com.example.auth_service.entity.User;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.service.UserService;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Override
	public User signup(RegisterUserDto input) {
		User user = new User(null, input.getFullName(), input.getEmail(), input.getUsername(),
				passwordEncoder.encode(input.getPassword()));

		return userRepository.save(user);
	}

	@Override
	public User authenticate(LoginUserDto input) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword()));

		return userRepository.findByUsername(input.getUsername()).orElseThrow();
	}

}
