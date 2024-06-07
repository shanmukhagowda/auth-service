package com.example.auth_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth_service.configuration.JwtService;
import com.example.auth_service.dto.LoginResponse;
import com.example.auth_service.dto.LoginUserDto;
import com.example.auth_service.dto.RegisterUserDto;
import com.example.auth_service.entity.Employee;
import com.example.auth_service.entity.User;
import com.example.auth_service.repository.EmployeeRepository;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.service.UserService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	UserService userService;

	@Autowired
	JwtService jwtService;

	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
		User registeredUser = userService.signup(registerUserDto);

		return ResponseEntity.ok(registeredUser);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
		User authenticatedUser = userService.authenticate(loginUserDto);

		String jwtToken = jwtService.generateToken(authenticatedUser);

		LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}

	@GetMapping("/get/user")
	public ResponseEntity<Page<User>> getEmployees(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(userRepository.findAll(PageRequest.of(page, size)));
	}

	@PostMapping("/add/employee")
	public ResponseEntity<Page<Employee>> addEmployee(@RequestParam(defaultValue = "0") int counter,
			@RequestParam(defaultValue = "10") int maxCount) {


		employeeRepository.addRandomEmployees(counter, maxCount);

		return ResponseEntity.ok(employeeRepository.findAll(PageRequest.of(0, 15)));
	}
}
